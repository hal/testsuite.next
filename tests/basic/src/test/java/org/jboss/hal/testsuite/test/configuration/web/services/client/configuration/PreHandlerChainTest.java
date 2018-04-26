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

@RunWith(Arquillian.class)
public class PreHandlerChainTest {
    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private WebServicesPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String CLIENT_CONFIGURATION_EDIT =
        "client-configuration-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final WebServicesFixtures.HandlerChain PRE_HANDLER_CHAIN_CREATE = new WebServicesFixtures.HandlerChain.Builder(CLIENT_CONFIGURATION_EDIT)
        .handlerChainName("pre-handler-chain-to-be-created-" + RandomStringUtils.randomAlphanumeric(7))
        .clientConfiguration()
        .preHandlerChain()
        .build();

    private static final WebServicesFixtures.HandlerChain PRE_HANDLER_CHAIN_EDIT = new WebServicesFixtures.HandlerChain.Builder(CLIENT_CONFIGURATION_EDIT)
        .handlerChainName("pre-handler-chain-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7))
        .clientConfiguration()
        .preHandlerChain()
        .build();

    private static final WebServicesFixtures.HandlerChain PRE_HANDLER_CHAIN_DELETE =
        new WebServicesFixtures.HandlerChain.Builder(CLIENT_CONFIGURATION_EDIT)
            .handlerChainName("pre-handler-chain-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7))
            .clientConfiguration()
            .preHandlerChain()
            .build();





    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(WebServicesFixtures.clientConfigurationAddress(CLIENT_CONFIGURATION_EDIT));
        operations.add(PRE_HANDLER_CHAIN_EDIT.handlerChainAddress());
        operations.add(PRE_HANDLER_CHAIN_DELETE.handlerChainAddress());
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(WebServicesFixtures.clientConfigurationAddress(CLIENT_CONFIGURATION_EDIT));
        } finally {
            client.close();
        }
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.WEBSERVICES_CLIENT_CONFIG_ITEM);
        page.getClientConfigurationTable().action(CLIENT_CONFIGURATION_EDIT, "Pre");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(PRE_HANDLER_CHAIN_CREATE.handlerChainAddress(),
            page.getClientConfigurationHandlerChainTable(), PRE_HANDLER_CHAIN_CREATE.getHandlerChainName());
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(PRE_HANDLER_CHAIN_DELETE.handlerChainAddress(),
            page.getClientConfigurationHandlerChainTable(), PRE_HANDLER_CHAIN_DELETE.getHandlerChainName());
    }

    @Test
    public void editProtocolBindings() throws Exception {
        page.getClientConfigurationHandlerChainTable().select(PRE_HANDLER_CHAIN_EDIT.getHandlerChainName());
        crudOperations.update(PRE_HANDLER_CHAIN_EDIT.handlerChainAddress(),
            page.getClientConfigurationHandlerChainForm(), "protocol-bindings");
    }
}
