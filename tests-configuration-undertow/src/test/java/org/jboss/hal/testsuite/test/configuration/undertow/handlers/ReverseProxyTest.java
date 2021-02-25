package org.jboss.hal.testsuite.test.configuration.undertow.handlers;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowHandlersFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowHandlersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ReverseProxyTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowHandlersPage page;

    private static final String REVERSE_PROXY_CREATE =
        "reverse-proxy-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String REVERSE_PROXY_DELETE =
        "reverse-proxy-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_DELETE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_CREATE));
        operations.removeIfExists(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-reverse-proxy", "item"));
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_CREATE),
            page.getReverseProxyTable(), REVERSE_PROXY_CREATE);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_DELETE),
            page.getReverseProxyTable(), REVERSE_PROXY_DELETE);
    }
}
