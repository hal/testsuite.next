package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.EE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_BROADCAST_GROUP;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BG_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.BROADCAST_PERIOD;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JGROUPS_CHANNEL;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JGROUPS_CLUSTER;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.broadcastGroupAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class BroadcastGroupTest extends AbstractClusteringTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_UPDATE)).assertSuccess();
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_DELETE)).assertSuccess();
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
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);

        crudOperations.create(broadcastGroupAddress(SRV_UPDATE, BG_CREATE), table, BG_CREATE);
    }

    @Test
    public void broadcastGroupUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);
        table.scrollToTop();
        table.select(BG_UPDATE);
        crudOperations.update(broadcastGroupAddress(SRV_UPDATE, BG_UPDATE), form, BROADCAST_PERIOD, 123L);
    }

    @Test
    public void broadcastGroupTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);
        table.select(BG_UPDATE);
        crudOperations.updateWithError(form, f -> {
            f.text(JGROUPS_CLUSTER, EE);
            f.text(JGROUPS_CHANNEL, EE);
            f.text(SOCKET_BINDING, HTTP);
        }, JGROUPS_CLUSTER, SOCKET_BINDING);
    }

    @Test
    public void broadcastGroupRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);

        crudOperations.delete(broadcastGroupAddress(SRV_UPDATE, BG_DELETE), table, BG_DELETE);
    }

}
