package org.jboss.hal.testsuite.test.configuration.transaction;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.page.configuration.TransactionPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ProcessTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private TransactionPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws CommandFailedException {
        client.apply(new AddLocalSocketBinding(TransactionFixtures.PROCESS_SOCKET_BINDING_CREATE));
        client.apply(new AddLocalSocketBinding(TransactionFixtures.PROCESS_SOCKET_BINDING_WITH_PROCESS_ID_UUID));
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException {
        try {
            client.apply(new RemoveLocalSocketBinding(TransactionFixtures.PROCESS_SOCKET_BINDING_CREATE));
            client.apply(new RemoveLocalSocketBinding(TransactionFixtures.PROCESS_SOCKET_BINDING_WITH_PROCESS_ID_UUID));
        } finally {
            client.close();
        }
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("tx", "process", "item"));
    }

    @Test
    public void toggleProcessIDUUID() throws Exception {
        boolean processIDUUID =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.PROCESS_ID_UUID).booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getProcessForm(), formFragment -> {
                formFragment.flip(TransactionFixtures.PROCESS_ID_UUID, !processIDUUID);
                if (processIDUUID) {
                    formFragment.text(TransactionFixtures.PROCESS_ID_SOCKET_BINDING, TransactionFixtures.PROCESS_SOCKET_BINDING_WITH_PROCESS_ID_UUID);
                } else {
                    formFragment.text(TransactionFixtures.PROCESS_ID_SOCKET_BINDING,"");
                }
            },
            resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.PROCESS_ID_UUID, !processIDUUID));
    }

    @Test
    public void editProcessIdSocketBinding() throws Exception {
        boolean processIDUUID =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.PROCESS_ID_UUID).booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getProcessForm(), formFragment -> {
            if (processIDUUID) {
                formFragment.flip(TransactionFixtures.PROCESS_ID_UUID, false);
            }
            formFragment.text(TransactionFixtures.PROCESS_ID_SOCKET_BINDING, TransactionFixtures.PROCESS_SOCKET_BINDING_CREATE);
            },
            resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.PROCESS_ID_SOCKET_BINDING, TransactionFixtures.PROCESS_SOCKET_BINDING_CREATE));
    }

    @Test
    public void editProcessIDSocketMaxPorts() throws Exception {
        prepareProcessID();
        try {
            crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getProcessForm(),
                TransactionFixtures.PROCESS_ID_SOCKET_MAX_PORTS, Random.number());
        } catch (TimeoutException e) {
            Assert.fail("HAL-1454");
        }
    }

    private void prepareProcessID() throws IOException {
        Batch batch = new Batch().undefineAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
            TransactionFixtures.PROCESS_ID_UUID)
            .writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.PROCESS_ID_SOCKET_BINDING, TransactionFixtures.PROCESS_SOCKET_BINDING_CREATE);
        operations.batch(batch);
    }
}
