package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class GlobalSettingsTest {

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowPage page;

    private static final String DEFAULT_SERVER_TO_BE_EDITED =
        "new_default_server_" + RandomStringUtils.randomAlphanumeric(7);
    private static final String DEFAULT_SERVLET_CONTAINER_TO_BE_EDITED =
        "new_default_servlet_container_" + RandomStringUtils.randomAlphanumeric(7);
    private static final String DEFAULT_VIRTUAL_HOST_TO_BE_EDITED =
        "new_default_virtual_host_" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(UndertowFixtures.serverAddress(DEFAULT_SERVER_TO_BE_EDITED));
        operations.add(UndertowFixtures.servletContainerAddress(DEFAULT_SERVLET_CONTAINER_TO_BE_EDITED));
        operations.add(
            UndertowFixtures.virtualHostAddress(DEFAULT_SERVER_TO_BE_EDITED, DEFAULT_VIRTUAL_HOST_TO_BE_EDITED));
    }

    @AfterClass
    public static void tearDown() throws IOException {
        operations.remove(UndertowFixtures.serverAddress(DEFAULT_SERVER_TO_BE_EDITED));
        operations.remove(UndertowFixtures.servletContainerAddress(DEFAULT_SERVLET_CONTAINER_TO_BE_EDITED));
    }

    @Test
    public void updateDefaultSecurityDomain() throws Exception {
        page.navigate();
        crudOperations.update(UndertowFixtures.UNDERTOW_ADDRESS, page.getConfigurationForm(),
            UndertowFixtures.DEFAULT_SECURITY_DOMAIN);
    }

    @Test
    public void updateDefaultServer() throws Exception {
        page.navigate();
        crudOperations.update(UndertowFixtures.UNDERTOW_ADDRESS,
            page.getConfigurationForm(), UndertowFixtures.DEFAULT_SERVER, DEFAULT_SERVER_TO_BE_EDITED);
    }

    @Test
    public void updateDefaultServletContainer() throws Exception {
        page.navigate();
        crudOperations.update(
            UndertowFixtures.UNDERTOW_ADDRESS, page.getConfigurationForm(), UndertowFixtures.DEFAULT_SERVLET_CONTAINER,
            DEFAULT_SERVLET_CONTAINER_TO_BE_EDITED);
    }

    @Test
    public void updateDefaultVirtualHost() throws Exception {
        page.navigate();
        crudOperations.update(UndertowFixtures.UNDERTOW_ADDRESS, page.getConfigurationForm(),
            consumer -> {
                consumer.text(UndertowFixtures.DEFAULT_SERVER, DEFAULT_SERVER_TO_BE_EDITED);
                consumer.text(UndertowFixtures.DEFAULT_VIRTUAL_HOST, DEFAULT_VIRTUAL_HOST_TO_BE_EDITED);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(UndertowFixtures.DEFAULT_VIRTUAL_HOST,
                DEFAULT_VIRTUAL_HOST_TO_BE_EDITED));
    }

    @Test
    public void updateInstanceId() throws Exception {
        page.navigate();
        crudOperations.update(UndertowFixtures.UNDERTOW_ADDRESS, page.getConfigurationForm(),
            UndertowFixtures.INSTANCE_ID);
    }

    @Test
    public void toggleStatisticsEnabled() throws Exception {
        boolean statisticsEnabled =
            operations.readAttribute(UndertowFixtures.UNDERTOW_ADDRESS, UndertowFixtures.STATISTICS_ENABLED)
                .booleanValue();
        page.navigate();
        crudOperations.update(UndertowFixtures.UNDERTOW_ADDRESS, page.getConfigurationForm(),
            UndertowFixtures.STATISTICS_ENABLED, !statisticsEnabled);
    }
}
