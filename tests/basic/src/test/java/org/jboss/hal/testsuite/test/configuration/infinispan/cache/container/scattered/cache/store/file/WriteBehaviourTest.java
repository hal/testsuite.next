package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.file;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
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
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALLOW_RESOURCE_SERVICE_RESTART;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WriteBehaviourTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void init() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.headers(Values.of(ALLOW_RESOURCE_SERVICE_RESTART, true))
            .add(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE));
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
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.waitNoNotification();
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
        page.getFileStoreTab().select(ScatteredCachePage.FILE_STORE_WRITE_BEHAVIOUR_TAB);
        form = page.getFileStoreWriteBehindForm();
    }

    @Test
    public void change1ToWriteBehind() throws Exception {
        console.waitNoNotification();
        page.switchBehaviour();
        new ResourceVerifier(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND),
                client).verifyExists();
    }

    @Test
    public void change2ModificationQueueSize() throws Exception {
        console.waitNoNotification();
        crud.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND), form,
                "modification-queue-size", Random.number());
    }

    @Test
    public void change3ThreadPoolSize() throws Exception {
        console.waitNoNotification();
        crud.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND), form,
                "thread-pool-size", Random.number());
    }

    @Test
    public void change4ToWriteThrough() throws Exception {
        console.waitNoNotification();
        page.switchBehaviour();
        new ResourceVerifier(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, THROUGH),
                client).verifyExists();
    }
}
