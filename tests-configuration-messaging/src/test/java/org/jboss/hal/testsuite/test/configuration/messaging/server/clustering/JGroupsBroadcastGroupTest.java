package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTORS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_ACCEPTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_LISTENER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ENDPOINT;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BROADCAST_PERIOD;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JGROUPS_CLUSTER;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.acceptorHttpAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.connectorHttpAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.jgroupsBroadcastGroupAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class JGroupsBroadcastGroupTest extends AbstractClusteringTest {

    private static final String MESSAGING_BROADCAST_GROUP = "messaging-broadcast-group";
    private static final String MESSAGING_JGROUPS_BROADCAST_GROUP = "msg-jgroups-broadcast-group";

    private static final ModelNode CONNECTORS_LIST;

    static {
        CONNECTORS_LIST = new ModelNode();
        CONNECTORS_LIST.setEmptyList().add(HTTP_CONNECTOR);
    }

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(acceptorHttpAddress(SRV_UPDATE, HTTP_ACCEPTOR), Values.of(HTTP_LISTENER, DEFAULT));
        operations.add(connectorHttpAddress(SRV_UPDATE, HTTP_CONNECTOR), Values.of(ENDPOINT, DEFAULT)
                .and(SOCKET_BINDING, HTTP));
        operations.add(jgroupsBroadcastGroupAddress(SRV_UPDATE, BG_UPDATE), Values.of(JGROUPS_CLUSTER, Random.name())
                .and(CONNECTORS, CONNECTORS_LIST)).assertSuccess();
        operations.add(jgroupsBroadcastGroupAddress(SRV_UPDATE, BG_DELETE), Values.of(JGROUPS_CLUSTER, Random.name())
                .and(CONNECTORS, CONNECTORS_LIST)).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void broadcastGroupCreate() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM), Ids.build(MESSAGING_JGROUPS_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getJGroupsBroadcastGroupTable();
        FormFragment form = page.getJGroupsBroadcastGroupForm();
        table.bind(form);

        crudOperations.create(jgroupsBroadcastGroupAddress(SRV_UPDATE, BG_CREATE), table, formFragment -> {
            formFragment.text(NAME, BG_CREATE);
            formFragment.list(CONNECTORS).add(HTTP_CONNECTOR);
            formFragment.text(JGROUPS_CLUSTER, Random.name());
        });
    }

    @Test
    public void broadcastGroupUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM), Ids.build(MESSAGING_JGROUPS_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getJGroupsBroadcastGroupTable();
        FormFragment form = page.getJGroupsBroadcastGroupForm();
        table.bind(form);
        table.scrollToTop();
        table.select(BG_UPDATE);
        crudOperations.update(jgroupsBroadcastGroupAddress(SRV_UPDATE, BG_UPDATE), form, BROADCAST_PERIOD, 123L);
    }

    @Test
    public void broadcastGroupRemove() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM), Ids.build(MESSAGING_JGROUPS_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getJGroupsBroadcastGroupTable();
        FormFragment form = page.getJGroupsBroadcastGroupForm();
        table.bind(form);

        crudOperations.delete(jgroupsBroadcastGroupAddress(SRV_UPDATE, BG_DELETE), table, BG_DELETE);
    }

}
