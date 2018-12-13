package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.http;

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

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENDPOINT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_ACCEPTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_HTTP_CONNECTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_HTTP_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_HTTP_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_HTTP_TRY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONN_HTTP_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SERVER_NAME;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.connectorHttpAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class HttpConnectorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_UPDATE),
            Values.of(ENDPOINT, HTTP_ACCEPTOR).and(SOCKET_BINDING, HTTP)).assertSuccess();
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_TRY_UPDATE),
            Values.of(ENDPOINT, HTTP_ACCEPTOR).and(SOCKET_BINDING, HTTP)).assertSuccess();
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_DELETE),
            Values.of(ENDPOINT, DEFAULT).and(SOCKET_BINDING, HTTP)).assertSuccess();
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
    public void connectorHttpCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.create(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_HTTP_CREATE);
                formFragment.text(ENDPOINT, HTTP_ACCEPTOR);
                formFragment.text(SOCKET_BINDING, HTTP);
            }
        );
    }

    @Test
    public void connectorHttpTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_HTTP_CREATE, ENDPOINT);
    }

    @Test
    public void connectorHttpUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);
        table.select(CONN_HTTP_UPDATE);
        crudOperations.update(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_UPDATE), form, SERVER_NAME);
    }

    @Test
    public void connectorHttpTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);
        table.select(CONN_HTTP_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(ENDPOINT), ENDPOINT);
    }

    @Test
    public void connectorHttpRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.delete(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_DELETE), table, CONN_HTTP_DELETE);
    }

}
