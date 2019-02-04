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
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
import org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures;
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
public class DiscoveryGroupTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-to-create-" + Random.name();
    private static final String DISCOVERY_GROUP_UPDATE = "discovery-group-to-update-" + Random.name();
    private static final String DISCOVERY_GROUP_DELETE = "discovery-group-to-delete-" + Random.name();
    private static final String JGROUPS_CHANNEL_UPDATE = "jgroups-channel-" + Random.name();
    private static final String LOCAL_SOCKET_BINDING = "local-socket-binding-" + Random.name();
    private static final String JGROUPS_CHANNEL = "jgroups-channel";
    private static final String JGROUPS_CLUSTER = "jgroups-cluster";
    private static final String SOCKET_BINDING = "socket-binding";

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE)).assertSuccess();
        operations.add(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_DELETE)).assertSuccess();
        AddLocalSocketBinding addLocalSocketBinding = new AddLocalSocketBinding(LOCAL_SOCKET_BINDING);
        client.apply(addLocalSocketBinding);
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL_UPDATE), Values.of("stack", "tcp")).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_DELETE));
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
        console.verticalNavigation().selectPrimary("msg-remote-discovery-group-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_CREATE),
            page.getDiscoveryGroupTable(), DISCOVERY_GROUP_CREATE);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_DELETE),
            page.getDiscoveryGroupTable(), DISCOVERY_GROUP_DELETE);
    }

    @Test
    public void editInitialWaitTimeout() throws Exception {
        page.getDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getDicoveryGroupForm(), "initial-wait-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editJGroupsChannel() throws Exception {
        page.getDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getDicoveryGroupForm(), formFragment -> {
                formFragment.text(JGROUPS_CHANNEL, JGROUPS_CHANNEL_UPDATE);
                formFragment.text(JGROUPS_CLUSTER, Random.name());
                formFragment.clear(SOCKET_BINDING);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(JGROUPS_CHANNEL, JGROUPS_CHANNEL_UPDATE));
    }

    @Test
    public void editJGroupsCluster() throws Exception {
        String jgroupsCluster = Random.name();
        page.getDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getDicoveryGroupForm(), formFragment -> {
                formFragment.clear(JGROUPS_CHANNEL);
                formFragment.text(JGROUPS_CLUSTER, jgroupsCluster);
                formFragment.clear(SOCKET_BINDING);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(JGROUPS_CLUSTER, jgroupsCluster));
    }

    @Test
    public void editRefreshTimeout() throws Exception {
        page.getDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getDicoveryGroupForm(), "refresh-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editSocketBinding() throws Exception {
        page.getDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getDicoveryGroupForm(), formFragment -> {
                formFragment.clear(JGROUPS_CHANNEL);
                formFragment.clear(JGROUPS_CLUSTER);
                formFragment.text(SOCKET_BINDING, LOCAL_SOCKET_BINDING);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(SOCKET_BINDING, LOCAL_SOCKET_BINDING));
    }
}
