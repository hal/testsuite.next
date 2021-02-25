package org.jboss.hal.testsuite.test.configuration.undertow.server.hosts;

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
import org.jboss.hal.testsuite.fixtures.IOFixtures;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
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
public class AccessLogTest {
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

    private static final Address UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS =
        UndertowFixtures.serverAddress(UndertowFixtures.DEFAULT_SERVER)
            .and("host", UndertowFixtures.DEFAULT_HOST)
            .and("setting", "access-log");

    private static final String WORKER_TO_BE_CREATED = "worker-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(IOFixtures.workerAddress(WORKER_TO_BE_CREATED));
        operations.add(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS);
        operations.removeIfExists(IOFixtures.workerAddress(WORKER_TO_BE_CREATED));
    }

    @Before
    public void initPage() {
        page.navigate("name", UndertowFixtures.DEFAULT_SERVER);
        console.verticalNavigation().selectPrimary(Ids.UNDERTOW_HOST_ITEM);
        page.getHostsTable().select(UndertowFixtures.DEFAULT_HOST);
    }

    @Test
    public void updateDirectory() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "directory");
    }

    @Test
    public void toggleExtended() throws Exception {
        boolean extended =
            operations.readAttribute(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, "extended").booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "extended",
            !extended);
    }

    @Test
    public void updatePattern() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "pattern");
    }

    @Test
    public void updatePrefix() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "prefix");
    }

    @Test
    public void updateRelativeTo() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "relative-to");
    }

    @Test
    public void toggleRotate() throws Exception {
        boolean rotate =
            operations.readAttribute(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, "rotate").booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "rotate",
            !rotate);
    }

    @Test
    public void updateSuffix() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "suffix");
    }

    @Test
    public void toggleUseServerLog() throws Exception {
        boolean useServerLog =
            operations.readAttribute(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, "use-server-log").booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "use-server-log",
            !useServerLog);
    }

    @Test
    public void updateWorker() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ACCESS_LOG_ADDRESS, page.getHostsAccessLogForm(), "worker",
            WORKER_TO_BE_CREATED);
    }
}
