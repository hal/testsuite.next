package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.acceptor.generic;

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
import static org.jboss.hal.resources.Ids.MESSAGING_ACCEPTOR;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.ACCP_GEN_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.ACCP_GEN_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.ACCP_GEN_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.ACCP_GEN_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.FACTORY_CLASS;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.acceptorGenericAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class GenericAcceptorTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_UPDATE), Values.of(FACTORY_CLASS, Random.name())).assertSuccess();
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_TRY_UPDATE), Values.of(FACTORY_CLASS, Random.name())).assertSuccess();
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_DELETE), Values.of(FACTORY_CLASS, Random.name())).assertSuccess();
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
    public void acceptorGenericCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.create(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, ACCP_GEN_CREATE);
                formFragment.text(FACTORY_CLASS, Random.name());
            }
        );
    }

    @Test
    public void acceptorGenericTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_GEN_CREATE, FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);
        table.select(ACCP_GEN_UPDATE);
        crudOperations.update(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_UPDATE), form, FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);
        table.select(ACCP_GEN_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.delete(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_DELETE), table, ACCP_GEN_DELETE);
    }
}
