package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.service;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR_SERVICE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_SVC_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_SVC_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_SVC_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.FACTORY_CLASS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.connectorServiceAddress;

@RunWith(Arquillian.class)
public class ConnectorServiceTest extends AbstractServerConnectionsTest {

    @Test
    public void connectorServiceCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.create(connectorServiceAddress(SRV_UPDATE, CONN_SVC_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_SVC_CREATE);
                formFragment.text(FACTORY_CLASS, anyString);
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
        crudOperations.update(connectorServiceAddress(SRV_UPDATE, CONN_SVC_UPDATE), form, FACTORY_CLASS);
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
