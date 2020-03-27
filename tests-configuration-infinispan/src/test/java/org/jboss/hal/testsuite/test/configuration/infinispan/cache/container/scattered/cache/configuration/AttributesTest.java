package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;
import java.util.Collections;

import org.jboss.arquillian.core.api.annotation.Inject;
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
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class AttributesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER)).assertSuccess();
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS)).assertSuccess();
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE)).assertSuccess();
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

    @Before
    public void initPage() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void editBiasLifeSpan() throws Exception {
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(),
            "bias-lifespan", (long) Random.number());
    }

    @Test
    public void editInvalidationBatchSize() throws Exception {
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(),
            "invalidation-batch-size", Random.number());
    }

    @Test
    public void editModules() throws Exception {
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(), "modules", Collections.singletonList(Random.name()));
    }

    @Test
    public void editRemoteTimeout() throws Exception {
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(),
            "remote-timeout", (long) Random.number());
    }

    @Test
    public void editSegments() throws Exception {
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(),
            "segments", Random.number());
    }

    @Test
    public void toggleStatisticsEnabled() throws Exception {
        boolean statisticsEnabled =
            operations.readAttribute(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), "statistics-enabled")
                .booleanValue();
        crud.update(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getConfigurationForm(),
            "statistics-enabled", !statisticsEnabled);
    }
}
