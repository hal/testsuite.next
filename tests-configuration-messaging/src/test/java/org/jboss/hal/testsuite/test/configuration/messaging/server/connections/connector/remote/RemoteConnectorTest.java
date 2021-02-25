package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.remote;

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

import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_REMOTE_CONNECTOR;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_REM_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_REM_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_REM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.connectorRemoteAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class RemoteConnectorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(connectorRemoteAddress(SRV_UPDATE, CONN_REM_UPDATE), Values.of(SOCKET_BINDING, HTTP))
            .assertSuccess();
        operations.add(connectorRemoteAddress(SRV_UPDATE, CONN_REM_DELETE), Values.of(SOCKET_BINDING, HTTP))
            .assertSuccess();
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
