package org.jboss.hal.testsuite.test.configuration.undertow.server.listener;

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
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class AJPListenerTest {

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

    private static final String UNDERTOW_SERVER_TO_BE_TESTED =
        "undertow-server-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String AJP_LISTENER_TO_BE_ADDED =
        "ajp-listener-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String AJP_LISTENER_TO_BE_REMOVED =
        "ajp-listener-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_BINDING_TO_BE_ADDED =
        "socket-binding-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_BINDING_TO_BE_REMOVED =
        "socket-binding-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING_TO_BE_ADDED));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING_TO_BE_REMOVED));
        operations.add(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_REMOVED),
            Values
                .of("socket-binding", SOCKET_BINDING_TO_BE_REMOVED.toLowerCase() + "ref"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING_TO_BE_ADDED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING_TO_BE_REMOVED));
    }

    @Before
    public void initPage() {
        page.navigate("name", UNDERTOW_SERVER_TO_BE_TESTED);
        console.verticalNavigation()
            .selectSecondary(Ids.UNDERTOW_SERVER_LISTENER_ITEM, Ids.build(Ids.UNDERTOW_SERVER_AJP_LISTENER, "item"));
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_ADDED),
            page.getAjpListenerTable(), form -> {
                form.text("name", AJP_LISTENER_TO_BE_ADDED);
                form.text("socket-binding", SOCKET_BINDING_TO_BE_ADDED.toLowerCase() + "ref");
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_REMOVED),
            page.getAjpListenerTable(), AJP_LISTENER_TO_BE_REMOVED);
    }
}
