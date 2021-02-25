package org.jboss.hal.testsuite.test.configuration.web.services.endpoint.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fixtures.WebServicesFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.WebServicesPage;
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
public class EndpointConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String END_POINT_CONFIGURATION_CREATE =
        "endpoint-configuration-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String END_POINT_CONFIGURATION_EDIT =
        "endpoint-configuration-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String END_POINT_CONFIGURATION_REMOVE =
        "endpoint-configuration-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_EDIT));
        operations.add(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_REMOVE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_CREATE));
            operations.removeIfExists(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_EDIT));
            operations.removeIfExists(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_REMOVE));
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
        console.verticalNavigation().selectPrimary(Ids.WEBSERVICES_ENDPOINT_CONFIG_ITEM);
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_CREATE),
            page.getEndpointConfigurationTable(), END_POINT_CONFIGURATION_CREATE);
    }

    @Test
    public void editProperty() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(Random.name(), Random.name());
        properties.put(Random.name(), Random.name());
        page.getEndpointConfigurationTable().select(END_POINT_CONFIGURATION_EDIT);
        FormFragment endpointConfigurationForm = page.getEndpointConfigurationForm();
        endpointConfigurationForm.edit();
        endpointConfigurationForm.properties("property").add(properties);
        endpointConfigurationForm.save();
        console.verifySuccess();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            new ResourceVerifier(
                WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_EDIT).and("property", entry.getKey()),
                client)
                .verifyExists()
                .verifyAttribute("value", entry.getValue());
        }
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(WebServicesFixtures.endpointConfigurationAddress(END_POINT_CONFIGURATION_REMOVE),
            page.getEndpointConfigurationTable(), END_POINT_CONFIGURATION_REMOVE);
    }

}
