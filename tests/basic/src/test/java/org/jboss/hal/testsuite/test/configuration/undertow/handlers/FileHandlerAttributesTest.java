package org.jboss.hal.testsuite.test.configuration.undertow.handlers;

import java.io.IOException;
import java.util.Arrays;

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
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowHandlersPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowHandlersFixtures;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class FileHandlerAttributesTest {

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

    private static final String TEMP_DEPLOYMENT_EDIT =
        "temp-deployment-" + RandomStringUtils.randomAlphanumeric(7) + ".war";

    private static final String FILE_HANDLER_EDIT =
        "file-handler-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String ERROR_HAL_1449 = "HAL-1449";


    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        deploy(TEMP_DEPLOYMENT_CREATE);
        deploy(TEMP_DEPLOYMENT_EDIT);
        operations.add(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
            Values.of("path", tempDeploymentPath(TEMP_DEPLOYMENT_CREATE)));
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
        operations.removeIfExists(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT));
        client.apply(new Undeploy.Builder(TEMP_DEPLOYMENT_CREATE).build());
        client.apply(new Undeploy.Builder(TEMP_DEPLOYMENT_EDIT).build());
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-file-handler", "item"));
        page.getFileHandlerTable().select(FILE_HANDLER_EDIT);
    }

    @Test
    public void updateCacheBufferSize() throws Exception {
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(), "cache-buffer-size", Random.number());
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }

    @Test
    public void updateCacheBuffers() throws Exception {
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(), "cache-buffers", Random.number());
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }

    @Test
    public void toggleCaseSensitive() throws Exception {
        boolean caseSensitive =
            operations.readAttribute(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT), "case-sensitive")
                .booleanValue();
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(),
                "case-sensitive", !caseSensitive);
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }

    @Test
    public void toggleDirectoryListing() throws Exception {
        boolean directoryListing =
            operations.readAttribute(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT), "directory-listing")
                .booleanValue();
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(),
                "directory-listing", !directoryListing);
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }

    @Test
    public void toggleFollowSymlink() throws Exception {
        boolean followSymlink =
            operations.readAttribute(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT), "follow-symlink")
                .booleanValue();
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(),
                "follow-symlink", !followSymlink);
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }

    @Test
    public void editPath() throws Exception {
        crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT), page.getFileHandlerForm(),
            formFragment -> {
                formFragment.text("path", tempDeploymentPath(TEMP_DEPLOYMENT_EDIT));
            }, resourceVerifier -> resourceVerifier.verifyAttribute("path",
                new ModelNode(new ValueExpression(tempDeploymentPath(TEMP_DEPLOYMENT_EDIT)))));
    }

    @Test
    public void editSafeSymlinkPaths() throws Exception {
        String[] symlinks = {Random.name(), Random.name(), Random.name()};
        try {
            crudOperations.update(UndertowHandlersFixtures.fileHandlerAddress(FILE_HANDLER_EDIT),
                page.getFileHandlerForm(),
                "safe-symlink-paths",
                Arrays.asList(symlinks));
        } catch (TimeoutException e) {
            Assert.fail(ERROR_HAL_1449);
        }
    }
}
