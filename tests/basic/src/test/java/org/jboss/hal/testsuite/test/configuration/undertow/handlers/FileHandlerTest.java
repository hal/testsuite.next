package org.jboss.hal.testsuite.test.configuration.undertow.handlers;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ValueExpression;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowHandlersPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowHandlersFixtures;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class FileHandlerTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowHandlersPage page;

    private static final String TEMP_DEPLOYMENT_CREATE =
        "temp-deployment-" + RandomStringUtils.randomAlphanumeric(7) + ".war";

    private static final String TEMP_DEPLOYMENT_DELETE =
        "temp-deployment-" + RandomStringUtils.randomAlphanumeric(7) + ".war";

    private static final String FILE_HANDLER_CREATE =
        "file-handler-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String FILE_HANDLER_DELETE =
        "file-handler-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        deploy(TEMP_DEPLOYMENT_CREATE);
        deploy(TEMP_DEPLOYMENT_DELETE);
        operations.add(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_DELETE),
            Values.of("path", tempDeploymentPath(TEMP_DEPLOYMENT_DELETE)));
    }

    private static void deploy(String deploymentArchiveName) throws CommandFailedException {
        WebArchive webAppArchive = ShrinkWrap.create(WebArchive.class, deploymentArchiveName)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        Deploy deploy =
            new Deploy.Builder(webAppArchive.as(ZipExporter.class).exportAsInputStream(), webAppArchive.getName(),
                true).build();
        client.apply(deploy);
    }

    private static String tempDeploymentPath(String deploymentArchiveName) {
        return String.format("${jboss.server.temp.dir}/%s", deploymentArchiveName);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_CREATE));
        operations.removeIfExists(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_DELETE));
        client.apply(new Undeploy.Builder(TEMP_DEPLOYMENT_CREATE).build());
        client.apply(new Undeploy.Builder(TEMP_DEPLOYMENT_DELETE).build());
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-file-handler", "item"));
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_CREATE),
            page.getFileHandlerTable(), formFragment -> {
                formFragment.text("name", FILE_HANDLER_CREATE);
                formFragment.text("path", tempDeploymentPath(TEMP_DEPLOYMENT_CREATE));
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute("path",
                    new ModelNode(new ValueExpression(tempDeploymentPath(TEMP_DEPLOYMENT_CREATE))));
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_DELETE),
            page.getFileHandlerTable(), FILE_HANDLER_DELETE);
    }
}
