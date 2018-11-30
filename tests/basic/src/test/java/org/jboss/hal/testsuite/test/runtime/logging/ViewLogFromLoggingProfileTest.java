package org.jboss.hal.testsuite.test.runtime.logging;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures;
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
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class ViewLogFromLoggingProfileTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String deploymentName;

    private static final String LOGGING_PROFILE = "logging-profile-" + Random.name();

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        WebArchive archive = EmbeddedMaven.forProject(ViewLogFromLoggingProfileTest.class.getResource("pom.xml").getFile())
            .addProperty("loggingProfile", LOGGING_PROFILE).setGoals("package").build().getDefaultBuiltArchive()
            .as(WebArchive.class);
        deploymentName = archive.getName();
        File archiveFile = temporaryFolder.newFile(deploymentName);
        archive.as(ZipExporter.class).exportTo(archiveFile, true);
        Deploy deploy = new Deploy.Builder(archiveFile).build();
        operations.add(LoggingFixtures.LoggingProfile.loggingProfileAddress(LOGGING_PROFILE));
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

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private LoggingSubsystemConfigurationPage page;

    @Test
    public void test() {
        page.navigate();
        Assert.assertTrue(true);
        System.out.println("Woohoo");
    }


}
