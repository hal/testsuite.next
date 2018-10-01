package org.jboss.hal.testsuite.test.keycloak;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static com.google.common.collect.ObjectArrays.concat;
import static java.util.stream.StreamSupport.stream;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

/**
 * Abstraction for Keycloak server management as well as business operations.<br />
 * Requires {@code keycloak.home} system variable with path to the local Keycloak installation to be passed.
 */
public class KeycloakOperations {

    static final String
        ADMIN_NAME = "admin",
        ADMIN_PASSWORD = "admin",
        WILDFLY_INFRA = "wildfly-infra",
        WILDFLY_CONSOLE = "wildfly-console",
        WILDFLY_MANAGEMENT = "wildfly-management";

    private static final String
        UNDERTOW_REALM = "UndertowRealm",
        KCADMIN = "kcadmin",
        R = "-r", S = "-s";

    // TODO get rid of default path value
    private static final File kcadmDir = new File(ConfigUtils.get("keycloak.home"), "bin");

    private final Operations ops;
    private final Administration adminOps;

    public KeycloakOperations(OnlineManagementClient client) {
        this.ops = new Operations(client);
        this.adminOps = new Administration(client);
    }

    public KeycloakOperations enableSsl() throws IOException, InterruptedException, TimeoutException {
        ops.batch(new Batch()
                .add(Address.coreService(MANAGEMENT).and(SECURITY_REALM, UNDERTOW_REALM))
                .add(Address.coreService(MANAGEMENT).and(SECURITY_REALM, UNDERTOW_REALM).and("server-identity", "ssl"),
                        Values.of("keystore-path", "keycloak.jks")
                            .and("keystore-relative-to", "jboss.server.config.dir")
                            .and("keystore-password", "secure"))).assertSuccess();
        ops.writeAttribute(Address.subsystem(UNDERTOW).and(SERVER, "default-server").and(HTTPS_LISTENER, "https"),
                SECURITY_REALM, UNDERTOW_REALM).assertSuccess();
        adminOps.reloadIfRequired();
        return this;
    }

    public KeycloakOperations createRealm() throws IOException, InterruptedException {
        KcAdmin.create("realms", S, "realm=" + WILDFLY_INFRA, S, "enabled=true");
        return this;
    }

    public KeycloakOperations setupConsoleClient() throws IOException, InterruptedException {
        String consId = createClient(WILDFLY_CONSOLE, WILDFLY_INFRA);
        updateClient(consId, WILDFLY_INFRA,
                "publicClient=true",
                "redirectUris=[\"http://localhost:9990/console/*\",\"https://localhost:9993/console/*\"]",
                "webOrigins=[\"http://localhost:9990\",\"https://localhost:9993\"]");
        return this;
    }

    public KeycloakOperations setupManagementClient() throws IOException, InterruptedException {
        String mgmtId = createClient(WILDFLY_MANAGEMENT, WILDFLY_INFRA);
        updateClient(mgmtId, WILDFLY_INFRA, "bearerOnly=true");
        return this;
    }

    public KeycloakOperations setupConsoleUser() throws IOException, InterruptedException {
        String roleName = "Administrator";
        KcAdmin.create("roles", R, WILDFLY_INFRA, S, "name=" + roleName);
        KcAdmin.create("users", R, WILDFLY_INFRA, S, "username=" + ADMIN_NAME, S, "enabled=true");
        KcAdmin.call("set-password", R, WILDFLY_INFRA, "--username", ADMIN_NAME, "--new-password", ADMIN_PASSWORD);
        KcAdmin.call("add-roles","--uusername", ADMIN_NAME, "--rolename", roleName, R, WILDFLY_INFRA);
        return this;
    }

    public String getPubKey() throws IOException, InterruptedException {
        JsonElement root = KcAdmin.get("realms/" + WILDFLY_INFRA + "/keys");
        Optional<String> pubkey = stream(root.getAsJsonObject().getAsJsonArray("keys").spliterator(), false)
            .filter(e -> e.getAsJsonObject().getAsJsonPrimitive("type").getAsString().equals("RSA"))
            .map(e -> e.getAsJsonObject().getAsJsonPrimitive("publicKey").getAsString()).findFirst();
        return pubkey.get();
    }

    /**
     * @return id of created client application
     */
    private String createClient(String clientName, String realmName) throws IOException, InterruptedException {
        KcAdmin.create("clients", R, realmName, S, "clientId=" + clientName, S, "enabled=true");
        JsonElement root = KcAdmin.get("realms/" + realmName + "/clients");
        Optional<String> id = stream(root.getAsJsonArray().spliterator(), false)
            .filter(e -> e.getAsJsonObject().getAsJsonPrimitive("clientId").getAsString().equals(clientName))
            .map(e -> e.getAsJsonObject().getAsJsonPrimitive("id").getAsString()).findFirst();
        return id.get();
    }

    private KeycloakOperations updateClient(String clientId, String realmName, String... args)
            throws IOException, InterruptedException {
        String[] command = {"clients/" + clientId, R, realmName};
        for (String arg : args) {
            command = concat(command, new String[]{S, arg}, String.class);
        }
        KcAdmin.update(command);
        return this;
    }

    private static class KcAdmin {
        public static void create(String... command) throws IOException, InterruptedException {
            call(concat("create", command));
        }

        public static void update(String... command) throws IOException, InterruptedException {
            call(concat("update", command));
        }

        public static JsonElement get(String... command) throws IOException, InterruptedException {
            Process process = call(concat("get", command));
            Reader reader = new InputStreamReader(process.getInputStream());
            JsonElement root;
            try {
                root = new JsonParser().parse(reader);
            } finally {
                reader.close();
            }
            return root;
        }

        public static Process call(String... command) throws IOException, InterruptedException {
            String[] wholeCommand = concat(concat("./kcadm.sh", command), new String[] {"--server", "http://localhost:8180/auth",
                    "--realm", "master", "--user", KCADMIN, "--password", KCADMIN}, String.class);
            ProcessBuilder pb = new ProcessBuilder(wholeCommand).directory(kcadmDir);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String error;
                try (InputStream errorStream = process.getErrorStream()) {
                    error = IOUtils.toString(errorStream, Charset.defaultCharset());
                }
                throw new RuntimeException("kcadm.sh running command '" + String.join(" ", wholeCommand)
                    + "' finished with exit code '" + exitCode + "' and error stream '" + error + "'.");
            }
            return process;
        }
    }
}
