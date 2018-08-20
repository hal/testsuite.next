package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.backup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.backupAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class BackupsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String BACKUP_CREATE =
        "scattered-cache-with-backup-to-be-created-" + Random.name();
    private static final String BACKUP_DELETE =
        "scattered-cache-with-backup-to-be-deleted-" + Random.name();
    private static final String BACKUP_EDIT =
        "scattered-cache-with-backup-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.add(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_DELETE));
        operations.add(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    @Inject
    private Console console;

    @Page
    private ScatteredCachePage page;

    @Before
    public void navigateToTransactionForm() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.verticalNavigation().selectPrimary("scattered-cache-backup-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(
            backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_CREATE),
            page.getBackupsTable(), BACKUP_CREATE);
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_DELETE), page.getBackupsTable(),
            BACKUP_DELETE);
    }

    @Test
    public void editAfterFailures() throws Exception {
        page.getBackupsTable().select(BACKUP_EDIT);
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            "after-failures", Random.number());
    }

    @Test
    public void toggleEnabled() throws Exception {
        page.getBackupsTable().select(BACKUP_EDIT);
        boolean enabled =
            operations.readAttribute(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), "enabled")
                .booleanValue();
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            "enabled", !enabled);
    }

    @Test
    public void editFailurePolicy() throws Exception {
        String currentFailurePolicy =
            operations.readAttribute(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), "failure-policy")
                .stringValue();
        List<String> failurePolicies = new ArrayList<>(Arrays.asList("IGNORE", "FAIL", "WARN", "CUSTOM"));
        failurePolicies.remove(currentFailurePolicy);
        String failurePolicy = failurePolicies.get(Random.number(0, failurePolicies.size()));
        page.getBackupsTable().select(BACKUP_EDIT);
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            formFragment -> formFragment.select("failure-policy", failurePolicy),
            resourceVerifier -> resourceVerifier.verifyAttribute("failure-policy", failurePolicy));
    }

    @Test
    public void editMinWait() throws Exception {
        page.getBackupsTable().select(BACKUP_EDIT);
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            "min-wait", (long) Random.number());
    }

    @Test
    public void editStrategy() throws Exception {
        String currentStrategy =
            operations.readAttribute(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), "strategy")
                .stringValue();
        List<String> strategies = new ArrayList<>(Arrays.asList("SYNC", "ASYNC"));
        strategies.remove(currentStrategy);
        String strategy = strategies.get(0);
        page.getBackupsTable().select(BACKUP_EDIT);
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            formFragment -> formFragment.select("strategy", strategy),
            resourceVerifier -> resourceVerifier.verifyAttribute("strategy", strategy));
    }

    @Test
    public void editTimeout() throws Exception {
        page.getBackupsTable().select(BACKUP_EDIT);
        crudOperations.update(backupAddress(CACHE_CONTAINER, SCATTERED_CACHE, BACKUP_EDIT), page.getBackupsForm(),
            "min-wait", (long) Random.number());
    }
}
