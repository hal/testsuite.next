package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.transactionAddress;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_TRANSACTION = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION));
        operations.removeIfExists(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
        } finally {
            client.close();
        }
    }

    @Inject private CrudOperations crud;
    @Inject private Console console;
    @Page private ScatteredCachePage page;
    private FormFragment form;

    @Before
    public void initPage() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
        form = page.getTransactionForm();
    }

    @Test
    public void create() throws Exception {
        crud.createSingleton(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), form);
    }

    @Test
    public void remove() throws Exception {
        crud.deleteSingleton(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), form);
    }

    @Test
    public void editLocking() throws Exception {
        String currentLocking =
            operations.readAttribute(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), "locking")
                .stringValue();
        List<String> lockings = new ArrayList<>(Arrays.asList("PESSIMISTIC", "OPTIMISTIC"));
        lockings.remove(currentLocking);
        crud.update(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), form,
            formFragment -> formFragment.select("locking", lockings.get(0)),
            resourceVerifier -> resourceVerifier.verifyAttribute("locking", lockings.get(0)));
    }

    @Test
    public void editMode() throws Exception {
        String currentMode =
            operations.readAttribute(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), "mode")
                .stringValue();
        List<String> modes = new ArrayList<>(Arrays.asList("NONE", "BATCH", "NON_XA", "NON_DURABLE_XA", "FULL_XA"));
        modes.remove(currentMode);
        crud.update(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION), form,
            formFragment -> formFragment.select("mode", modes.get(0)),
            resourceVerifier -> resourceVerifier.verifyAttribute("mode", modes.get(0)));
    }
}
