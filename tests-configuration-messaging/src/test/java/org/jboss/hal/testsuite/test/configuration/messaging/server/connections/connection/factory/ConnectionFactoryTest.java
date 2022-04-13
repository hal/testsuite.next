package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connection.factory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTORS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTION_FACTORY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CALL_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_FAC_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_FAC_CREATE_ENTRY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_FAC_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_FAC_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_FAC_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.connectionFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.jgroupsDiscoveryGroupAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class ConnectionFactoryTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException, InterruptedException, TimeoutException {
        createServer(SRV_UPDATE);
        String discoveryGroup = Random.name();
        operations.add(jgroupsDiscoveryGroupAddress(SRV_UPDATE, discoveryGroup)).assertSuccess();
        administration.reload();
        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_UPDATE),
                Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, discoveryGroup)).assertSuccess();
        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_TRY_UPDATE),
                Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, discoveryGroup)).assertSuccess();
        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_DELETE),
                Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, discoveryGroup)).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void setUp() throws Exception {
        page.navigateAgain(SERVER, SRV_UPDATE);
    }

    @Test
    public void connectionFactoryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);

        crudOperations.create(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_FAC_CREATE);
                    formFragment.text(DISCOVERY_GROUP, Random.name());
                    formFragment.list(ENTRIES).add(CONN_FAC_CREATE_ENTRY);
                }
        );
    }

    @Test
    public void connectionFactoryTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_FAC_CREATE, DISCOVERY_GROUP);
    }

    @Test
    public void connectionFactoryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);
        table.select(CONN_FAC_UPDATE);
        crudOperations.update(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_UPDATE), form,
                formFragment -> {
                    formFragment.number(CALL_TIMEOUT, 123L);
                    formFragment.flip("use-topology-for-load-balancing", false);
                },
                verifier -> {
                    verifier.verifyAttribute(CALL_TIMEOUT, 123L);
                    verifier.verifyAttribute("use-topology-for-load-balancing", false);
                });
    }

    @Test
    public void connectionFactoryTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);
        table.select(CONN_FAC_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(CONNECTORS).add(Random.name()), DISCOVERY_GROUP);
    }

    @Test
    public void connectionFactoryRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        crudOperations.delete(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_DELETE), table, CONN_FAC_DELETE);
    }
}
