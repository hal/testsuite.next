package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connection.factory;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTORS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTION_FACTORY;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CALL_TIMEOUT;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_FAC_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_FAC_CREATE_ENTRY;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_FAC_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_FAC_TRY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_FAC_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.connectionFactoryAddress;

@RunWith(Arquillian.class)
public class ConnectionFactoryTest extends AbstractServerConnectionsTest {

    @Test
    public void connectionFactoryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);

        crudOperations.create(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_FAC_CREATE);
                formFragment.text(DISCOVERY_GROUP, anyString);
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
        crudOperations.update(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_UPDATE), form, CALL_TIMEOUT, 123L);
    }

    @Test
    public void connectionFactoryTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);
        table.select(CONN_FAC_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(CONNECTORS).add(anyString), DISCOVERY_GROUP);
    }

    @Test
    public void connectionFactoryRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        crudOperations.delete(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_DELETE), table, CONN_FAC_DELETE);
    }

}
