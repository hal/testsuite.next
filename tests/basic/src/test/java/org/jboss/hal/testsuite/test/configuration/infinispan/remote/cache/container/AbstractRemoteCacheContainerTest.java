package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.RemoteCacheContainerPage;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteCacheContainerAddress;

public abstract class AbstractRemoteCacheContainerTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final Administration administration = new Administration(client);

    protected static void createRemoteCacheContainer(String name, String socketBinding) throws IOException {
        String remoteCluster = Random.name();
        operations.batch(
            new Batch().add(remoteCacheContainerAddress(name), Values.of("default-remote-cluster", remoteCluster))
                .add(remoteCacheContainerAddress(name).and("remote-cluster", remoteCluster),
                    Values.of(SOCKET_BINDINGS, new ModelNodeGenerator.ModelNodeListBuilder().addAll(socketBinding).build())))
            .assertSuccess();
    }

    protected static void createRemoteSocketBinding(String name) throws CommandFailedException {
        client.apply(new AddRemoteSocketBinding(name, "localhost", AvailablePortFinder.getNextAvailableTCPPort()));
    }

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Drone
    protected WebDriver browser;

    @Page
    protected RemoteCacheContainerPage page;

    @Inject
    protected Console console;

    @Inject
    protected CrudOperations crudOperations;

}
