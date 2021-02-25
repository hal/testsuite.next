package org.jboss.hal.testsuite.test.keycloak;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator.ModelNodeListBuilder;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator.ModelNodePropertiesBuilder;
import org.wildfly.extras.creaper.core.ManagementClient;
import org.wildfly.extras.creaper.core.online.Constants;
import org.wildfly.extras.creaper.core.online.ManagementProtocol;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineOptions;
import org.wildfly.extras.creaper.core.online.SslOptions;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.keycloak.KeycloakOperations.*;

/**
 * Abstraction for Wildfly management operations related to integration with Keycloak
 */
public class KeycloakWildflyOperations {

    private static final String
        KEYCLOAK_MGMT_HTTP_AUTHENTICATION = "keycloak-mgmt-http-authentication",
        RESOURCE = "resource",
        KEY_STORE_NAME = Ids.build(KEY_STORE, Random.name()),
        KEY_MANAGER_NAME = Ids.build(KEY_MANAGER, Random.name()),
        SERVER__SSL_CTX_NAME = Ids.build(SERVER_SSL_CONTEXT, Random.name());

    private static final Address HTTP_INTERFACE_ADDRESS = Address.coreService(MANAGEMENT).and(MANAGEMENT_INTERFACE, HTTP_INTERFACE);

    private final OnlineManagementClient client;
    private final Operations ops;
    private final Administration adminOps;

    public KeycloakWildflyOperations(OnlineManagementClient client) {
        this.client = client;
        this.ops = new Operations(client);
        this.adminOps = new Administration(client);
    }

    public KeycloakWildflyOperations secureManagementWithKeycloak(String keycloakRealmPublicKey) throws IOException, InterruptedException, TimeoutException {

        // Create a realm for both wildfly console and mgmt interface
        ops.add(Address.subsystem(KEYCLOAK).and(REALM, WILDFLY_INFRA), Values.of(AUTH_SERVER_URL,
                "http://localhost:8180/auth").and(REALM_PUBLIC_KEY, keycloakRealmPublicKey)).assertSuccess();

        // Create a secure-org.jboss.hal.testsuite.test.deployment in order to protect mgmt interface
        ops.add(Address.subsystem(KEYCLOAK).and("secure-org.jboss.hal.testsuite.test.deployment", WILDFLY_MANAGEMENT), Values.of(REALM, WILDFLY_INFRA)
                .and(RESOURCE, WILDFLY_MANAGEMENT).and("principal-attribute", "preferred_username").and("bearer-only", true)
                .and("ssl-required", "EXTERNAL")).assertSuccess();

        // Protect HTTP mgmt interface with Keycloak adapter
        ops.undefineAttribute(HTTP_INTERFACE_ADDRESS, SECURITY_REALM).assertSuccess();
        ModelNode mechanismConfigurationsListNode = new ModelNodePropertiesBuilder()
                .addProperty(MECHANISM_NAME, "KEYCLOAK")
                .addProperty(MECHANISM_REALM_CONFIGURATIONS,
                        new ModelNodeListBuilder(
                                new ModelNodePropertiesBuilder()
                                    .addProperty(REALM_NAME, "KeycloakOIDCRealm")
                                    .addProperty("realm-mapper", "keycloak-oidc-realm-mapper")
                                    .build())
                                .build())
                        .build();
        ops.add(Address.subsystem(ELYTRON).and(HTTP_AUTHENTICATION_FACTORY, KEYCLOAK_MGMT_HTTP_AUTHENTICATION),
                Values.of(SECURITY_DOMAIN, "KeycloakDomain").and("http-server-mechanism-factory", WILDFLY_MANAGEMENT)
                .andList(MECHANISM_CONFIGURATIONS, mechanismConfigurationsListNode)).assertSuccess();
        ops.writeAttribute(HTTP_INTERFACE_ADDRESS, HTTP_AUTHENTICATION_FACTORY, KEYCLOAK_MGMT_HTTP_AUTHENTICATION)
                .assertSuccess();
        ops.writeAttribute(HTTP_INTERFACE_ADDRESS, HTTP_UPGRADE,
                new ModelNodePropertiesBuilder()
                        .addProperty(ENABLED, new ModelNode(true))
                        .addProperty(SASL_AUTHENTICATION_FACTORY, "management-sasl-authentication")
                        .build()).assertSuccess();

        // Enable RBAC where roles are obtained from the identity
        ops.writeAttribute(Address.coreService(MANAGEMENT).and(ACCESS, AUTHORIZATION), PROVIDER, "rbac").assertSuccess();
        ops.writeAttribute(Address.coreService(MANAGEMENT).and(ACCESS, AUTHORIZATION), "use-identity-roles", true).assertSuccess();

        // Create a secure-server in order to publish the wildfly console configuration via mgmt interface
        ops.add(Address.subsystem(KEYCLOAK).and("secure-server", WILDFLY_CONSOLE), Values.of(REALM, WILDFLY_INFRA)
                .and(RESOURCE, WILDFLY_CONSOLE).and("public-client", true)).assertSuccess();

        adminOps.reloadIfRequired();
        return this;
    }

    public void enableSslForManagement() throws IOException, InterruptedException, TimeoutException {
        ops.add(Address.subsystem(ELYTRON).and(KEY_STORE, KEY_STORE_NAME), Values.of(PATH, "keycloak.jks")
                .and(RELATIVE_TO, "jboss.server.config.dir").andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, "secure"))
                .and(TYPE, "JKS")).assertSuccess();
        ops.add(Address.subsystem(ELYTRON).and(KEY_MANAGER, KEY_MANAGER_NAME), Values.of(KEY_STORE, KEY_STORE_NAME)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, "secure"))).assertSuccess();
        ops.add(Address.subsystem(ELYTRON).and(SERVER_SSL_CONTEXT, SERVER__SSL_CTX_NAME), Values.of(KEY_MANAGER, KEY_MANAGER_NAME))
                .assertSuccess();
        ops.batch(new Batch()
                .writeAttribute(HTTP_INTERFACE_ADDRESS, SSL_CONTEXT, SERVER__SSL_CTX_NAME)
                .writeAttribute(HTTP_INTERFACE_ADDRESS, SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS))
            .assertSuccess();
        setAuthServerUrl("https://localhost:8543/auth");
        ops.invoke(RELOAD, Address.root());
    }

    public void disableSslForManagement() throws IOException {
        ops.undefineAttribute(HTTP_INTERFACE_ADDRESS, SECURE_SOCKET_BINDING).assertSuccess();
        ops.undefineAttribute(HTTP_INTERFACE_ADDRESS, SSL_CONTEXT).assertSuccess();
        setAuthServerUrl("http://localhost:8180/auth");
        ops.invoke(RELOAD, Address.root());
    }

    private void setAuthServerUrl(String url) throws IOException {
        ops.writeAttribute(Address.subsystem(KEYCLOAK).and(REALM, WILDFLY_INFRA), AUTH_SERVER_URL, url).assertSuccess();
    }

    /**
     * Creates standalone {@link OnlineManagementClient} for comunication with SSL-secured management
     */
    public static OnlineManagementClient createSslClient(SslOptions sslOptions) throws IOException {
        return ManagementClient.onlineLazy(OnlineOptions.standalone().hostAndPort("localhost", 9993).ssl(sslOptions)
                .protocol(ManagementProtocol.HTTPS_REMOTING).build());
    }

    /**
     * Intended to be used as a validation that reload is finished.
     */
    public void waitUntilServerIsRunning() throws IOException, InterruptedException, TimeoutException {

        Thread.sleep(500);
        int timeoutInSeconds = 60;
        client.reconnect(timeoutInSeconds);

        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutInSeconds);
        while (System.currentTimeMillis() < endTime) {
            try {
                if (isServerRunning()) {
                    break;
                }
            } catch (Throwable ignored) {
                // server is probably down, will retry
            }

            Thread.sleep(200);
        }

        boolean running = false;
        try {
            running = isServerRunning();
        } catch (Throwable ignored) {
            // server probably down
        }
        if (!running) {
            throw new TimeoutException("Waiting for server timed out");
        }
    }

    private boolean isServerRunning() throws IOException {
        ModelNodeResult result = ops.readAttribute(Address.root(), Constants.SERVER_STATE);
        result.assertDefinedValue();
        String serverState = result.stringValue();
        return Constants.CONTROLLER_PROCESS_STATE_RUNNING.equals(serverState)
                || Constants.CONTROLLER_PROCESS_STATE_RELOAD_REQUIRED.equals(serverState)
                || Constants.CONTROLLER_PROCESS_STATE_RESTART_REQUIRED.equals(serverState);
    }
}
