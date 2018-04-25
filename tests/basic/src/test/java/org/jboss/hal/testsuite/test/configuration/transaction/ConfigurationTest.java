package org.jboss.hal.testsuite.test.configuration.transaction;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ConfigurationTest {

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

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("tx", "attributes", "config", "item"));
    }

    @Test
    public void editDefaultTimeout() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(),
            TransactionFixtures.DEFAULT_TIMEOUT,
            Random.number());
    }

    @Test
    public void toggleEnableTsmStatus() throws Exception {
        boolean enableTsmStatus =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.ENABLE_TSM_STATUS)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(),
            TransactionFixtures.ENABLE_TSM_STATUS,
            !enableTsmStatus);
    }

    @Test
    public void toggleJournalStoreEnableAsyncIO() throws Exception {
        boolean journalStoreEnableAsyncIO =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.JOURNAL_STORE_ENABLE_ASYNC_IO)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(), formFragment -> {
            formFragment.flip(TransactionFixtures.USE_JOURNAL_STORE, !journalStoreEnableAsyncIO);
            formFragment.flip(TransactionFixtures.JOURNAL_STORE_ENABLE_ASYNC_IO, !journalStoreEnableAsyncIO);
        }, resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.JOURNAL_STORE_ENABLE_ASYNC_IO,
            !journalStoreEnableAsyncIO));
    }

    @Test
    public void toggleJTS() throws Exception {
        boolean jts = operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.JTS).booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(),
            TransactionFixtures.JTS,
            !jts);
    }

    @Test
    public void editNodeIdentifier() throws Exception {
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(), "node-identifier",
            Random.name());
    }

    @Test
    public void toggleStatisticsEnabled() throws Exception {
        boolean statisticsEnabled =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATISTICS_ENABLED).booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(),
            TransactionFixtures.STATISTICS_ENABLED,
            !statisticsEnabled);
    }

    @Test
    public void toggleUseJournalStore() throws Exception {
        boolean useJournalStore =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.USE_JOURNAL_STORE).booleanValue();
        boolean journalStoreEnableAsyncIO =
            operations.readAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS,
                TransactionFixtures.JOURNAL_STORE_ENABLE_ASYNC_IO)
                .booleanValue();
        crudOperations.update(TransactionFixtures.TRANSACTIONS_ADDRESS, page.getConfigurationForm(), formFragment -> {
            formFragment.flip(TransactionFixtures.USE_JOURNAL_STORE, !useJournalStore);
            if (journalStoreEnableAsyncIO) {
                formFragment.flip(TransactionFixtures.JOURNAL_STORE_ENABLE_ASYNC_IO, false);
            }
        }, resourceVerifier -> resourceVerifier.verifyAttribute(TransactionFixtures.USE_JOURNAL_STORE, !useJournalStore));
    }
}
