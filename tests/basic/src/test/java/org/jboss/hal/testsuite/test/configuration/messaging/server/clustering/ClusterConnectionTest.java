package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTOR_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STATIC_CONNECTORS;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CLUSTER_CONNECTION;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CALL_TIMEOUT;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CC_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CC_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CC_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CC_UPDATE_ALTERNATIVES;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CLUSTER_CONNECTION_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.clusterConnectionAddress;

@RunWith(Arquillian.class)
public class ClusterConnectionTest extends AbstractClusteringTest {

    @Test
    public void clusterConnectionCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        console.waitNoNotification();
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        crudOperations.create(clusterConnectionAddress(SRV_UPDATE, CC_CREATE), table, f -> {
            f.text(NAME, CC_CREATE);
            f.text(CLUSTER_CONNECTION_ADDRESS, anyString);
            f.text(CONNECTOR_NAME, HTTP_CONNECTOR);
            f.text(DISCOVERY_GROUP, anyString);
        });
    }

    @Test
    public void clusterConnectionTryCreate() {
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        crudOperations.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CC_CREATE), CLUSTER_CONNECTION_ADDRESS);
    }

    @Test
    public void clusterConnectionUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        table.scrollToTop();
        table.select(CC_UPDATE);
        crudOperations.update(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE), form, CALL_TIMEOUT, 123L);
    }

    @Test
    public void clusterConnectionTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        table.scrollToTop();
        table.select(CC_UPDATE_ALTERNATIVES);

        crudOperations.updateWithError(form, f -> {
            f.text(DISCOVERY_GROUP, anyString);
            f.list(STATIC_CONNECTORS).add(HTTP);
        }, STATIC_CONNECTORS);
    }

    @Test
    public void clusterConnectionRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);

        crudOperations.delete(clusterConnectionAddress(SRV_UPDATE, CC_DELETE), table, CC_DELETE);
    }

}
