package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.remote;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_REMOTE_CONNECTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_REM_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_REM_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_REM_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.connectorRemoteAddress;

@RunWith(Arquillian.class)
public class RemoteConnectorTest extends AbstractServerConnectionsTest {

    @Test
    public void connectorRemoteCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.create(connectorRemoteAddress(SRV_UPDATE, CONN_REM_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_REM_CREATE);
                formFragment.text(SOCKET_BINDING, HTTP);
            }
        );
    }

    @Test
    public void connectorRemoteTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_REM_CREATE, SOCKET_BINDING);
    }

    @Test
    public void connectorRemoteUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);
        table.select(CONN_REM_UPDATE);
        crudOperations.update(connectorRemoteAddress(SRV_UPDATE, CONN_REM_UPDATE), form, SOCKET_BINDING, HTTPS);
    }

    @Test
    public void connectorRemoteTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);
        table.select(CONN_REM_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SOCKET_BINDING), SOCKET_BINDING);
    }

    @Test
    public void connectorRemoteRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.delete(connectorRemoteAddress(SRV_UPDATE, CONN_REM_DELETE), table, CONN_REM_DELETE);
    }

}
