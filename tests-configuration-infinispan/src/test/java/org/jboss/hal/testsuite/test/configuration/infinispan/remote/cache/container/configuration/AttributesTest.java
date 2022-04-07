package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container.configuration;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container.AbstractRemoteCacheContainerTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteClusterAddress;

@RunWith(Arquillian.class)
public class AttributesTest extends AbstractRemoteCacheContainerTest {

    private static final String REMOTE_CACHE_CONTAINER_TO_BE_TESTED =
        "remote-cache-container-to-be-tested-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_CLUSTER = "remote-socket-binding-cluster-" + Random.name();

    private static final String REMOTE_CLUSTER = "remote-cluster-" + Random.name();

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException, TimeoutException, InterruptedException {
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_SOCKET_BINDING);
        operations.add(remoteClusterAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER),
            Values.of("socket-bindings",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(REMOTE_SOCKET_BINDING_CLUSTER).build()));
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER));
    }

    @Before
    public void initPage() {
        page.navigate("name", REMOTE_CACHE_CONTAINER_TO_BE_TESTED);
        console.verticalNavigation().selectPrimary("rcc-item");
    }

    @Test
    public void editConnectionTimeout() throws Exception {
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "connection-timeout", Random.number());
    }

    @Test
    public void editDefaultRemoteCluster() throws Exception {
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "default-remote-cluster", REMOTE_CLUSTER);
    }

    @Test
    public void editMaxRetries() throws Exception {
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "max-retries", Random.number());
    }

    @Test
    public void editModule() throws Exception {
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "modules", Collections.singletonList(Random.name()));
    }

    @Test
    public void editProtocolVersion() throws Exception {
        String[] protocolVersions = {"1.0", "1.1", "1.2", "1.3", "2.0", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.7"};
        String protocolVersion = protocolVersions[Random.number(0, protocolVersions.length)];
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), formFragment -> {
                formFragment.select("protocol-version", protocolVersion);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("protocol-version", protocolVersion));
    }

    @Test
    public void ediSocketTimeout() throws Exception {
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "socket-timeout", Random.number());
    }

    @Test
    public void toggleTCPKeepAlive() throws Exception {
        boolean tcpKeepAlive =
            operations.readAttribute(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), "tcp-keep-alive")
                .booleanValue();
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "tcp-keep-alive", !tcpKeepAlive);
    }

    @Test
    public void toggleTCPNoDelay() throws Exception {
        boolean tcpNoDelay =
            operations.readAttribute(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), "tcp-no-delay")
                .booleanValue();
        crudOperations.update(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED),
            page.getConfigurationForm(), "tcp-no-delay", !tcpNoDelay);
    }
}
