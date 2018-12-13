package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.acceptor.remote;

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
import static org.jboss.hal.resources.Ids.MESSAGING_ACCEPTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_REMOTE_ACCEPTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_REM_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_REM_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_REM_TRY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_REM_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.acceptorRemoteAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class RemoteAcceptorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_UPDATE), Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_TRY_UPDATE), Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_DELETE), Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
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
    public void acceptorRemoteCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.create(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, ACCP_REM_CREATE);
                formFragment.text(SOCKET_BINDING, HTTP);
            }
        );
    }

    @Test
    public void acceptorRemoteTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_REM_CREATE, SOCKET_BINDING);
    }

    @Test
    public void acceptorRemoteUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);
        table.select(ACCP_REM_UPDATE);
        crudOperations.update(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_UPDATE), form, SOCKET_BINDING, HTTPS);
    }

    @Test
    public void acceptorRemoteTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);
        table.select(ACCP_REM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SOCKET_BINDING), SOCKET_BINDING);
    }

    @Test
    public void acceptorRemoteRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.delete(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_DELETE), table, ACCP_REM_DELETE);
    }

}
