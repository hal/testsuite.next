package org.jboss.hal.testsuite.test.configuration.elytron.ssl;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.RequiresLetsEncrypt;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.page.runtime.elytron.ElytronRuntimeSSLPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@Category(RequiresLetsEncrypt.class)
@RunWith(Arquillian.class)
public class CertificateAuthorityAccountTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String KEY_STORE = "key-store-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_CREATE = "certificate-authority-account-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE =
        "certificate-authority-account-deactivate-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE =
        "certificate-authority-account-update-" + Random.name();
    private static final String PATH = "path";

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
    }

    private static void createCertificateAuthorityAccount(String name) throws IOException {
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(name),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE).and("contact-urls", new ModelNodeGenerator.ModelNodeListBuilder()
            .addAll("mailto:example@org.com").build()));
    }

    private static void activateCertificateAuthorityAccount(String name) throws IOException {
        operations.invoke(CREATE_ACCOUNT, ElytronFixtures.certificateAuthorityAccountAddress(name),
            Values.of(AGREE_TO_TERMS_OF_SERVICE, true)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE));
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE));
            operations.removeIfExists(
                ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE));
            operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
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
    }

    @Test
    public void deactivate() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_DEACTIVATE);
        page.getCertificateAuthorityAccountTable().button("Deactivate").click();
        AddResourceDialogFragment addResourceDialog = console.addResourceDialog();
        addResourceDialog.add();
        console.verifySuccess();
    }

    @Test
    public void update() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Update").click();
        AddResourceDialogFragment resourceDialogFragment = console.addResourceDialog();
        resourceDialogFragment.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        resourceDialogFragment.add();
        console.verifySuccess();
    }

    @Test
    public void getMetadata() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Get Metadata").click();
        console.verifySuccess();
    }

    @Test
    public void changeAccountKey() throws InterruptedException {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        page.getCertificateAuthorityAccountTable().button("Change Account Key").click();
        AddResourceDialogFragment resourceDialogFragment = console.addResourceDialog();
        resourceDialogFragment.add();
        console.verifySuccess();
    }
}
