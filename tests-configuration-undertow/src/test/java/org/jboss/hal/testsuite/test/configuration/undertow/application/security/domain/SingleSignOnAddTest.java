package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fixtures.undertow.ApplicationSecurityDomainFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
import org.junit.AfterClass;
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
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_SERVER_MECH_FACTORY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.httpAuthenticationFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.undertow.ApplicationSecurityDomainFixtures.APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2;
import static org.jboss.hal.testsuite.fixtures.undertow.ApplicationSecurityDomainFixtures.KEY_STORE_TO_BE_ADDED;
import static org.jboss.hal.testsuite.fixtures.undertow.ApplicationSecurityDomainFixtures.applicationSecurityDomain;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SingleSignOnAddTest {

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
        operations.add(applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, HTTP_AUTH_CREATE));
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED),
            Values.of(TYPE, ElytronFixtures.JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, Random.name()).build()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2));
        operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE_TO_BE_ADDED));
        operations.removeIfExists(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
    }

    @Test
    public void create() throws Exception {
        page.navigate(NAME, APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2);
        String keyAlias = Random.name();
        String clearText = Random.name();
        crudOperations.createSingleton(
            ApplicationSecurityDomainFixtures.singleSignOnAddress(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2),
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
}
