package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowBufferCachePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class BufferCachesTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowBufferCachePage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String BUFFER_CACHE_CREATE =
        "buffer-cache-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BUFFER_CACHE_DELETE =
        "buffer-cache-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BUFFER_CACHE_EDIT =
        "buffer-cache-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_DELETE));
        operations.add(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_CREATE));
        operations.removeIfExists(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_EDIT));
        operations.removeIfExists(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_DELETE));
    }

    @Test
    public void create() throws Exception {
        page.navigate();
        crudOperations.create(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_CREATE),
            page.getBufferCacheTable(), BUFFER_CACHE_CREATE);
    }

    @Test
    public void remove() throws Exception {
        page.navigate();
        crudOperations.delete(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_DELETE),
            page.getBufferCacheTable(), BUFFER_CACHE_DELETE);
    }

    @Test
    public void updateBufferSize() throws Exception {
        page.navigate();
        page.getBufferCacheTable().select(BUFFER_CACHE_EDIT);
        crudOperations.update(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_EDIT),
            page.getBufferCacheForm(), "buffer-size", 10);
    }

    @Test
    public void updateBuffersPerRegion() throws Exception {
        page.navigate();
        page.getBufferCacheTable().select(BUFFER_CACHE_EDIT);
        crudOperations.update(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_EDIT),
            page.getBufferCacheForm(), "buffers-per-region", 10);
    }

    @Test
    public void updateMaxRegions() throws Exception {
        page.navigate();
        page.getBufferCacheTable().select(BUFFER_CACHE_EDIT);
        crudOperations.update(UndertowFixtures.bufferCacheAddress(BUFFER_CACHE_EDIT),
            page.getBufferCacheForm(), "max-regions", 10);
    }
}
