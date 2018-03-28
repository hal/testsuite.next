package org.jboss.hal.testsuite.test.configuration.undertow.application.security.domain;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
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
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class AttributesTest {

    @Inject
    private Console console;

    @Page
    private UndertowApplicationSecurityDomainPage page;

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    private FormFragment attributesForm;

    private static final String APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED =
        "application-security-domain-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "application-http-authentication"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED));
    }

    @Before
    public void initPage() {
        page.navigate("name", APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED);
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
            ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "management-http-authentication");
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
