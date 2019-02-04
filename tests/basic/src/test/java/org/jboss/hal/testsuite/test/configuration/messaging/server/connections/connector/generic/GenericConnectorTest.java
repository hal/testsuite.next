package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.generic;

import java.io.IOException;

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

import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_GEN_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_GEN_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_GEN_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.FACTORY_CLASS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.connectorGenericAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class GenericConnectorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(connectorGenericAddress(SRV_UPDATE, CONN_GEN_UPDATE), Values.of(FACTORY_CLASS, Random.name())).assertSuccess();
        operations.add(connectorGenericAddress(SRV_UPDATE, CONN_GEN_DELETE), Values.of(FACTORY_CLASS, Random.name())).assertSuccess();
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
    public void connectorGenericCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.create(connectorGenericAddress(SRV_UPDATE, CONN_GEN_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_GEN_CREATE);
                formFragment.text(FACTORY_CLASS, Random.name());
            }
        );
    }

    @Test
    public void connectorGenericTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_GEN_CREATE, FACTORY_CLASS);
    }

    @Test
    public void connectorGenericUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);
        table.select(CONN_GEN_UPDATE);
        crudOperations.update(connectorGenericAddress(SRV_UPDATE, CONN_GEN_UPDATE), form, FACTORY_CLASS);
    }

    @Test
    public void connectorGenericTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);
        table.select(CONN_GEN_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void connectorGenericRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.delete(connectorGenericAddress(SRV_UPDATE, CONN_GEN_DELETE), table, CONN_GEN_DELETE);
    }
}
