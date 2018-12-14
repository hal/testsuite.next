package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.QUEUE_ADDRESS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CORE_QUEUE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.COREQUEUE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.COREQUEUE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.coreQueueAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class CoreQueueTest extends AbstractServerDestinationsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(coreQueueAddress(SRV_UPDATE, COREQUEUE_DELETE), Values.of(QUEUE_ADDRESS, Random.name())).assertSuccess();
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
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.create(coreQueueAddress(SRV_UPDATE, COREQUEUE_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, COREQUEUE_CREATE);
                formFragment.text(QUEUE_ADDRESS, Random.name());
            }
        );
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.delete(coreQueueAddress(SRV_UPDATE, COREQUEUE_DELETE), table, COREQUEUE_DELETE);
    }

}
