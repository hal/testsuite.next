package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.connector.generic;

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
import org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures;
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
public class GenericConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String GENERIC_CONNECTOR_CREATE = "generic-connector-to-create-" + Random.name();
    private static final String GENERIC_CONNECTOR_UPDATE = "generic-connector-to-update-" + Random.name();
    private static final String GENERIC_CONNECTOR_DELETE = "generic-connector-to-delete-" + Random.name();
    private static final String LOCAL_SOCKET_BINDING = "local-socket-binding-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE),
            Values.of(MessagingFixtures.FACTORY_CLASS, Random.name())).assertSuccess();
        operations.add(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_DELETE),
            Values.of(MessagingFixtures.FACTORY_CLASS, Random.name())).assertSuccess();
        AddLocalSocketBinding addLocalSocketBinding = new AddLocalSocketBinding(LOCAL_SOCKET_BINDING);
        client.apply(addLocalSocketBinding);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(
                RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_CREATE));
            operations.removeIfExists(
                RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE));
            operations.removeIfExists(
                RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_DELETE));
            client.apply(new RemoveLocalSocketBinding(LOCAL_SOCKET_BINDING));
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
        console.verticalNavigation().selectSecondary("msg-remote-connector-group-item", "msg-remote-connector-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_CREATE),
            page.getGenericConnectorTable(),
            formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, GENERIC_CONNECTOR_CREATE);
                formFragment.text(MessagingFixtures.FACTORY_CLASS, Random.name());
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_DELETE),
            page.getGenericConnectorTable(), GENERIC_CONNECTOR_DELETE);
    }

    @Test
    public void editFactoryClass() throws Exception {
        page.getGenericConnectorTable().select(GENERIC_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE),
            page.getGenericConnectorForm(), MessagingFixtures.FACTORY_CLASS);
    }

    @Test
    public void editParams() throws Exception {
        page.getGenericConnectorTable().select(GENERIC_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE),
            page.getGenericConnectorForm(), "params",
            new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(Random.name(), Random.name()).build());
    }

    @Test
    public void editSocketBinding() throws Exception {
        page.getGenericConnectorTable().select(GENERIC_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE),
            page.getGenericConnectorForm(), "socket-binding", LOCAL_SOCKET_BINDING);
    }
}
