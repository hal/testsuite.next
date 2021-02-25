package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTOR_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STATIC_CONNECTORS;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CLUSTER_CONNECTION;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CALL_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CC_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CC_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CC_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CC_UPDATE_ALTERNATIVES;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CLUSTER_CONNECTION_ADDRESS;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.clusterConnectionAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class ClusterConnectionTest extends AbstractClusteringTest {

    private static final Values CC_PARAMS = Values.of(CLUSTER_CONNECTION_ADDRESS, Random.name())
        .and(CONNECTOR_NAME, HTTP_CONNECTOR)
        .and(DISCOVERY_GROUP, Random.name());

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE), CC_PARAMS).assertSuccess();
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE_ALTERNATIVES), CC_PARAMS).assertSuccess();
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_DELETE), CC_PARAMS).assertSuccess();
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
    public void clusterConnectionCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        console.waitNoNotification();
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        crudOperations.create(clusterConnectionAddress(SRV_UPDATE, CC_CREATE), table, f -> {
            f.text(NAME, CC_CREATE);
            f.text(CLUSTER_CONNECTION_ADDRESS, Random.name());
            f.text(CONNECTOR_NAME, HTTP_CONNECTOR);
            f.text(DISCOVERY_GROUP, Random.name());
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
            f.text(DISCOVERY_GROUP, Random.name());
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
