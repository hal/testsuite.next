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
import org.jboss.hal.testsuite.page.configuration.TransactionPage;
import org.jboss.hal.testsuite.test.configuration.paths.PathsFixtures;
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

@RunWith(Arquillian.class)
public class PathTest {

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

    private static String defaultObjectStoreRelativeTo;

    @BeforeClass
    public static void setUp() throws IOException {
        defaultObjectStoreRelativeTo =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.OBJECT_STORE_RELATIVE_TO).stringValue();
        operations.add(PathsFixtures.pathAddress(TransactionFixtures.PATH_EDIT), Values.of("value", Random.name()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.OBJECT_STORE_RELATIVE_TO,
                defaultObjectStoreRelativeTo);
            operations.removeIfExists(PathsFixtures.pathAddress(TransactionFixtures.PATH_EDIT));
        } finally {
            client.close();
        }
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("tx", "path", "config", "item"));
    }

    @Test
    public void editObjectStorePath() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getPathForm(), "object-store-path");
    }

    @Test
    public void editObjectStoreRelativeTo() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getPathForm(),
            TransactionFixtures.OBJECT_STORE_RELATIVE_TO);
    }
}
