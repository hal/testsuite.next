package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.connector.in.vm;

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
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
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

import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class InVMConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String IN_VM_CONNECTOR_CREATE = "in-vm-connector-to-create-" + Random.name();
    private static final String IN_VM_CONNECTOR_UPDATE = "in-vm-connector-to-update-" + Random.name();
    private static final String IN_VM_CONNECTOR_DELETE = "in-vm-connector-to-delete-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        createInVMConnector(IN_VM_CONNECTOR_DELETE);
        createInVMConnector(IN_VM_CONNECTOR_UPDATE);
    }

    public static void createInVMConnector(String name) throws IOException {
        operations.add(RemoteActiveMQServer.inVMConnectorAddress(name), Values.of("server-id", Random.number()))
            .assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_DELETE));
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
        console.verticalNavigation()
            .selectSecondary("msg-remote-connector-group-item", "msg-remote-in-vm-connector-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_CREATE),
            page.getInVMConnectorTable(),
            formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, IN_VM_CONNECTOR_CREATE);
                formFragment.number("server-id", Random.number());
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_DELETE),
            page.getInVMConnectorTable(), IN_VM_CONNECTOR_DELETE);
    }

    @Test
    public void editParams() throws Exception {
        page.getInVMConnectorTable().select(IN_VM_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_UPDATE),
            page.getInVMConnectorForm(), "params",
            new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(Random.name(), Random.name()).build());
    }

    @Test
    public void editServerId() throws Exception {
        page.getInVMConnectorTable().select(IN_VM_CONNECTOR_UPDATE);
        crudOperations.update(RemoteActiveMQServer.inVMConnectorAddress(IN_VM_CONNECTOR_UPDATE),
            page.getInVMConnectorForm(), "server-id", Random.number());
    }
}
