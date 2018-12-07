package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_JMS_TOPIC;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSTOPIC_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSTOPIC_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSTOPIC_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.jmsTopicAddress;

@RunWith(Arquillian.class)
public class JMSTopicTest extends AbstractServerDestinationsTest {

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);

        crudOperations.create(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, JMSTOPIC_CREATE);
                formFragment.properties(ENTRIES).add(Random.name());
            }
        );
    }

    @Test
    public void editEntries() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);
        String val = Random.name();

        table.select(JMSTOPIC_UPDATE);
        crudOperations.update(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_UPDATE), form,
            formFragment -> formFragment.list(ENTRIES).add(val),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(ENTRIES, val));
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);

        crudOperations.delete(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_DELETE), table, JMSTOPIC_DELETE);
    }

}
