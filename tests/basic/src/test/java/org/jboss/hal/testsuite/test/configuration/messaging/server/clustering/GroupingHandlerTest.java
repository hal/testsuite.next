package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_GROUPING_HANDLER;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.GH_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.GH_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.GH_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.GROUPING_HANDLER_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.groupingHandlerAddress;

@RunWith(Arquillian.class)
public class GroupingHandlerTest extends AbstractClusteringTest {

    @Test
    public void groupingHandlerCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.create(groupingHandlerAddress(SRV_UPDATE, GH_CREATE), table, f -> {
            f.text(NAME, GH_CREATE);
            f.text(GROUPING_HANDLER_ADDRESS, anyString);
            f.select(TYPE, REMOTE.toUpperCase());
        });
    }

    @Test
    public void groupingHandlerTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, f -> f.text(NAME, GH_CREATE), GROUPING_HANDLER_ADDRESS);
    }

    @Test
    public void groupingHandlerUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);
        table.select(GH_UPDATE);
        crudOperations.update(groupingHandlerAddress(SRV_UPDATE, GH_UPDATE), form, GROUPING_HANDLER_ADDRESS,
            Random.name());
    }

    @Test
    public void groupingHandlerRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.delete(groupingHandlerAddress(SRV_UPDATE, GH_DELETE), table, GH_DELETE);
    }

}
