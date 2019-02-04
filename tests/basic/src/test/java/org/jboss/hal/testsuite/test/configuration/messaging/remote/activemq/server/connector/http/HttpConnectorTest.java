package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.connector.http;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
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

import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class HttpConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String HTTP_CONNECTOR_CREATE = "http-connector-to-create-" + Random.name();
    private static final String HTTP_CONNECTOR_UPDATE = "http-connector-to-update-" + Random.name();
    private static final String HTTP_CONNECTOR_DELETE = "http-connector-to-delete-" + Random.name();

    private static final String LOCAL_SOCKET_BINDING = "local-socket-binding-" + Random.name();
    private static final String LOCAL_SOCKET_BINDING_UPDATE = "local-socket-binding-update-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        client.apply(new AddLocalSocketBinding(LOCAL_SOCKET_BINDING));
        client.apply(new AddLocalSocketBinding(LOCAL_SOCKET_BINDING_UPDATE));
        createHttpConnector(HTTP_CONNECTOR_DELETE, LOCAL_SOCKET_BINDING);
        createHttpConnector(HTTP_CONNECTOR_UPDATE, LOCAL_SOCKET_BINDING);
    }

    private static void createHttpConnector(String name, String socketBinding) throws IOException {
        operations.add(RemoteActiveMQServer.httpConnectorAddress(name), Values.of("endpoint", Random.name())
        .and("socket-binding", socketBinding)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(
                RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_CREATE));
            operations.removeIfExists(
                RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_UPDATE));
            operations.removeIfExists(
                RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_DELETE));
            client.apply(new RemoveLocalSocketBinding(LOCAL_SOCKET_BINDING));
            client.apply(new RemoveLocalSocketBinding(LOCAL_SOCKET_BINDING_UPDATE));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private MessagingRemoteActiveMQPage page;

    @Before
    public void navigate() {
        page.navigate();
        console.verticalNavigation().selectSecondary("msg-remote-connector-group-item", "msg-remote-http-connector-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_CREATE),
            page.getHttpConnectorTable(),
            formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, HTTP_CONNECTOR_CREATE);
                formFragment.text("endpoint", Random.name());
                formFragment.text("socket-binding", LOCAL_SOCKET_BINDING);
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_DELETE),
            page.getHttpConnectorTable(), HTTP_CONNECTOR_DELETE);
    }

    @Test
    public void editEndpoint() throws Exception {
        page.getHttpConnectorTable().select(HTTP_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
            page.getHttpConnectorForm(), "endpoint");
    }

    @Test
    public void editParams() throws Exception {
        page.getHttpConnectorTable().select(HTTP_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
            page.getHttpConnectorForm(), "params",
            new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(Random.name(), Random.name()).build());
    }

    @Test
    public void editServerName() throws Exception {
        page.getHttpConnectorTable().select(HTTP_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
            page.getHttpConnectorForm(), "server-name");
    }

    @Test
    public void editSocketBinding() throws Exception {
        page.getHttpConnectorTable().select(HTTP_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
            page.getHttpConnectorForm(), "socket-binding", LOCAL_SOCKET_BINDING_UPDATE);
    }

}
