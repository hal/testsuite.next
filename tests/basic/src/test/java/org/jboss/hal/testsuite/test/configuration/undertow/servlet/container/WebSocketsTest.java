package org.jboss.hal.testsuite.test.configuration.undertow.servlet.container;

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
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.jboss.hal.testsuite.test.configuration.io.IOFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class WebSocketsTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowServletContainerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String SERVLET_CONTAINER_EDIT =
        "servlet-container-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_WEB_SOCKETS_CREATE =
        "servlet-container-without-websockets-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_WEB_SOCKETS_REMOVE =
        "servlet-container-with-websockets-" + RandomStringUtils.randomAlphanumeric(7);

    private static final Address SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS = webSocketsAddress(SERVLET_CONTAINER_EDIT);

    private static final String BUFFER_POOL_TO_BE_EDITED =
        "buffer-pool-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String WORKER_TO_BE_EDITED = "worker-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static Address webSocketsAddress(String servletContainer) {
        return UndertowFixtures.servletContainerAddress(servletContainer).and("setting", "websockets");
    }

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.add(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS);
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_WEB_SOCKETS_CREATE));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_WEB_SOCKETS_REMOVE));
        operations.add(webSocketsAddress(SERVLET_CONTAINER_WEB_SOCKETS_REMOVE));
        operations.add(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.add(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_WEB_SOCKETS_CREATE));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_WEB_SOCKETS_REMOVE));
        operations.removeIfExists(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.removeIfExists(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
    }

    @Test
    public void create() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_WEB_SOCKETS_CREATE);
        crudOperations.createSingleton(webSocketsAddress(SERVLET_CONTAINER_WEB_SOCKETS_CREATE), page.getWebSocketsForm());
    }

    private void navigateToWebSocketsForm(String servletContainerName) {
        page.navigate("name", servletContainerName);
        console.verticalNavigation()
            .selectPrimary(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_WEBSOCKET, "item"));
    }

    @Test
    public void remove() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_WEB_SOCKETS_REMOVE);
        crudOperations.deleteSingleton(webSocketsAddress(SERVLET_CONTAINER_WEB_SOCKETS_REMOVE), page.getWebSocketsForm());
    }

    @Test
    public void editBufferPool() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, page.getWebSocketsForm(), "buffer-pool",
            BUFFER_POOL_TO_BE_EDITED);
    }

    @Test
    public void editDeflaterLevel() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, page.getWebSocketsForm(), "deflater-level",
            Random.number(0, 10));
    }

    @Test
    public void toggleDispatchToWorker() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_EDIT);
        boolean dispatchToWorker =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, "dispatch-to-worker").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, page.getWebSocketsForm(), "dispatch-to-worker",
            !dispatchToWorker);
    }

    @Test
    public void togglePerMessageDeflate() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_EDIT);
        boolean perMessageDeflate =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, "per-message-deflate").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, page.getWebSocketsForm(), "per-message-deflate",
            !perMessageDeflate);
    }

    @Test
    public void editWorker() throws Exception {
        navigateToWebSocketsForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_WEB_SOCKETS_ADDRESS, page.getWebSocketsForm(), "worker",
           WORKER_TO_BE_EDITED);
    }
}
