package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;

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

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENABLED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.partitionHandlingAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartitionHandlingTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_PART_HANDLING = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING));
        operations.removeIfExists(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING));
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
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
        form = page.getPartitionHandlingForm();
    }

    @Test
    public void create() throws Exception {
        crud.createSingleton(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING), form);
    }

    @Test
    public void remove() throws Exception {
        crud.deleteSingleton(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING), form);
    }

    @Test
    public void editToggleEnabled() throws Exception {
        boolean enabled =
            operations.readAttribute(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING), ENABLED)
                .booleanValue(false);
        crud.update(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PART_HANDLING), form, ENABLED, !enabled);
    }

}
