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
import org.jboss.hal.testsuite.page.configuration.UndertowByteBufferPoolPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ByteBufferPoolTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowByteBufferPoolPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String BYTE_BUFFER_POOL_CREATE =
        "byte-buffer-pool-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BYTE_BUFFER_POOL_DELETE =
        "byte-buffer-pool-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BYTE_BUFFER_POOL_EDIT =
        "byte-buffer-pool-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_DELETE));
        operations.add(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_CREATE));
        operations.removeIfExists(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_EDIT));
        operations.removeIfExists(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_DELETE));
    }

    @Test
    public void create() throws Exception {
        page.navigate();
        crudOperations.create(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_CREATE),
            page.getByteBufferPoolTable(), BYTE_BUFFER_POOL_CREATE);
    }

    @Test
    public void remove() throws Exception {
        page.navigate();
        crudOperations.delete(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_DELETE),
            page.getByteBufferPoolTable(), BYTE_BUFFER_POOL_DELETE);
    }

    @Test
    public void updateBufferSize() throws Exception {
        page.navigate();
        page.getByteBufferPoolTable().select(BYTE_BUFFER_POOL_EDIT);
        crudOperations.update(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_EDIT),
            page.getByteBufferPoolForm(), "buffer-size", 10);
    }

    @Test
    public void updateMaxPoolSize() throws Exception {
        page.navigate();
        page.getByteBufferPoolTable().select(BYTE_BUFFER_POOL_EDIT);
        crudOperations.update(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_EDIT),
            page.getByteBufferPoolForm(), "max-pool-size", 10);
    }

    @Test
    public void updateThreadLocalMaxSize() throws Exception {
        page.navigate();
        page.getByteBufferPoolTable().select(BYTE_BUFFER_POOL_EDIT);
        crudOperations.update(UndertowFixtures.byteBufferPoolAddress(BYTE_BUFFER_POOL_EDIT),
            page.getByteBufferPoolForm(), "thread-local-cache-size", 10);
    }
}
