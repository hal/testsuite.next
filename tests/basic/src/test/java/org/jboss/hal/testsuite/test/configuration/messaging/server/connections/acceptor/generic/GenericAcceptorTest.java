package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.acceptor.generic;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_ACCEPTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_GEN_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_GEN_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_GEN_TRY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_GEN_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.FACTORY_CLASS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.acceptorGenericAddress;

@RunWith(Arquillian.class)
public class GenericAcceptorTest extends AbstractServerConnectionsTest {
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
