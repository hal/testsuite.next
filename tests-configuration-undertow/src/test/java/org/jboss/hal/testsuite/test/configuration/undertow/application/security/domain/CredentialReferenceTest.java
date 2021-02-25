package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fixtures.undertow.ApplicationSecurityDomainFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_SERVER_MECH_FACTORY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.httpAuthenticationFactoryAddress;

@RunWith(Arquillian.class)
public class CredentialReferenceTest {

    @Page
    private UndertowApplicationSecurityDomainPage page;

    @Inject
    private CrudOperations crudOperations;

    private static final String APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON =
        "application-security-with-single-sign-on-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String KEY_STORE_TO_BE_ADDED =
        "key_store_to_be_added_" + RandomStringUtils.randomAlphanumeric(7);

    private static final String LOCATION_SUFFIX = "_location";

    private static final String CREDENTIAL_STORE_TO_BE_ADDED =
        "credential_store_to_be_added_" + RandomStringUtils.randomAlphanumeric(7);

    private static final String CREDENTIAL_STORE_TO_BE_EDITED =
        "credential_store_to_be_edited_" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED),
            Values.of("type", ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(ElytronFixtures.credentialStoreAddress(CREDENTIAL_STORE_TO_BE_ADDED),
            Values.of(ElytronFixtures.CREDENTIAL_STORE_CREATE, true)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build())
                .and(LOCATION, CREDENTIAL_STORE_TO_BE_ADDED + LOCATION_SUFFIX));
        operations.add(ElytronFixtures.credentialStoreAddress(CREDENTIAL_STORE_TO_BE_EDITED),
            Values.of(ElytronFixtures.CREDENTIAL_STORE_CREATE, true)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build())
                .and(LOCATION, CREDENTIAL_STORE_TO_BE_EDITED + LOCATION_SUFFIX));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, "global").and(SECURITY_DOMAIN, "ApplicationDomain"));
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, HTTP_AUTH_CREATE));
        operations.add(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            Values.of(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS, Random.name())
                .and(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE, KEY_STORE_TO_BE_ADDED)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED));
        operations.removeIfExists(ElytronFixtures.credentialStoreAddress(CREDENTIAL_STORE_TO_BE_ADDED));
        operations.removeIfExists(ElytronFixtures.credentialStoreAddress(CREDENTIAL_STORE_TO_BE_EDITED));
        operations.removeIfExists(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
    }

    @Before
    public void initPage() {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
    }

    @Test
    public void updateAlias() throws Exception {
        String alias = Random.name();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getCredentialReferenceForm(),
            formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, alias);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, "");
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, CREDENTIAL_STORE_TO_BE_ADDED);
            },
            resourceVerifier -> resourceVerifier.verifyAttribute(
                CredentialReference.fqName(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS), alias));
    }

    @Test
    public void updateStore() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getCredentialReferenceForm(),
            formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name());
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, "");
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, CREDENTIAL_STORE_TO_BE_EDITED);
            },
            resourceVerifier -> resourceVerifier.verifyAttribute(
                CredentialReference.fqName(ElytronFixtures.CREDENTIAL_REFERENCE_STORE),
                CREDENTIAL_STORE_TO_BE_EDITED));
    }

    @Test
    public void updateClearText() throws Exception {
        String clearText = Random.name();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getCredentialReferenceForm(),
            formFragment -> {
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, "");
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, clearText);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_STORE, "");
            },
            resourceVerifier -> resourceVerifier.verifyAttribute(
                CredentialReference.fqName(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT), clearText));
    }

    @Test
    public void updateType() throws Exception {
        String type = Random.name();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getCredentialReferenceForm(),
            formFragment -> formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_TYPE, type),
            resourceVerifier -> resourceVerifier.verifyAttribute(
                CredentialReference.fqName(ElytronFixtures.CREDENTIAL_REFERENCE_TYPE), type));
    }
}
