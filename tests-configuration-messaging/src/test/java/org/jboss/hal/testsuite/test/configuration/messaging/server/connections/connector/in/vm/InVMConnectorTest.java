package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.connector.in.vm;

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
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CONNECTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_IN_VM_CONNECTOR;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_INVM_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_INVM_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_INVM_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONN_INVM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SERVER_ID;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.connectorInVMAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class InVMConnectorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_UPDATE), Values.of(SERVER_ID, 11)).assertSuccess();
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_TRY_UPDATE), Values.of(SERVER_ID, 12)).assertSuccess();
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_DELETE), Values.of(SERVER_ID, 22)).assertSuccess();
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
    public void connectorInVMCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.create(connectorInVMAddress(SRV_UPDATE, CONN_INVM_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, CONN_INVM_CREATE);
                formFragment.number(SERVER_ID, 123);
            }
        );
    }

    @Test
    public void connectorInVMTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_INVM_CREATE, SERVER_ID);
    }

    @Test
    public void connectorInVMUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);
        table.select(CONN_INVM_UPDATE);
        crudOperations.update(connectorInVMAddress(SRV_UPDATE, CONN_INVM_UPDATE), form, SERVER_ID, 89);
    }

    @Test
    public void connectorInVMTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);
        table.select(CONN_INVM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SERVER_ID), SERVER_ID);
    }

    @Test
    public void connectorInVMRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.delete(connectorInVMAddress(SRV_UPDATE, CONN_INVM_DELETE), table, CONN_INVM_DELETE);
    }
}
