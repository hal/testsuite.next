package org.jboss.hal.testsuite.test.configuration.undertow.server.hosts;

import java.util.Collections;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class AttributesTest {

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

    private static final Address UNDERTOW_DEFAULT_HOST_ADDRESS =
        UndertowFixtures.serverAddress(UndertowFixtures.DEFAULT_SERVER)
            .and("host", UndertowFixtures.DEFAULT_HOST);

    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void setUp() throws CommandFailedException {
        backup = new BackupAndRestoreAttributes.Builder(UNDERTOW_DEFAULT_HOST_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException {
        client.apply(backup.restore());
    }

    @Before
    public void initPage() {
        page.navigate("name", UndertowFixtures.DEFAULT_SERVER);
        console.verticalNavigation().selectPrimary(Ids.UNDERTOW_HOST_ITEM);
        page.getHostsTable().select(UndertowFixtures.DEFAULT_HOST);
    }

    @Test
    public void updateAlias() throws Exception {
        crudOperations.update(
            UNDERTOW_DEFAULT_HOST_ADDRESS,
            page.getHostsForm(), "alias", Collections.singletonList(Random.name()));
    }

    @Test
    public void updateDefaultWebModule() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ADDRESS, page.getHostsForm(), "default-web-module");
    }

    @Test
    public void updateDefaultResponseCode() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ADDRESS, page.getHostsForm(), "default-response-code",
            Random.number(400, 599));
    }

    @Test
    public void toggleDisableConsoleRedirect() throws Exception {
        boolean disableConsoleRedirect =
            operations.readAttribute(UNDERTOW_DEFAULT_HOST_ADDRESS, "disable-console-redirect").booleanValue();
        crudOperations.update(UNDERTOW_DEFAULT_HOST_ADDRESS, page.getHostsForm(), "disable-console-redirect",
            !disableConsoleRedirect);
    }
}
