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
import org.jboss.hal.testsuite.fixtures.JGroupsFixtures;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class JGroupsDiscoveryGroupTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String REMOTE_DISCOVERY_GROUP = "msg-remote-discovery-group-item";
    private static final String REMOTE_JGROUPS_DISCOVERY_GROUP = "msg-remote-jgroups-discovery-group-item";
    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-to-create-" + Random.name();
    private static final String DISCOVERY_GROUP_UPDATE = "discovery-group-to-update-" + Random.name();
    private static final String DISCOVERY_GROUP_DELETE = "discovery-group-to-delete-" + Random.name();
    private static final String JGROUPS_CHANNEL_UPDATE = "jgroups-channel-" + Random.name();
    private static final String JGROUPS_CHANNEL = "jgroups-channel";
    private static final String JGROUPS_CLUSTER = "jgroups-cluster";

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
                Values.of(JGROUPS_CLUSTER, Random.name())).assertSuccess();
        operations.add(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE),
                Values.of(JGROUPS_CLUSTER, Random.name())).assertSuccess();
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL_UPDATE),
                Values.of("stack", "tcp")).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE));
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
        console.verticalNavigation().selectSecondary(REMOTE_DISCOVERY_GROUP, REMOTE_JGROUPS_DISCOVERY_GROUP);
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_CREATE),
                page.getJGroupsDiscoveryGroupTable(), formFragment -> {
                    formFragment.text(NAME, DISCOVERY_GROUP_CREATE);
                    formFragment.text(JGROUPS_CLUSTER, Random.name());
                });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_DELETE),
            page.getJGroupsDiscoveryGroupTable(), DISCOVERY_GROUP_DELETE);
    }

    @Test
    public void editInitialWaitTimeout() throws Exception {
        page.getJGroupsDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getJGroupsDicoveryGroupForm(), "initial-wait-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editJGroupsChannel() throws Exception {
        page.getJGroupsDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getJGroupsDicoveryGroupForm(), formFragment -> {
                formFragment.text(JGROUPS_CHANNEL, JGROUPS_CHANNEL_UPDATE);
                formFragment.text(JGROUPS_CLUSTER, Random.name());
            }, resourceVerifier -> resourceVerifier.verifyAttribute(JGROUPS_CHANNEL, JGROUPS_CHANNEL_UPDATE));
    }

    @Test
    public void editJGroupsCluster() throws Exception {
        String jgroupsCluster = Random.name();
        page.getJGroupsDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getJGroupsDicoveryGroupForm(), formFragment -> {
                formFragment.clear(JGROUPS_CHANNEL);
                formFragment.text(JGROUPS_CLUSTER, jgroupsCluster);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(JGROUPS_CLUSTER, jgroupsCluster));
    }

    @Test
    public void editRefreshTimeout() throws Exception {
        page.getJGroupsDiscoveryGroupTable().select(DISCOVERY_GROUP_UPDATE);
        crudOperations.update(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE),
            page.getJGroupsDicoveryGroupForm(), "refresh-timeout", Long.valueOf(Random.number()));
    }

}
