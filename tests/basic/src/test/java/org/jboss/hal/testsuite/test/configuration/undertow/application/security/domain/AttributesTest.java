package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
import org.jboss.hal.testsuite.test.configuration.undertow.ApplicationSecurityDomainFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.HTTP_AUTH_CREATE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.HTTP_AUTH_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.HTTP_SERVER_MECH_FACTORY;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.httpAuthenticationFactoryAddress;
import static org.jboss.hal.testsuite.test.configuration.undertow.ApplicationSecurityDomainFixtures.APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED;

@RunWith(Arquillian.class)
public class AttributesTest {

    @Page
    private UndertowApplicationSecurityDomainPage page;

    @Inject
    private CrudOperations crudOperations;

    private FormFragment attributesForm;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {

        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, "global").and(SECURITY_DOMAIN, "ApplicationDomain"));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, "global").and(SECURITY_DOMAIN, "ApplicationDomain"));
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, HTTP_AUTH_UPDATE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED));
        operations.removeIfExists(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
        operations.removeIfExists(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE));
    }

    @Before
    public void initPage() {
        page.navigate(NAME, APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED);
        attributesForm = page.getAttributesForm();
    }

    @Test
    public void toggleEnableJACC() throws Exception {
        boolean enableJACC = operations.readAttribute(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            ApplicationSecurityDomainFixtures.ENABLE_JACC).booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            attributesForm,
            ApplicationSecurityDomainFixtures.ENABLE_JACC, !enableJACC);
    }

    @Test
    public void updateHttpAuthenticationFactory() throws Exception {
        crudOperations.update(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            attributesForm,
            ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, HTTP_AUTH_CREATE);
    }

    @Test
    public void toggleOverrideDeploymentConfig() throws Exception {
        boolean overrideDeploymentConfig = operations.readAttribute(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            ApplicationSecurityDomainFixtures.OVERRIDE_DEPLOYMENT_CONFIG).booleanValue();
        crudOperations.update(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            attributesForm,
            ApplicationSecurityDomainFixtures.OVERRIDE_DEPLOYMENT_CONFIG, !overrideDeploymentConfig);
    }


}
