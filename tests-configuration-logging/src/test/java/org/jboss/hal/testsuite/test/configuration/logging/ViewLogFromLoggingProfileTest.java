package org.jboss.hal.testsuite.test.configuration.logging;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.drone.api.annotation.lifecycle.MethodLifecycle;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.DeploymentDrone;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.LogFilePreviewFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class ViewLogFromLoggingProfileTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static String deploymentName;

    private static final String LOGGING_PROFILE = "logging-profile-" + Random.name();
    private static final String FILE_HANDLER = "file-handler-" + Random.name();
    private static final String LOG_FILE = "log-file-" + Random.name() + ".log";
    private static String deploymentUrl;

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        WebArchive archive =
            EmbeddedMaven.forProject(ViewLogFromLoggingProfileTest.class.getResource("pom.xml").getFile())
                .addProperty("loggingProfile", LOGGING_PROFILE).setGoals("package").setBatchMode(true).build().getDefaultBuiltArchive()
                .as(WebArchive.class);
        deploymentName = archive.getName();
        deploymentUrl = String.format("http://%s:8080/%s", System.getProperty("as.managementAddress", "localhost"),
            deploymentName.replaceAll("\\.war", ""));
        File archiveFile = temporaryFolder.newFile(deploymentName);
        archive.as(ZipExporter.class).exportTo(archiveFile, true);
        Deploy deploy = new Deploy.Builder(archiveFile).build();
        ModelNode file = new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("path", LOG_FILE)
            .addProperty("relative-to", "jboss.server.log.dir").build();
        operations.add(LoggingFixtures.LoggingProfile.loggingProfileAddress(LOGGING_PROFILE)).assertSuccess();
        operations.add(LoggingFixtures.LoggingProfile.fileHandlerAddress(LOGGING_PROFILE, FILE_HANDLER),
            Values.of("file", file).and("level", "ALL")).assertSuccess();
        operations.add(LoggingFixtures.LoggingProfile.rootLoggerAddress(LOGGING_PROFILE),
            Values.of("handlers", new ModelNodeGenerator.ModelNodeListBuilder()
                .addAll(FILE_HANDLER).build()).and("level", "ALL")).assertSuccess();
        client.apply(deploy);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            Undeploy undeploy = new Undeploy.Builder(deploymentName).build();
            client.apply(undeploy);
            operations.removeIfExists(LoggingFixtures.LoggingProfile.loggingProfileAddress(LOGGING_PROFILE));
        } finally {
            client.close();
        }
    }

    private static String logFileItemId(String logFile) {
        return logFile.replaceAll("\\.log", "log");
    }

    @Drone
    private WebDriver browser;

    @Drone
    @DeploymentDrone
    @MethodLifecycle
    private WebDriver deploymentBrowser;

    @Inject
    private Console console;

    private FinderFragment finder;

    private ColumnFragment column;

    public void getColumn() throws IOException {
        finder = console.finder(NameTokens.RUNTIME,
            FinderFragment.runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), "logging"));
        column = finder.column("lf");
    }

    @Test
    public void verifyLoggingProfileIsPresentInColumn() throws IOException {
        getColumn();
        Assert.assertTrue("Regular log files should be present in the column alongside log files from logging profiles",
            column.containsItem(logFileItemId("server.log")));
        Assert.assertTrue("Logging profile's log file should be present in the column",
            column.containsItem(logFileItemId(LOG_FILE)));
    }

    @Test
    public void verifyLogFileNameIsHeader() throws IOException {
        getColumn();
        String header = column.selectItem(logFileItemId(LOG_FILE)).getRoot()
            .findElement(By.cssSelector(".item-text > span"))
            .getText();
        Assert.assertEquals("Logging profile's log file should be header of column's item", LOG_FILE, header);
    }

    @Test
    public void verifyLoggingProfileIsColumnsSubtitle() throws IOException {
        getColumn();
        String subtitle = column.selectItem(logFileItemId(LOG_FILE)).getRoot()
            .findElement(By.cssSelector(".item-text > .subtitle"))
            .getText();
        Assert.assertEquals("Logging profile should be the subtitle of column's item", LOGGING_PROFILE, subtitle);
    }

    @Test
    public void verifyLoggingProfileIsInPreview() throws IOException {
        deploymentBrowser.get(deploymentUrl);
        getColumn();
        column.selectItem(logFileItemId(LOG_FILE));
        LogFilePreviewFragment logFilePreview = finder.preview(LogFilePreviewFragment.class);
        Assert.assertEquals("Logging profile should be present in the \"Main Attributes\" of the log file",
            LOGGING_PROFILE, logFilePreview.getMainAttributes().get("Logging Profile"));
        Assert.assertFalse("Log file's preview should not be empty", logFilePreview.preview().isEmpty());
    }
}
