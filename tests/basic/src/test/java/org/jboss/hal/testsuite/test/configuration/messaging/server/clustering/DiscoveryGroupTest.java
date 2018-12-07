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
import static org.jboss.hal.resources.Ids.MESSAGING_DISCOVERY_GROUP;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DG_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DG_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DG_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DG_UPDATE_ALTERNATIVES;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JGROUPS_CHANNEL;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JGROUPS_CLUSTER;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.REFRESH_TIMEOUT;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.discoveryGroupAddress;

@RunWith(Arquillian.class)
public class DiscoveryGroupTest extends AbstractClusteringTest {

    @Test
    public void discoveryGroupCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);

        crudOperations.create(discoveryGroupAddress(SRV_UPDATE, DG_CREATE), table, DG_CREATE);
    }

    @Test
    public void discoveryGroupUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);
        table.select(DG_UPDATE);
        crudOperations.update(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE), form, REFRESH_TIMEOUT, 123L);
    }

    @Test
    public void discoveryGroupTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);
        table.select(DG_UPDATE_ALTERNATIVES);
        crudOperations.updateWithError(form, f -> {
            f.text(JGROUPS_CLUSTER, EE);
            f.text(JGROUPS_CHANNEL, EE);
            f.text(SOCKET_BINDING, HTTP);
        }, JGROUPS_CLUSTER, SOCKET_BINDING);
    }

    @Test
    public void discoveryGroupRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);

        crudOperations.delete(discoveryGroupAddress(SRV_UPDATE, DG_DELETE), table, DG_DELETE);
    }

}
