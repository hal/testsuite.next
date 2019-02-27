package org.jboss.hal.testsuite.test.runtime.elytron.ssl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.page.runtime.elytron.ElytronRuntimeSSLPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.util.TestsuiteEnvironmentUtils;
import org.jboss.hal.testsuite.util.audit.log.AuditLog;
import org.jboss.hal.testsuite.util.audit.log.AuditLogWatcher;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

// temporarily disable tests as the pebble server is not ready to run in CI server.
@Ignore
@RunWith(Arquillian.class)
public class CertificateAuthorityAccountTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Address AUDIT_ADDRESS = Address.coreService("management").and("access", "audit");
    private static final Address AUDIT_LOG_ADDRESS = AUDIT_ADDRESS.and("logger", "audit-log");
    private static final Address JSON_FORMATTER_ADDRESS = AUDIT_ADDRESS.and("json-formatter", "json-formatter");
    private static final Address FILE_HANDLER_ADDRESS = AUDIT_ADDRESS.and("file-handler", "file");
    private static final BackupAndRestoreAttributes auditLogAttributesBackup =
        new BackupAndRestoreAttributes.Builder(AUDIT_ADDRESS).build();

    private static final String JBOSS_HOME_DIR = "jboss.home.dir";
    private static final String KEY_STORE = "key-store-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_CREATE = "certificate-authority-account-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE =
        "certificate-authority-account-deactivate-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE =
        "certificate-authority-account-update-" + Random.name();
    private static final String PATH = "path";

    private static File auditLogFile = new File(TestsuiteEnvironmentUtils.getJbossHome(), "audit-log.log");
    private static final BlockingQueue<AuditLog> auditLogQueue = new SynchronousQueue<>();
    private static final AuditLogWatcher auditLogWatcher = new AuditLogWatcher(auditLogFile.toPath(), auditLogQueue);

    private static final String CREATE_ACCOUNT = "create-account";
    private static final String UPDATE = "update-account";
    private static final String AGREE_TO_TERMS_OF_SERVICE = "agree-to-terms-of-service";
    private static final String STAGING = "staging";

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get("clear-text").set(Random.name());
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE), Values.of("type", "JKS")
            .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
            .and(PATH, Random.name())
            .and("relative-to", "jboss.server.config.dir"));
        createCertificateAuthorityAccount(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE);
        createCertificateAuthorityAccount(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE);
        createCertificateAuthorityAccount(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        activateCertificateAuthorityAccount(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE);
        activateCertificateAuthorityAccount(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        client.apply(auditLogAttributesBackup.backup());
        Batch batch = new Batch();
        batch.writeAttribute(FILE_HANDLER_ADDRESS, "relative-to", JBOSS_HOME_DIR);
        batch.writeAttribute(FILE_HANDLER_ADDRESS, PATH, auditLogFile.getName());
        operations.batch(batch);
        operations.writeAttribute(JSON_FORMATTER_ADDRESS, "include-date", false);
        operations.writeAttribute(AUDIT_LOG_ADDRESS, "enabled", true);
        new Thread(auditLogWatcher).start();
    }

    private static void createCertificateAuthorityAccount(String name) throws IOException {
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(name),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE));
    }

    private static void activateCertificateAuthorityAccount(String name) throws IOException {
        operations.invoke(CREATE_ACCOUNT, ElytronFixtures.certificateAuthorityAccountAddress(name),
            Values.of(AGREE_TO_TERMS_OF_SERVICE, true)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException, OperationException {
        try {
            auditLogWatcher.stop();
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE));
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE));
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE));
            operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
            client.apply(auditLogAttributesBackup.restore());
            Files.delete(auditLogFile.toPath());
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private ElytronRuntimeSSLPage page;

    private AuditLog auditLog;

    @Before
    public void initPage() throws IOException {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.ELYTRON_CERTIFICATE_AUTHORITY_ACCOUNT);
    }

    @Test
    public void create() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE);
        page.getCertificateAuthorityAccountTable().button("Create").click();
        AddResourceDialogFragment addResourceDialog = console.addResourceDialog();
        addResourceDialog.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        addResourceDialog.getForm().flip(STAGING, true);
        addResourceDialog.add();
        console.verifySuccess();
        retrieveUpdatedAuditLog();
        AuditLog.Operation lastOperation = getLastOperation();
        Assert.assertTrue("The \"create-account\" operation should be successful",
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).isSuccess());
        Assert.assertEquals("The \"create-account\" operation should be called", lastOperation.getOperationName(),
            "create-account");
        Assert.assertTrue(lastOperation.getAddress().toString()
            .equals(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE).toString()));
    }

    private void retrieveUpdatedAuditLog() throws InterruptedException {
        auditLog = auditLogQueue.poll(5, TimeUnit.SECONDS);
    }

    private AuditLog.Operation getLastOperation() {
        List<AuditLog.Operation> lastOperations =
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).getOperations();
        return lastOperations.get(lastOperations.size() - 1);
    }

    @Test
    public void deactivate() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE);
        page.getCertificateAuthorityAccountTable().button("Deactivate").click();
        AddResourceDialogFragment addResourceDialog = console.addResourceDialog();
        addResourceDialog.add();
        console.verifySuccess();
        retrieveUpdatedAuditLog();
        AuditLog.Operation lastOperation = getLastOperation();
        Assert.assertTrue("The \"deactivate-account\" operation should be called succesfully",
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).isSuccess());
        Assert.assertEquals("The \"deactivate-account\" operation should be called", "deactivate-account", lastOperation.getOperationName());
        Assert.assertEquals(lastOperation.getAddress().toString(),
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE).toString());
    }

    @Test
    public void update() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Update").click();
        AddResourceDialogFragment resourceDialogFragment = console.addResourceDialog();
        resourceDialogFragment.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        resourceDialogFragment.add();
        console.verifySuccess();
        retrieveUpdatedAuditLog();
        AuditLog.Operation lastOperation = getLastOperation();
        Assert.assertTrue("The \"update\" operation should be successful",
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).isSuccess());
        Assert.assertEquals("The \"update\" operation should be called", UPDATE, lastOperation.getOperationName());
        Assert.assertTrue(lastOperation.getAddress().toString()
            .equals(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE).toString()));
    }

    @Test
    public void getMetadata() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Get Metadata").click();
        console.verifySuccess();
        retrieveUpdatedAuditLog();
        AuditLog.Operation lastOperation = getLastOperation();
        Assert.assertTrue("The \"get-metadata\" operation should be successful",
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).isSuccess());
        Assert.assertEquals("The \"get-metadata\" operation should be called", "get-metadata", lastOperation.getOperationName());
        Assert.assertTrue(lastOperation.getAddress().toString()
            .equals(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE).toString()));
    }

    @Test
    public void changeAccountKey() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Change Account Key").click();
        AddResourceDialogFragment resourceDialogFragment = console.addResourceDialog();
        resourceDialogFragment.add();
        console.verifySuccess();
        retrieveUpdatedAuditLog();
        AuditLog.Operation lastOperation = getLastOperation();
        Assert.assertTrue("The \"change-account-key\" operation should be successful",
            auditLog.getLogEntries().get(auditLog.getLogEntries().size() - 1).isSuccess());
        Assert.assertEquals("The \"change-account-key\" operation should be called", "change-account-key",lastOperation.getOperationName());
        Assert.assertTrue(lastOperation.getAddress().toString()
            .equals(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE).toString()));
    }
}
