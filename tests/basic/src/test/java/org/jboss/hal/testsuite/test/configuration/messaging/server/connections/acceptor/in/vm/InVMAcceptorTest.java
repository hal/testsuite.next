package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.acceptor.in.vm;

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
import static org.jboss.hal.resources.Ids.MESSAGING_ACCEPTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_IN_VM_ACCEPTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_INVM_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_INVM_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_INVM_TRY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_INVM_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SERVER_ID;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.acceptorInVMAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class InVMAcceptorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_UPDATE), Values.of(SERVER_ID, 11)).assertSuccess();
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_TRY_UPDATE), Values.of(SERVER_ID, 12)).assertSuccess();
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_DELETE), Values.of(SERVER_ID, 22)).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Test
    public void acceptorInVMCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.create(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, ACCP_INVM_CREATE);
                formFragment.number(SERVER_ID, 123);
            }
        );
    }

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void acceptorInVMTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_INVM_CREATE, SERVER_ID);
    }

    @Test
    public void acceptorInVMUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);
        table.select(ACCP_INVM_UPDATE);
        crudOperations.update(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_UPDATE), form, SERVER_ID, 89);
    }

    @Test
    public void acceptorInVMTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);
        table.select(ACCP_INVM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SERVER_ID), SERVER_ID);
    }

    @Test
    public void acceptorInVMRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.delete(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_DELETE), table, ACCP_INVM_DELETE);
    }

}
