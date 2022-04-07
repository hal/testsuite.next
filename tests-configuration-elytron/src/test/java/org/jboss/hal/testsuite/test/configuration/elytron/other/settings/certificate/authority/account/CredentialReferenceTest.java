package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.certificate.authority.account;

import java.io.IOException;
import java.util.function.Consumer;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.test.configuration.elytron.other.settings.AbstractOtherSettingsTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class CredentialReferenceTest extends AbstractOtherSettingsTest {

    private static final String KEY_STORE = "key-store-with-path-" + Random.name();

    private static final String
        CERTIFICATE_AUTHORITY_ACCOUNT_WITH_CREDENTIAL_REFERENCE =
        "certificate-authority-account-with-credential-reference" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_WITHOUT_CREDENTIAL_REFERENCE =
        "certificate-authority-account-without-credential-reference-" + Random.name();
    private static final String
        CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT =
        "certificate-authority-account-with-credential-reference-to-be-edited-" + Random.name();

    @BeforeClass
    public static void initResources() throws IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get("clear-text").set(Random.name());
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE), Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_TYPE, "JKS")
            .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
            .and("path", Random.name())
            .and("relative-to", "jboss.server.config.dir"));
        operations.add(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_WITH_CREDENTIAL_REFERENCE),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator().createObjectNodeWithPropertyChild(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()))).assertSuccess();
        operations.add(ElytronFixtures.certificateAuthorityAccountAddress(
            CERTIFICATE_AUTHORITY_ACCOUNT_WITHOUT_CREDENTIAL_REFERENCE),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name()).and(ModelDescriptionConstants.KEY_STORE, KEY_STORE)).assertSuccess();
        operations.add(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT),
            Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator().createObjectNodeWithPropertyChild(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()))).assertSuccess();
    }

    @AfterClass
    public static void cleanUp() throws IOException, OperationException {
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_WITH_CREDENTIAL_REFERENCE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(
                CERTIFICATE_AUTHORITY_ACCOUNT_WITHOUT_CREDENTIAL_REFERENCE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
    }

    @Before
    public void navigateToCertificateAuthorityAccount() {
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.OTHER_ITEM, ElytronFixtures.CERTIFICATE_AUTHORITY_ACCOUNT_ITEM);
    }

    @Test
    public void create() throws Exception {
        String clearTextValue = Random.name();
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_WITHOUT_CREDENTIAL_REFERENCE);
        crud.createSingleton(ElytronFixtures.certificateAuthorityAccountAddress(
            CERTIFICATE_AUTHORITY_ACCOUNT_WITHOUT_CREDENTIAL_REFERENCE),
            page.getCertificateAuthorityAccountCredentialReferenceForm(), formFragment -> formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, clearTextValue),
            resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
    }

    @Test
    public void remove() throws Exception {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_WITH_CREDENTIAL_REFERENCE);
        crud.deleteSingleton(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_WITH_CREDENTIAL_REFERENCE),
            page.getCertificateAuthorityAccountCredentialReferenceForm(),
            resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(ElytronFixtures.CREDENTIAL_REFERENCE));
    }

    @Test
    public void editAlias() throws Exception {
        String alias = Random.name();
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT);
        crud.update(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT),
            page.getCertificateAuthorityAccountCredentialReferenceForm(), clearFields.andThen(formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, alias);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, ElytronFixtures.CRED_ST_UPDATE);
            }), resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.alias", alias));
    }

    private Consumer<FormFragment> clearFields = formFragment -> {
        formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, "");
        formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, "");
        formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, "");
        formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_TYPE, "");
    };

    @Test
    public void editClearText() throws Exception {
        String clearTextValue = Random.name();
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT);
        crud.update(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT),
            page.getCertificateAuthorityAccountCredentialReferenceForm(),
            clearFields.andThen(formFragment -> formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, clearTextValue)),
            resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
    }

    @Test
    public void editStore() throws Exception {
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT);
        crud.update(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT),
            page.getCertificateAuthorityAccountCredentialReferenceForm(), clearFields.andThen(formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name());
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, ElytronFixtures.CRED_ST_DELETE);
            }), resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.store",
                ElytronFixtures.CRED_ST_DELETE));
    }

    @Test
    public void editType() throws Exception {
        String type = Random.name();
        page.getCertificateAuthorityAccountTable().select(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT);
        crud.update(
            ElytronFixtures.certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_CREDENTIAL_REFERENCE_EDIT),
            page.getCertificateAuthorityAccountCredentialReferenceForm(), clearFields.andThen(formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name());
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, ElytronFixtures.CRED_ST_UPDATE);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_TYPE, type);
            }), resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.type", type));
    }
}
