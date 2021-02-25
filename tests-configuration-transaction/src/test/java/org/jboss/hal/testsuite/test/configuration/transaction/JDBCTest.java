package org.jboss.hal.testsuite.test.configuration.transaction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.TransactionPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

@RunWith(Arquillian.class)
public class JDBCTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private TransactionPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final SnapshotBackup snapshotBackup = new SnapshotBackup();

    private static final Operations operations = new Operations(client);

    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        client.apply(snapshotBackup.backup());
        createDataSource();
    }

    private static void createDataSource() throws CommandFailedException {
        client.apply(new AddDataSource.Builder(TransactionFixtures.JDBC_DATASOURCE)
            .jndiName("java:/" + TransactionFixtures.JDBC_DATASOURCE)
            .driverName("h2")
            .connectionUrl("jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1")
            .enableAfterCreate()
            .build());
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, TimeoutException, InterruptedException {
        try {
            client.apply(snapshotBackup.restore());
            administration.restartIfRequired();
        } finally {
            client.close();
        }
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("tx", "jdbc", "config", "item"));
    }

    @Test
    public void toggleJDBCActionStoreDropTable() throws Exception {
        boolean jdbcActionStoreDropTable =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.JDBC_ACTION_STORE_DROP_TABLE)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            toggleWithUseJDBCStore(TransactionFixtures.JDBC_ACTION_STORE_DROP_TABLE, !jdbcActionStoreDropTable), resourceVerifier -> {
                resourceVerifier.verifyAttribute(TransactionFixtures.JDBC_ACTION_STORE_DROP_TABLE, !jdbcActionStoreDropTable);
            });
    }

    @Test
    public void editJDBCActionStoreTablePrefix() throws Exception {
        String jdbcActionStoreTablePrefix = Random.name();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            editWithUseJDBCStore(TransactionFixtures.JDBC_ACTION_STORE_TABLE_PREFIX,
                jdbcActionStoreTablePrefix),
            resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.JDBC_ACTION_STORE_TABLE_PREFIX,
                jdbcActionStoreTablePrefix));
    }

    @Test
    public void toggleJDBCCommunicationStoreDropTable() throws Exception {
        boolean jdbcCommunicationStoreDropTable =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.JDBC_COMMUNICATION_STORE_DROP_TABLE)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            toggleWithUseJDBCStore(TransactionFixtures.JDBC_COMMUNICATION_STORE_DROP_TABLE, !jdbcCommunicationStoreDropTable),
            resourceVerifier -> {
                resourceVerifier.verifyAttribute(TransactionFixtures.JDBC_COMMUNICATION_STORE_DROP_TABLE, !jdbcCommunicationStoreDropTable);
            });
    }

    @Test
    public void editJDBCCommunicationStoreTablePrefix() throws Exception {
        String jdbcCommunicationStoreTablePrefix = Random.name();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            editWithUseJDBCStore(TransactionFixtures.JDBC_COMMUNICATION_STORE_TABLE_PREFIX,
                jdbcCommunicationStoreTablePrefix),
            resourceVerifier -> resourceVerifier.verifyAttribute(
                TransactionFixtures.JDBC_COMMUNICATION_STORE_TABLE_PREFIX,
                jdbcCommunicationStoreTablePrefix));
    }

    @Test
    public void toggleJDBCStoreStoreDropTable() throws Exception {
        boolean jdbcStateStoreDropTable =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.JDBC_STATE_STORE_DROP_TABLE)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            toggleWithUseJDBCStore(TransactionFixtures.JDBC_STATE_STORE_DROP_TABLE, !jdbcStateStoreDropTable),
            resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.JDBC_STATE_STORE_DROP_TABLE,
                !jdbcStateStoreDropTable));
    }

    @Test
    public void editJDBCStoreStoreTablePrefix() throws Exception {
        String jdbcStateStoreTablePrefix = Random.name();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getJdbcForm(),
            editWithUseJDBCStore(TransactionFixtures.JDBC_STATE_STORE_TABLE_PREFIX,
                jdbcStateStoreTablePrefix),
            resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.JDBC_STATE_STORE_TABLE_PREFIX,
                jdbcStateStoreTablePrefix));
    }

    private Consumer<FormFragment> toggleWithUseJDBCStore(String attributeName, boolean value) {
        return formFragment -> {
            if (value) {
                formFragment.flip(TransactionFixtures.USE_JDBC_STORE, value);
            }
            formFragment.flip(attributeName, value);
            if (value) {
                formFragment.text(TransactionFixtures.JDBC_STORE_DATASOURCE, TransactionFixtures.JDBC_DATASOURCE);
            }
        };
    }

    private Consumer<FormFragment> editWithUseJDBCStore(String attributeName, String value) {
        return formFragment -> {
            formFragment.flip(TransactionFixtures.USE_JDBC_STORE, true);
            formFragment.text(attributeName, value);
            formFragment.text(TransactionFixtures.JDBC_STORE_DATASOURCE, TransactionFixtures.JDBC_DATASOURCE);
        };
    }
}
