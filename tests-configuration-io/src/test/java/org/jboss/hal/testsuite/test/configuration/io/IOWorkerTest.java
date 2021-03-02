package org.jboss.hal.testsuite.test.configuration.io;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddSocketBinding;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.fixtures.IOFixtures;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.IOWorkerPreviewFragment;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

@RunWith(Arquillian.class)
public class IOWorkerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final Administration administration = new Administration(client);

    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);

    private static final BackupAndRestoreAttributes backup = new BackupAndRestoreAttributes.Builder(Address.subsystem("io")).build();

    private static final String SOCKET_BINDING = "socket-binding-" + Random.name();

    private static final String HTTP_LISTENER = "http-listener-to-be-added-" + Random.name();

    private static final int SOCKET_BINDING_PORT = AvailablePortFinder.getNextAvailableTCPPort();

    @BeforeClass
    public static void setUp() throws CommandFailedException {
        client.apply(backup.backup());
        client.apply(
            new AddSocketBinding.Builder().name(SOCKET_BINDING).port(SOCKET_BINDING_PORT).multicastPort(1).build());
    }

    @AfterClass
    public static void tearDown()
        throws IOException, CommandFailedException, OperationException, TimeoutException, InterruptedException {
        try {
            operations.removeIfExists(UndertowFixtures.httpListenerAddress("default-server", HTTP_LISTENER));
            client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING));
            client.apply(backup.restore());
            administration.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    @Before
    public void initConsole() {
        browser.navigate().refresh();
    }

    /**
     * Test is BusyTaskThreadCount is displayed.
     * See https://issues.jboss.org/browse/HAL-1576
     * @throws IOException
     */
    @Test
    public void checkDisplayOfBusyTaskTreadCount() throws IOException {
        IOWorkerPreviewFragment ioWorkerPreviewFragment = getIOWorkerFragment().preview(IOWorkerPreviewFragment.class);
     try {
         IOWorkerPreviewFragment.ProgressItem busyTaskTreadCount =
                 new IOWorkerPreviewFragment.ProgressItem(ioWorkerPreviewFragment.getBusyTaskThreadCount());
         busyTaskTreadCount.getCurrentValue();

     } catch (NoSuchElementException e) {
         Assert.fail("No number of busy threads is displayed. See HAL-1576.");
     }
    }

    @Test
    public void verifyCorePoolSize() throws IOException, TimeoutException, InterruptedException {
        int taskMaxThreads = Random.number(1, 30);
        IOWorkerPreviewFragment ioWorkerPreviewFragment = getIOWorkerFragment().preview(IOWorkerPreviewFragment.class);
        operations.writeAttribute(IOFixtures.workerAddress(IOFixtures.DEFAULT_IO_WORKER), "task-max-threads", taskMaxThreads);
        administration.reloadIfRequired();
        ioWorkerPreviewFragment.refresh();
        IOWorkerPreviewFragment.ProgressItem corePoolSizeAttribute =
            new IOWorkerPreviewFragment.ProgressItem(ioWorkerPreviewFragment.getCorePoolSize());
        Assert.assertEquals("Newly updated task-max-threads's value should be present in the core pool size column",
            taskMaxThreads, corePoolSizeAttribute.getMaxValue());
    }

    @Test
    public void verifyMaxPoolSize() throws IOException, TimeoutException, InterruptedException {
        int taskMaxThreads = Random.number(1, 30);
        IOWorkerPreviewFragment ioWorkerPreviewFragment = getIOWorkerFragment().preview(IOWorkerPreviewFragment.class);
        operations.writeAttribute(IOFixtures.workerAddress(IOFixtures.DEFAULT_IO_WORKER), "task-max-threads", taskMaxThreads);
        administration.reloadIfRequired();
        ioWorkerPreviewFragment.refresh();
        IOWorkerPreviewFragment.ProgressItem maxPoolSize =
            new IOWorkerPreviewFragment.ProgressItem(ioWorkerPreviewFragment.getMaxPoolSize());
        Assert.assertEquals("Newly updated task-max-threads's value should be present in the max pool size progress column",
            taskMaxThreads, maxPoolSize.getMaxValue());
    }

    @Test
    public void verifyIOThreadCount() throws IOException, TimeoutException, InterruptedException {
        int ioThreads = Random.number(1, 8);
        IOWorkerPreviewFragment ioWorkerPreviewFragment = getIOWorkerFragment().preview(IOWorkerPreviewFragment.class);
        operations.writeAttribute(IOFixtures.workerAddress(IOFixtures.DEFAULT_IO_WORKER), "io-threads", ioThreads);
        administration.reloadIfRequired();
        ioWorkerPreviewFragment.refresh();
        IOWorkerPreviewFragment.ProgressItem ioThreadCount =
            new IOWorkerPreviewFragment.ProgressItem(ioWorkerPreviewFragment.getIoThreadCount());
        Assert.assertEquals("Newly updated io-threads's value should be present in the IO thread count progress column",
            ioThreads, ioThreadCount.getMaxValue());
    }

    @Test
    public void verifyConnections() throws IOException, TimeoutException, InterruptedException {
        FinderFragment finderFragment = getIOWorkerFragment();
        IOWorkerPreviewFragment ioWorkerPreviewFragment = finderFragment.preview(IOWorkerPreviewFragment.class);
        operations.add(UndertowFixtures.httpListenerAddress("default-server", HTTP_LISTENER),
            Values.of("socket-binding", SOCKET_BINDING));
        administration.reloadIfRequired();
        ioWorkerPreviewFragment.refresh();
        List<String> newlyAddedConnections = ioWorkerPreviewFragment.getConnections()
            .stream()
            .map(attributeItem -> attributeItem.getKeyElement().getText())
            .filter(connectionUrl -> connectionUrl.contains("127.0.0.1:" + SOCKET_BINDING_PORT))
            .collect(Collectors.toList());
        Assert.assertEquals("Newly added connections should be present in the list", 1, newlyAddedConnections.size());
    }

    private FinderFragment getIOWorkerFragment() throws IOException {
        FinderPath finderPath = new FinderPath();
        if (ConfigUtils.isDomain()) {
            finderPath
                .append(Ids.DOMAIN_BROWSE_BY, "hosts")
                .append(Ids.HOST, Ids.build("host", ConfigUtils.getDefaultHost()))
                .append(Ids.SERVER, Ids.build(ConfigUtils.getDefaultHost(), ConfigUtils.getDefaultServer()))
                .append(Ids.RUNTIME_SUBSYSTEM, "io");
        } else {
            finderPath = FinderFragment.runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), "io");
        }
        finderPath.append("worker", IOFixtures.DEFAULT_IO_WORKER);
        return console.finder(NameTokens.RUNTIME, finderPath);
    }
}
