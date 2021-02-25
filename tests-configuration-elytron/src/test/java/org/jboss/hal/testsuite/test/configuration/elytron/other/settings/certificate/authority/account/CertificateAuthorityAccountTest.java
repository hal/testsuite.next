package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.certificate.authority.account;

import java.io.IOException;
import java.util.Arrays;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.elytron.other.settings.AbstractOtherSettingsTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class CertificateAuthorityAccountTest extends AbstractOtherSettingsTest {

    private static final String KEY_STORE = "key-store-with-path-" + Random.name();

    private static final String KEY_STORE_UPDATE = "another-key-store-with-path-" + Random.name();

    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_CREATE =
        "certificate-authority-account-to-be-created-" + Random.name();

    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE =
        "certificate-authority-account-to-be-updated-" + Random.name();

    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_DELETE =
        "certificate-authority-account-to-be-deleted-" + Random.name();

    @BeforeClass
    public static void initResources() throws IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get("clear-text").set(Random.name());
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE), Values.of("type", "JKS")
            .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
            .and("path", Random.name())
            .and("relative-to", "jboss.server.config.dir"));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_UPDATE), Values.of("type", "JKS")
            .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
            .and("path", Random.name())
            .and("relative-to", "jboss.server.config.dir"));
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name()).and(ModelDescriptionConstants.KEY_STORE, KEY_STORE));
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DELETE),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name()).and(ModelDescriptionConstants.KEY_STORE, KEY_STORE));
    }

    @AfterClass
    public static void cleanUp() throws IOException, OperationException {
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DELETE));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_UPDATE));
    }

    @Before
    public void navigateToCertificateAuthorityAccount() {
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.OTHER_ITEM, ElytronFixtures.CERTIFICATE_AUTHORITY_ACCOUNT_ITEM);
    }

    @Test
    public void create() throws Exception {
        crud.create(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREATE),
            page.getCertificateAuthorityAccountTable(), formFragment -> {
                formFragment.text("name", CERTIFICATE_AUTHORITY_ACCOUNT_CREATE);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name());
                formFragment.text(ModelDescriptionConstants.KEY_STORE, KEY_STORE);
            });
    }

    @Test
    public void delete() throws Exception {
        crud.delete(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_DELETE),
            page.getCertificateAuthorityAccountTable(), CERTIFICATE_AUTHORITY_ACCOUNT_DELETE);
    }

    @Test
    public void editAlias() throws Exception {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        crud.update(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE),
            page.getCertificateAuthorityAccountForm(), ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS);
    }

    @Test
    public void editContactUrls() throws Exception {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        crud.update(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE),
            page.getCertificateAuthorityAccountForm(), "contact-urls",
            Arrays.asList(Random.name(), Random.name(), Random.name()));
    }

    @Test
    public void editKeyStore() throws Exception {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        crud.update(ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE),
            page.getCertificateAuthorityAccountForm(), ModelDescriptionConstants.KEY_STORE, KEY_STORE_UPDATE);
    }
}
