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
import org.jboss.hal.testsuite.Random;
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
public class ReverseProxyAttributesTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowHandlersPage page;

    private static final String REVERSE_PROXY_EDIT =
        "reverse-proxy-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-reverse-proxy", "item"));
        page.getReverseProxyTable().select(REVERSE_PROXY_EDIT);
    }

    @Test
    public void editCachedConnectionsPerThread() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "cached-connections-per-thread",
            Random.number());
    }

    @Test
    public void editConnectionIdleTimeout() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "connection-idle-timeout",
            Random.number());
    }

    @Test
    public void editConnectionsPerThread() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "connections-per-thread",
            Random.number());
    }

    @Test
    public void editMaxRequestTime() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "max-request-time",
            Random.number());
    }

    @Test
    public void editMaxRetries() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "max-retries",
            Random.number());
    }

    @Test
    public void editProblemServerRetry() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "problem-server-retry",
            Random.number());
    }

    @Test
    public void editRequestQueueSize() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "request-queue-size",
            Random.number());
    }

    @Test
    public void editSessionCookieNames() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.reverseProxyAddress(REVERSE_PROXY_EDIT),
            page.getReverseProxyForm(), "session-cookie-names");
    }
}
