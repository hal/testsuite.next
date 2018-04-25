package org.jboss.hal.testsuite.test.configuration.transaction;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.page.configuration.TransactionPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class RecoveryTest {

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

    private static String defaultSocketBinding;
    private static String defaultStatusSocketBinding;

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        defaultSocketBinding =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.SOCKET_BINDING).stringValue();
        defaultStatusSocketBinding =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATUS_SOCKET_BINDING).stringValue();
        client.apply(new AddLocalSocketBinding(TransactionFixtures.RECOVERY_SOCKET_BINDING_CREATE));
        client.apply(new AddLocalSocketBinding(TransactionFixtures.RECOVERY_STATUS_SOCKET_BINDING));
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException {
        client.apply(new RemoveLocalSocketBinding(TransactionFixtures.RECOVERY_SOCKET_BINDING_CREATE));
        client.apply(new RemoveLocalSocketBinding(TransactionFixtures.RECOVERY_STATUS_SOCKET_BINDING));
        operations.writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.SOCKET_BINDING, defaultSocketBinding);
        operations.writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATUS_SOCKET_BINDING,
            defaultStatusSocketBinding);
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("tx", "recovery", "config", "item"));
    }

    @Test
    public void editSocketBinding() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getRecoveryForm(),
            TransactionFixtures.SOCKET_BINDING,
            TransactionFixtures.RECOVERY_SOCKET_BINDING_CREATE);
    }

    @Test
    public void editStatusSocketBinding() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getRecoveryForm(),
            TransactionFixtures.STATUS_SOCKET_BINDING,
            TransactionFixtures.RECOVERY_STATUS_SOCKET_BINDING);
    }

    @Test
    public void toggleRecoveryListener() throws Exception {
        boolean recoveryListener =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.RECOVERY_LISTENER).booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getRecoveryForm(),
            TransactionFixtures.RECOVERY_LISTENER,
            !recoveryListener);
    }
}
