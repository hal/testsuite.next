package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PAUSED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_JMS_TOPIC;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSTOPIC_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSTOPIC_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSTOPIC_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.jmsTopicAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;

@RunWith(Arquillian.class)
public class JMSTopicTest extends AbstractServerDestinationsTest {

    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_UPDATE), Values.ofList(ENTRIES, Random.name())).assertSuccess();
        operations.add(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_DELETE), Values.ofList(ENTRIES, Random.name())).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void navigate() {
        page.navigate(SERVER, SRV_UPDATE);
    }

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

    @Test
    public void pause() throws Exception {
        FinderPath path = runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_SERVER_RUNTIME, Ids.messagingServer(SRV_UPDATE));
        String itemId = Ids.destination(null, null, SRV_UPDATE, "jms-topic",
                JMSTOPIC_UPDATE);

        ColumnFragment column = console.finder(NameTokens.RUNTIME, path)
                .column(Ids.MESSAGING_SERVER_DESTINATION_RUNTIME);
        column.selectItem(itemId)
                .dropdown()
                .click("Pause");

        new ResourceVerifier(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_UPDATE), client)
                .verifyAttribute(PAUSED, true);
    }
}
