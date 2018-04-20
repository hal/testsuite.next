package org.jboss.hal.testsuite.test.configuration.undertow.server.hosts;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class SingleSignOnTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowServerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final Address UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS =
        UndertowFixtures.serverAddress(UndertowFixtures.DEFAULT_SERVER)
            .and("host", UndertowFixtures.DEFAULT_HOST)
            .and("setting", "single-sign-on");

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS);
    }

    @Before
    public void initPage() {
        page.navigate("name", UndertowFixtures.DEFAULT_SERVER);
        console.verticalNavigation().selectPrimary(Ids.UNDERTOW_HOST_ITEM);
        page.getHostsTable().select(UndertowFixtures.DEFAULT_HOST);
    }

    @Test
    public void updateCookieName() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, page.getHostsSingleSignOnForm(),
            "cookie-name");
    }

    @Test
    public void updateDomain() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, page.getHostsSingleSignOnForm(), "domain");
    }

    @Test
    public void toggleHttpOnly() throws Exception {
        boolean httpOnly = operations.readAttribute(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, "http-only")
            .booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, page.getHostsSingleSignOnForm(), "http-only",
            !httpOnly);
    }

    @Test
    public void updatePath() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, page.getHostsSingleSignOnForm(), "path");
    }

    @Test
    public void toggleSecure() throws Exception {
        boolean secure = operations.readAttribute(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, "secure")
            .booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_SINGLE_SIGN_ON_ADDRESS, page.getHostsSingleSignOnForm(), "secure",
            !secure);
    }
}
