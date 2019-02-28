package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.service;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR_SERVICE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ConnectorServiceTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(connectorServiceAddress(SRV_UPDATE, CONN_SVC_UPDATE),
                Values.of(FACTORY_CLASS, CONNECTOR_FACTORY_CLASS)).assertSuccess();
        operations.add(connectorServiceAddress(SRV_UPDATE, CONN_SVC_DELETE),
                Values.of(FACTORY_CLASS, CONNECTOR_FACTORY_CLASS)).assertSuccess();
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
    public void connectorServiceCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.create(connectorServiceAddress(SRV_UPDATE, CONN_SVC_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_SVC_CREATE);
                formFragment.text(FACTORY_CLASS, CONNECTOR_FACTORY_CLASS);
            }
        );
    }

    @Test
    public void connectorServiceTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_SVC_CREATE, FACTORY_CLASS);
    }

    @Test
    public void connectorServiceUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);
        table.select(CONN_SVC_UPDATE);
        crudOperations.update(connectorServiceAddress(SRV_UPDATE, CONN_SVC_UPDATE), form, FACTORY_CLASS, "org.apache.activemq.artemis.ArtemisConstants");
    }

    @Test
    public void connectorServiceTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);
        table.select(CONN_SVC_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void connectorServiceRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.delete(connectorServiceAddress(SRV_UPDATE, CONN_SVC_DELETE), table, CONN_SVC_DELETE);
    }

}
