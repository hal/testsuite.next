package org.jboss.hal.testsuite.test.configuration.web.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
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
        console.verticalNavigation().selectPrimary(Ids.WEBSERVICES_ITEM);
    }

    @Test
    public void toggleModifyWSDLAddress() throws Exception {
        boolean modifyWSDLAddress =
            operations.readAttribute(WebServicesFixtures.WEB_SERVICES_ADDRESS, "modify-wsdl-address").booleanValue();
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "modify-wsdl-address", !modifyWSDLAddress);
    }

    @Test
    public void toggleStatisticsEnabled() throws Exception {
        boolean statisticsEnabled =
            operations.readAttribute(WebServicesFixtures.WEB_SERVICES_ADDRESS, "statistics-enabled").booleanValue();
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "statistics-enabled", !statisticsEnabled);
    }

    @Test
    public void editWSDLHost() throws Exception {
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "wsdl-host");
    }

    @Test
    public void editWSDLPathRewriteRule() throws Exception {
        String wsdlPathRewriteRule = String.format("s/%s/%s/g", Random.name(), Random.name());
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "wsdl-path-rewrite-rule", wsdlPathRewriteRule);
    }

    @Test
    public void editWSDLPort() throws Exception {
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "wsdl-port", Random.number(0, 65536));
    }

    @Test
    public void editWSDLSecurePort() throws Exception {
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            "wsdl-secure-port", Random.number(0, 65536));
    }

    @Test
    public void editWSDLURIScheme() throws Exception {
        String[] wsdlUriSchemeOptions = {"", "http", "https"};
        String wsdlUriScheme =
            operations.readAttribute(WebServicesFixtures.WEB_SERVICES_ADDRESS, "wsdl-uri-scheme").stringValue("");
        String chosenOption = Arrays.stream(wsdlUriSchemeOptions).filter(option -> !option.equals(wsdlUriScheme))
            .collect(Collectors.toList()).get(Random.number(0,2));
        crudOperations.update(WebServicesFixtures.WEB_SERVICES_ADDRESS, page.getWebServicesConfigurationForm(),
            formFragment -> formFragment.select("wsdl-uri-scheme", chosenOption),
            resourceVerifier -> resourceVerifier.verifyAttribute("wsdl-uri-scheme", chosenOption));
    }
}
