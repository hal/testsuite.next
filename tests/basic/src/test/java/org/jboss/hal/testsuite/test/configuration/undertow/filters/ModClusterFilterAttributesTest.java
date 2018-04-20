package org.jboss.hal.testsuite.test.configuration.undertow.filters;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.page.configuration.UndertowFiltersPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.io.IOFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFiltersFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class ModClusterFilterAttributesTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowFiltersPage page;

    private static final String SOCKET_BINDING_CREATE =
        "socket-binding-to-create-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String ADVERTISE_SOCKET_BINDING =
        "advertise-socket-binding-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String MANAGEMENT_SOCKET_BINDING_TO_BE_EDITED =
        "management-socket-binding-to-be-edited" + RandomStringUtils.randomAlphanumeric(7);

    private static final String MOD_CLUSTER_FILTER_EDIT =
        "mod-cluster-filter-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String CLIENT_SSL_CONTEXT_TO_BE_EDITED =
        "client-ssl-context-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String WORKER_TO_BE_EDITED = "worker-to-be-edited" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        client.apply(new AddSocketBinding(SOCKET_BINDING_CREATE));
        client.apply(new AddSocketBinding(MANAGEMENT_SOCKET_BINDING_TO_BE_EDITED));
        client.apply(new AddSocketBinding(ADVERTISE_SOCKET_BINDING));
        operations.add(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            Values.of("management-socket-binding", SOCKET_BINDING_CREATE));
        operations.add(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_EDITED));
        operations.add(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT));
        client.apply(new RemoveSocketBinding(SOCKET_BINDING_CREATE));
        client.apply(new RemoveSocketBinding(ADVERTISE_SOCKET_BINDING));
        client.apply(new RemoveSocketBinding(MANAGEMENT_SOCKET_BINDING_TO_BE_EDITED));
        operations.removeIfExists(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_EDITED));
        operations.removeIfExists(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-mod-cluster", "item"));
        page.getModClusterFilterTable().select(MOD_CLUSTER_FILTER_EDIT);
    }

    @Test
    public void editAdvertiseFrequency() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "advertise-frequency",
            Random.number());
    }

    @Test
    public void editAdvertisePath() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "advertise-path");
    }

    @Test
    public void editAdvertiseProtocol() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "advertise-protocol");
    }

    @Test
    public void editAdvertiseSocketBinding() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "advertise-socket-binding", ADVERTISE_SOCKET_BINDING);
    }

    @Test
    public void editBrokenNodeTimeout() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "broken-node-timeout",
            Random.number());
    }

    @Test
    public void editCachedConnectionsPerThread() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "cached-connections-per-thread",
            Random.number());
    }

    @Test
    public void editConnectionIdleTimeout() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "connection-idle-timeout",
            Random.number());
    }

    @Test
    public void editConnectionsPerThread() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "connections-per-thread",
            Random.number());
    }

    @Test
    public void toggleEnableHttp2() throws Exception {
        boolean enableHttp2 =
            operations.readAttribute(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
                "enable-http2").booleanValue();
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "enable-http2", !enableHttp2);
    }

    @Test
    public void editHealthCheckInterval() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "health-check-interval", Random.number());
    }

    @Test
    public void toggleHttp2EnablePush() throws Exception {
        boolean http2EnablePush =
            operations.readAttribute(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
                "http2-enable-push").booleanValue();
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-enable-push", !http2EnablePush);
    }

    @Test
    public void editHttp2HeaderTableSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-header-table-size", Random.number());
    }

    @Test
    public void editHttp2InitialWindowSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-initial-window-size", Random.number());
    }

    @Test
    public void editHttp2MaxConcurrentStreams() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-max-concurrent-streams", Random.number());
    }

    @Test
    public void editHttp2MaxFrameSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-max-frame-size", Random.number(1, Integer.MAX_VALUE));
    }

    @Test
    public void editHttp2MaxHeaderListSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "http2-max-header-list-size", Random.number());
    }

    @Test
    public void editManagementAccessPredicate() throws Exception {
        String predicate = "secure";
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(),
            "management-access-predicate", predicate);
    }

    @Test
    public void editManagementSocketBinding() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "management-socket-binding", MANAGEMENT_SOCKET_BINDING_TO_BE_EDITED);
    }

    @Test
    public void editMaxAjpPacketSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "max-ajp-packet-size", Random.number());
    }

    @Test
    public void editMaxRequestTime() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "max-request-time", Random.number());
    }

    @Test
    public void editMaxRetries() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "max-retries", Random.number());
    }

    @Test
    public void editRequestQueueSize() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "request-queue-size", Random.number());
    }

    @Test
    public void editSecurityKey() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "security-key");
    }

    @Test
    public void editSSLContext() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "ssl-context", CLIENT_SSL_CONTEXT_TO_BE_EDITED);
    }

    @Test
    public void toggleUseAlias() throws Exception {
        boolean useAlias =
            operations.readAttribute(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
                "use-alias").booleanValue();
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "use-alias", !useAlias);
    }

    @Test
    public void editWorker() throws Exception {
        crudOperations.update(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_EDIT),
            page.getModClusterFilterForm(), "worker", WORKER_TO_BE_EDITED);
    }
}
