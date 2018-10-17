package org.jboss.hal.testsuite.test.runtime.elytron;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.util.TestsuiteEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class CertificateAuthorityAccountTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Address AUDIT_ADDRESS = Address.coreService("management").and("access", "audit");
    private static final Address AUDIT_LOG_ADDRESS = AUDIT_ADDRESS.and("logger", "audit-log");
    private static final Address JSON_FORMATTER_ADDRESS = AUDIT_ADDRESS.and("json-formatter", "json-formatter");
    private static final Address FILE_HANDLER_ADDRESS = AUDIT_ADDRESS.and("file-handler", "file");
    private static final BackupAndRestoreAttributes backup =
        new BackupAndRestoreAttributes.Builder(AUDIT_ADDRESS).build();

    private static final String JBOSS_HOME_DIR = "jboss.home.dir";
    private static final String KEY_STORE = "key-store-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT = "certificate-authority-account-" + Random.name();
    private static final String PATH = "path";

    private static File auditLogFile;

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        createCertificateAuthorityAccount();
        String serverHome = operations.readAttribute(Address.root().and(PATH, JBOSS_HOME_DIR), PATH).stringValue();
        auditLogFile = new File(serverHome, "audit-log.log");
        TestsuiteEnvironmentUtils.getJbossHome();
        client.apply(backup.backup());
        Batch batch = new Batch();
        batch.writeAttribute(FILE_HANDLER_ADDRESS, "relative-to", JBOSS_HOME_DIR);
        batch.writeAttribute(FILE_HANDLER_ADDRESS, PATH, auditLogFile.getName());
        operations.batch(batch);
        operations.writeAttribute(JSON_FORMATTER_ADDRESS, "include-date", false);
        operations.writeAttribute(AUDIT_LOG_ADDRESS, "enabled", true);
    }

    private static void createCertificateAuthorityAccount() throws IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get("clear-text").set(Random.name());
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE), Values.of("type", "JKS")
            .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
            .and(PATH, Random.name())
            .and("relative-to", "jboss.server.config.dir"));
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE));
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException, OperationException {
        try {
            removeCertificateAuthorityAccount();
            client.apply(backup.restore());
        } finally {
            client.close();
        }
    }

    private static void removeCertificateAuthorityAccount() throws IOException, OperationException {
        operations.removeIfExists(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Test
    public void test() {
        Assert.assertTrue(true);
        browser.navigate();
        Assert.assertTrue(true);
    }
}
