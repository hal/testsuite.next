package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.jdbc;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fixtures.DataSourceFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.commands.datasources.RemoveDataSource;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATA_SOURCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.BEHIND;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.THROUGH;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.WRITE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.jdbcStoreAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WriteBehaviourTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String DS = "data-source-for-scattered-cache-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));

        client.apply(new AddDataSource.Builder<>(DS).driverName("h2").jndiName(Random.jndiName())
                .connectionUrl(DataSourceFixtures.h2ConnectionUrl(Random.name())).build());

        operations.headers(Values.of(ModelDescriptionConstants.ALLOW_RESOURCE_SERVICE_RESTART, true))
                .add(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), Values.of(DATA_SOURCE, DS));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            client.apply(new RemoveDataSource(DS));
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
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.waitNoNotification();
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
        form = page.getJdbcStoreWriteBehindForm();
    }

    @Test
    public void change1ToWriteBehind() throws Exception {
        page.switchBehaviour();
        new ResourceVerifier(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND),
                client).verifyExists();
    }

    @Test
    public void change2ModificationQueueSize() throws Exception {
        crud.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND), form,
                "modification-queue-size", Random.number());
    }

    @Test
    public void change3ThreadPoolSize() throws Exception {
        crud.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND), form,
                "thread-pool-size", Random.number());
    }

    @Test
    public void change4ToWriteThrough() throws Exception {
        page.switchBehaviour();
        new ResourceVerifier(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, THROUGH),
                client).verifyExists();
    }
}
