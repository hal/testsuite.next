package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.jaspi.configuration;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

public abstract class AbstractJASPIConfigurationTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
    }

    protected static void createJASPIConfiguration(String name) throws IOException {
        createJASPIConfigurationWithServerAuthModuleClassName(name, Random.name());
    }

    protected static void createJASPIConfigurationWithServerAuthModuleClassName(String name, String className) throws IOException {
        operations.add(ElytronFixtures.jaspiConfigurationAddress(name), Values.of("server-auth-modules.class-name",
            className)).assertSuccess();
    }

    @Drone
    protected WebDriver browser;

    @Inject
    protected Console console;

    @Inject
    protected CrudOperations crudOperations;

    @Page
    protected ElytronOtherSettingsPage page;
}
