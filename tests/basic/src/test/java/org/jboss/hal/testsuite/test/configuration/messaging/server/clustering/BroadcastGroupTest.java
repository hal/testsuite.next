package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.EE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_BROADCAST_GROUP;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BG_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BG_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BG_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BROADCAST_PERIOD;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JGROUPS_CHANNEL;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JGROUPS_CLUSTER;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.broadcastGroupAddress;

@RunWith(Arquillian.class)
public class BroadcastGroupTest extends AbstractClusteringTest {

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
