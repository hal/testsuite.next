package org.jboss.hal.testsuite.test.configuration.web.services.client.configuration;

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
import org.jboss.hal.testsuite.page.configuration.WebServicesPage;
import org.jboss.hal.testsuite.test.configuration.web.services.WebServicesFixtures;
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
public class PostHandlerChainHandlerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String CLIENT_CONFIGURATION_EDIT =
        "client-configuration-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final WebServicesFixtures.HandlerChain POST_HANDLER_CHAIN_EDIT =
        new WebServicesFixtures.HandlerChain.Builder(CLIENT_CONFIGURATION_EDIT)
            .handlerChainName("pre-handler-chain-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7))
            .clientConfiguration()
            .postHandlerChain()
            .build();

    private static final WebServicesFixtures.Handler POST_HANDLER_CHAIN_HANDLER_CREATE =
        new WebServicesFixtures.Handler.Builder(
            "post-handler-chain-handler-to-be-created-" + RandomStringUtils.randomAlphanumeric(7))
            .className(Random.name())
            .handlerChain(POST_HANDLER_CHAIN_EDIT)
            .build();

    private static final WebServicesFixtures.Handler POST_HANDLER_CHAIN_HANDLER_DELETE =
        new WebServicesFixtures.Handler.Builder(
            "post-handler-chain-handler-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7))
            .className(Random.name())
            .handlerChain(POST_HANDLER_CHAIN_EDIT)
            .build();

    private static final WebServicesFixtures.Handler POST_HANDLER_CHAIN_HANDLER_EDIT =
        new WebServicesFixtures.Handler.Builder(
            "post-handler-chain-handler-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7))
            .className(Random.name())
            .handlerChain(POST_HANDLER_CHAIN_EDIT)
            .build();

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(WebServicesFixtures.clientConfigurationAddress(CLIENT_CONFIGURATION_EDIT));
        operations.add(POST_HANDLER_CHAIN_EDIT.handlerChainAddress());
        createHandler(POST_HANDLER_CHAIN_HANDLER_DELETE);
        createHandler(POST_HANDLER_CHAIN_HANDLER_EDIT);
    }

    private static void createHandler(WebServicesFixtures.Handler handler) throws IOException {
        operations.add(handler.handlerAddress(), Values.of(WebServicesFixtures.CLASS, handler.getClassName()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(WebServicesFixtures.clientConfigurationAddress(CLIENT_CONFIGURATION_EDIT));
        } finally {
            client.close();
        }
    }

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private WebServicesPage page;

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.WEBSERVICES_CLIENT_CONFIG_ITEM);
        page.getClientConfigurationTable().action(CLIENT_CONFIGURATION_EDIT, "Post");
        page.getClientConfigurationHandlerChainTable().action(POST_HANDLER_CHAIN_EDIT.getHandlerChainName(), "Handler");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(
            POST_HANDLER_CHAIN_HANDLER_CREATE.handlerAddress(),
            page.getClientConfigurationHandlerTable(), formFragment -> {
                formFragment.text("name", POST_HANDLER_CHAIN_HANDLER_CREATE.getName());
                formFragment.text(WebServicesFixtures.CLASS, POST_HANDLER_CHAIN_HANDLER_CREATE.getClassName());
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute(WebServicesFixtures.CLASS, POST_HANDLER_CHAIN_HANDLER_CREATE.getClassName());
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(POST_HANDLER_CHAIN_HANDLER_DELETE.handlerAddress(),
            page.getClientConfigurationHandlerTable(), POST_HANDLER_CHAIN_HANDLER_DELETE.getName());
    }

    @Test
    public void editClass() throws Exception {
        page.getClientConfigurationHandlerTable().select(POST_HANDLER_CHAIN_HANDLER_EDIT.getName());
        crudOperations.update(POST_HANDLER_CHAIN_HANDLER_EDIT.handlerAddress(),
            page.getClientConfigurationHandlerForm(), WebServicesFixtures.CLASS);
    }

}
