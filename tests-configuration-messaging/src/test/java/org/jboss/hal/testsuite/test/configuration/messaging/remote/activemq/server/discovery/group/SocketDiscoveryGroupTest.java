package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.discovery.group;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.fixtures.SocketBindingFixtures;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.RemoteActiveMQServer;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.STANDARD_SOCKETS;

@RunWith(Arquillian.class)
public class SocketDiscoveryGroupTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String REMOTE_DISCOVERY_GROUP = "msg-remote-discovery-group-item";
    private static final String REMOTE_SOCKET_DISCOVERY_GROUP = "msg-remote-socket-discovery-group-item";
    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-to-create-" + Random.name();
    private static final String DISCOVERY_GROUP_UPDATE = "discovery-group-to-update-" + Random.name();
    private static final String DISCOVERY_GROUP_DELETE = "discovery-group-to-delete-" + Random.name();
    private static final String LOCAL_SOCKET_BINDING = "local-socket-binding-" + Random.name();
    private static final String SOCKET_BINDING = "socket-binding";
    private static final String MESSAGING = "messaging";

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(SocketBindingFixtures.inboundAddress("standard-sockets", MESSAGING),
                Values.of("multicast-address", "224.0.1.105").and("multicast-port", "1234"))
                .assertSuccess();
        operations.add(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
                Values.of(SOCKET_BINDING, MESSAGING)).assertSuccess();
        operations.add(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE),
                Values.of(SOCKET_BINDING, MESSAGING)).assertSuccess();
        AddLocalSocketBinding addLocalSocketBinding = new AddLocalSocketBinding(LOCAL_SOCKET_BINDING);
        client.apply(addLocalSocketBinding);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(SocketBindingFixtures.inboundAddress(STANDARD_SOCKETS, MESSAGING));
            operations.removeIfExists(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE));
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
        console.verticalNavigation().selectSecondary(REMOTE_DISCOVERY_GROUP, REMOTE_SOCKET_DISCOVERY_GROUP);
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_CREATE),
                page.getSocketDiscoveryGroupTable(), formFragment -> {
                    formFragment.text(NAME, DISCOVERY_GROUP_CREATE);
                    formFragment.text(SOCKET_BINDING, HTTP);
                });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE),
            page.getSocketDiscoveryGroupTable(), DISCOVERY_GROUP_DELETE);
    }

    @Test
    public void editInitialWaitTimeout() throws Exception {
        page.getSocketDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getSocketDicoveryGroupForm(), "initial-wait-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editRefreshTimeout() throws Exception {
        page.getSocketDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getSocketDicoveryGroupForm(), "refresh-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editSocketBinding() throws Exception {
        page.getSocketDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.socketDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getSocketDicoveryGroupForm(), formFragment -> {
                formFragment.text(SOCKET_BINDING, LOCAL_SOCKET_BINDING);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(SOCKET_BINDING, LOCAL_SOCKET_BINDING));
    }
}
