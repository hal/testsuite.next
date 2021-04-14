package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ServerFinderTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String UNDERTOW_SERVER_TO_BE_ADDED =
        "undertow-server-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String UNDERTOW_SERVER_TO_BE_REMOVED =
        "undertow-server-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String UNDERTOW_SERVER_TO_BE_VIEWED =
        "undertow-server-to-be-viewed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String UNDERTOW_SERVER_TO_BE_REFRESHED =
        "undertow-server-to-be-refreshed-" + RandomStringUtils.randomAlphanumeric(7);

    private ColumnFragment undertowServerColumn;

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_REMOVED));
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_VIEWED));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_ADDED));
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_REMOVED));
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_VIEWED));
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_REFRESHED));
    }

    @Before
    public void initColumn() {
        undertowServerColumn =
            console.finder(NameTokens.CONFIGURATION, FinderFragment.configurationSubsystemPath(NameTokens.UNDERTOW)
                .append(Ids.UNDERTOW_SETTINGS, "server"))
                .column(Ids.UNDERTOW_SERVER);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment addUndertowServerDialog = undertowServerColumn.add();
        FormFragment undertowServerForm = addUndertowServerDialog.getForm();
        undertowServerForm.text("name", UNDERTOW_SERVER_TO_BE_ADDED);
        addUndertowServerDialog.add();
        console.verifySuccess();
        Assert.assertTrue("New created undertow server should be present in the column",
            undertowServerColumn.containsItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_ADDED)));
        new ResourceVerifier(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_ADDED), client).verifyExists();
    }

    @Test
    public void remove() throws Exception {
        Assert.assertTrue("Undertow server to be removed should be present in the column before removal",
            undertowServerColumn.containsItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_REMOVED)));
        undertowServerColumn.selectItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_REMOVED)).dropdown().click("Remove"
        );
        console.confirmationDialog().confirm();
        Assert.assertFalse("Freshly removed undertow server should not be in the column anymore",
            undertowServerColumn.containsItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_REMOVED)));
        new ResourceVerifier(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_REMOVED), client).verifyDoesNotExist();
    }

    @Test
    public void refresh() throws IOException {
        Assert.assertFalse("Undertow server should not be in the column before refreshing",
            undertowServerColumn.containsItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_REFRESHED)));
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_REFRESHED));
        undertowServerColumn.refresh();
        Assert.assertTrue("Undertow server should be present in the column after refresh",
            undertowServerColumn.containsItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_REFRESHED)));
    }

    @Test
    public void view() {
        undertowServerColumn.selectItem(getUndertowServerId(UNDERTOW_SERVER_TO_BE_VIEWED)).view();
        console.verify(new PlaceRequest.Builder()
            .nameToken(Ids.UNDERTOW_SERVER)
            .with("name", UNDERTOW_SERVER_TO_BE_VIEWED)
            .build());

    }

    private static String getUndertowServerId(String undertowServer) {
        return Ids.build("us", undertowServer);
    }
}
