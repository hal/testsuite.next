package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.ApplicationSecurityDomainFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.HTTP_AUTH_CREATE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.HTTP_SERVER_MECH_FACTORY;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.httpAuthenticationFactoryAddress;
import static org.jboss.hal.testsuite.test.configuration.undertow.ApplicationSecurityDomainFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SingleSignOnTest {

    @Page
    private UndertowApplicationSecurityDomainPage page;

    @Inject
    private CrudOperations crudOperations;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, "global").and(SECURITY_DOMAIN, "ApplicationDomain"));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED),
            Values.of(TYPE, ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_EDITED),
            Values.of(TYPE, ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
        operations.add(applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, HTTP_AUTH_CREATE));
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
        operations.removeIfExists(applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_EDITED));
        operations.removeIfExists(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_ADDED));
        operations.removeIfExists(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
    }

    @Before
    public void initPage() {
        page.navigate(NAME, APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON);
    }


    @Test
    public void toggleHttpOnly() throws Exception {
        boolean httpOnly = operations.readAttribute(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            "http-only").booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "http-only", !httpOnly);
    }

    @Test
    public void toggleSecure() throws Exception {
        boolean secure = operations.readAttribute(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            "secure").booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "secure", !secure);
    }

    @Test
    public void updateDomain() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "domain");
    }

    @Test
    public void updateCookieName() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "cookie-name");
    }

    @Test
    public void updatePath() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), PATH);
    }

    @Test
    public void updateKeyAlias() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_ALIAS);
    }

    @Test
    public void updateKeyStore() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), ApplicationSecurityDomainFixtures.SINGLE_SIGN_ON_KEY_STORE,
            KEY_STORE_TO_BE_EDITED);
    }

    @Test
    public void updateClientSslContext() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON),
            page.getSingleSignOnForm(), "client-ssl-context",
            CLIENT_SSL_CONTEXT_TO_BE_ADDED);
    }
}
