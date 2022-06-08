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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class HttpInvokerTest {

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

    private static final Address UNDERTOW_DEFAULT_HOST_HTTP_INVOKER_ADDRESS =
        UndertowFixtures.serverAddress(UndertowFixtures.DEFAULT_SERVER)
            .and("host", UndertowFixtures.DEFAULT_HOST)
            .and("setting", "http-invoker");

    private static final String HTTP_AUTHENTICATION_FACTORY_TO_BE_CREATED =
        "http-authentication-factory-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        operations.add(ElytronFixtures.httpAuthenticationFactoryAddress(HTTP_AUTHENTICATION_FACTORY_TO_BE_CREATED),
            Values.of("security-domain", "ApplicationDomain").and("http-server-mechanism-factory", "global"));
        backup = new BackupAndRestoreAttributes.Builder(UNDERTOW_DEFAULT_HOST_HTTP_INVOKER_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException, OperationException {
        client.apply(backup.restore());
        operations.removeIfExists(
            ElytronFixtures.httpAuthenticationFactoryAddress(HTTP_AUTHENTICATION_FACTORY_TO_BE_CREATED));
    }

    @Before
    public void initPage() {
        page.navigate("name", UndertowFixtures.DEFAULT_SERVER);
        console.verticalNavigation().selectPrimary(Ids.UNDERTOW_HOST_ITEM);
        page.getHostsTable().select(UndertowFixtures.DEFAULT_HOST);
    }

    @Test
    public void updateHttpAuthenticationFactory() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_HTTP_INVOKER_ADDRESS, page.getHostsHttpInvokerForm(),
            formFragment -> {
                formFragment.text("http-authentication-factory", HTTP_AUTHENTICATION_FACTORY_TO_BE_CREATED);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("http-authentication-factory",
                HTTP_AUTHENTICATION_FACTORY_TO_BE_CREATED));
    }

    @Test
    public void updatePath() throws Exception {
        crudOperations.update(UNDERTOW_DEFAULT_HOST_HTTP_INVOKER_ADDRESS, page.getHostsHttpInvokerForm(), "path");
    }
}
