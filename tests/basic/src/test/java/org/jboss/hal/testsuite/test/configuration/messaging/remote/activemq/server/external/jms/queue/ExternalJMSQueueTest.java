package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.external.jms.queue;

import java.io.IOException;
import java.util.Arrays;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class ExternalJMSQueueTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String EXTERNAL_JMS_QUEUE_CREATE = "external-jms-queue-to-be-created-" + Random.name();
    private static final String EXTERNAL_JMS_QUEUE_UPDATE = "external-jms-queue-to-be-updated-" + Random.name();
    private static final String EXTERNAL_JMS_QUEUE_DELETE = "external-jms-queue-to-be-deleted-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        createExternalJMSQueue(EXTERNAL_JMS_QUEUE_UPDATE);
        createExternalJMSQueue(EXTERNAL_JMS_QUEUE_DELETE);
    }

    private static void createExternalJMSQueue(String name) throws IOException {
        operations.add(RemoteActiveMQServer.externalJMSQueueAddress(name),
            Values.of("entries", new ModelNodeGenerator.ModelNodeListBuilder()
                .addAll(Random.name(), Random.name(), Random.name()).build())).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.externalJMSQueueAddress(EXTERNAL_JMS_QUEUE_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.externalJMSQueueAddress(EXTERNAL_JMS_QUEUE_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.externalJMSQueueAddress(EXTERNAL_JMS_QUEUE_DELETE));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Page
    private MessagingRemoteActiveMQPage page;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Before
    public void before() {
        page.navigate();
        console.verticalNavigation().selectPrimary("msg-remote-external-queue-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.externalJMSQueueAddress(EXTERNAL_JMS_QUEUE_CREATE),
            page.getExternalJMSQueueTable(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, EXTERNAL_JMS_QUEUE_CREATE);
                formFragment.list("entries").add(Arrays.asList(Random.name(), Random.name(), Random.name()));
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.externalJMSQueueAddress(EXTERNAL_JMS_QUEUE_DELETE),
            page.getExternalJMSQueueTable(), EXTERNAL_JMS_QUEUE_DELETE);
    }
}
