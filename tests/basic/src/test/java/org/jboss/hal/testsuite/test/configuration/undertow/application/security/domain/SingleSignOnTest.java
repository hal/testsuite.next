package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.ApplicationSecurityDomainFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class SingleSignOnTest {
    @Inject
    private Console console;

    @Page
    private UndertowApplicationSecurityDomainPage page;

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    private static final String APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED =
        "application-security-domain-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON =
        "application-security-with-single-sign-on-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String KEY_STORE_TO_BE_ADDED =
        "key_store_to_be_added_" + RandomStringUtils.randomAlphanumeric(7);
    private static final String KEY_STORE_TO_BE_EDITED =
        "key_store_to_be_edited_" + RandomStringUtils.randomAlphanumeric(7);
    private static final String CLIENT_SSL_CONTEXT_TO_BE_ADDED =
        "client-ssl-context-" + RandomStringUtils.randomAlphanumeric(7);
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "application-http-authentication"));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED),
            Values.of("type", ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_EDITED),
            Values.of("type", ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "application-http-authentication"));
        operations.add(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            Values.of(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS, Random.name())
                .and(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE, KEY_STORE_TO_BE_ADDED)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_ADDED));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED));
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED));
        operations.removeIfExists(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_ADDED));
    }

    @Test
    public void create() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED);
        String keyAlias = Random.name();
        String clearText = Random.name();
        crudOperations.createSingleton(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            page.getSingleSignOnForm(), formFragment -> {
                formFragment.text(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS, keyAlias);
                formFragment.text(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE, KEY_STORE_TO_BE_ADDED);
                formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, clearText);
            }, resourceVerifier -> {
                resourceVerifier.verifyAttribute(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS, keyAlias);
                resourceVerifier.verifyAttribute(ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE,
                    KEY_STORE_TO_BE_ADDED);
                resourceVerifier.verifyAttribute(
                    CredentialReference.fqName(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT), clearText);
            });
    }

    @Test
    public void toggleHttpOnly() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        boolean httpOnly = operations.readAttribute(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            "http-only").booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "http-only", !httpOnly);
    }

    @Test
    public void toggleSecure() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        boolean secure = operations.readAttribute(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            "secure").booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "secure", !secure);
    }

    @Test
    public void updateDomain() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "domain");
    }

    @Test
    public void updateCookieName() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "cookie-name");
    }

    @Test
    public void updatePath() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "path");
    }

    @Test
    public void updateKeyAlias() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS);
    }

    @Test
    public void updateKeyStore() throws Exception {
        page.navigate(ModelDescriptionConstants.NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE,
            KEY_STORE_TO_BE_EDITED);
    }

    @Test
    public void updateClientSslContext() throws Exception {
        page.navigate("name", APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "client-ssl-context",
            CLIENT_SSL_CONTEXT_TO_BE_ADDED);
    }
}
