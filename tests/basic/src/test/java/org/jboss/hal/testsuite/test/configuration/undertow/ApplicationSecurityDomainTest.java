package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowApplicationSecurityDomainPage;
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
public class ApplicationSecurityDomainTest {

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
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED),
            Values.of("http-authentication-factory", "application-http-authentication"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED));
    }

    @Before
    public void initPage() {
        page.navigate("name", APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED);
    }

    @Test
    public void something() {
        System.out.println("Hello");
    }
}
