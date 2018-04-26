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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.page.configuration.UndertowFiltersPage;
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
public class ModClusterFilterTest {

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
    private static final String SOCKET_BINDING_DELETE = "socket-binding-delete" + RandomStringUtils.randomAlphanumeric(7);

    private static final String MOD_CLUSTER_FILTER_CREATE =
        "mod-cluster-filter-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String MOD_CLUSTER_FILTER_DELETE =
        "mod-cluster-filter-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        client.apply(new AddSocketBinding(SOCKET_BINDING_CREATE));
        client.apply(new AddSocketBinding(SOCKET_BINDING_DELETE));
        operations.add(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_DELETE),
            Values.of("management-socket-binding", SOCKET_BINDING_DELETE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_CREATE));
        operations.removeIfExists(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_DELETE));
        client.apply(new RemoveSocketBinding(SOCKET_BINDING_DELETE));
        client.apply(new RemoveSocketBinding(SOCKET_BINDING_CREATE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-mod-cluster", "item"));
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_CREATE),
            page.getModClusterFilterTable(), formFragment -> {
                formFragment.text("name", MOD_CLUSTER_FILTER_CREATE);
                formFragment.text("management-socket-binding", SOCKET_BINDING_CREATE);
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute("management-socket-binding", SOCKET_BINDING_CREATE);
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(UndertowFiltersFixtures.modClusterFilterAdress(MOD_CLUSTER_FILTER_DELETE),
            page.getModClusterFilterTable(), MOD_CLUSTER_FILTER_DELETE);
    }
}
