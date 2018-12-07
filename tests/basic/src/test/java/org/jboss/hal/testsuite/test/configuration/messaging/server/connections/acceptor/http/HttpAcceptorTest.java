package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.acceptor.http;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_LISTENER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_ACCEPTOR;
import static org.jboss.hal.resources.Ids.MESSAGING_HTTP_ACCEPTOR;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_HTTP_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_HTTP_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ACCP_HTTP_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.UPGRADE_LEGACY;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.acceptorHttpAddress;

@RunWith(Arquillian.class)
public class HttpAcceptorTest extends AbstractServerConnectionsTest {

    @Test
    public void acceptorHttpCreate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.create(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, ACCP_HTTP_CREATE);
                formFragment.text(HTTP_LISTENER, DEFAULT);
            }
        );
    }

    @Test
    public void acceptorHttpTryCreate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_HTTP_CREATE, HTTP_LISTENER);
    }

    @Test
    public void acceptorHttpUpdate() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);
        table.select(ACCP_HTTP_UPDATE);
        crudOperations.update(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_UPDATE), form, UPGRADE_LEGACY, false);
    }

    @Test
    public void acceptorHttpTryUpdate() {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);
        table.select(ACCP_HTTP_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(HTTP_LISTENER), HTTP_LISTENER);
    }

    @Test
    public void acceptorHttpRemove() throws Exception {
        console.verticalNavigation()
            .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.delete(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_DELETE), table, ACCP_HTTP_DELETE);
    }

}
