package org.jboss.hal.testsuite.test.configuration.undertow.server.hosts;

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
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class HostsTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowServerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String UNDERTOW_SERVER_TO_BE_TESTED =
        "undertow-server-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String UNDERTOW_HOST_CREATE =
        "undertow-host-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String UNDERTOW_HOST_DELETE =
        "undertow-host-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED).and("host", UNDERTOW_HOST_DELETE), Values
            .of("default-web-module", Random.name()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
    }

    @Before
    public void initPage() {
        page.navigate("name", UNDERTOW_SERVER_TO_BE_TESTED);
        console.verticalNavigation().selectPrimary(Ids.UNDERTOW_HOST_ITEM);
    }

    @Test
    public void create() throws Exception {
        String defaultWebModule = Random.name();
        crudOperations.create(
            UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED).and("host", UNDERTOW_HOST_CREATE),
            page.getHostsTable(), formFragment -> {
                formFragment.text("name", UNDERTOW_HOST_CREATE);
                formFragment.text("default-web-module", defaultWebModule);
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute("default-web-module", defaultWebModule);
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(
            UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED).and("host", UNDERTOW_HOST_DELETE),
            page.getHostsTable(), UNDERTOW_HOST_DELETE);
    }
}
