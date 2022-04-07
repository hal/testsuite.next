package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.logs;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.modules.AddModule;
import org.wildfly.extras.creaper.commands.modules.RemoveModule;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class CustomSecurityEventListenerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CUSTOM_SECURITY_EVENT_LISTENER_MODULE_NAME =
        "org.wildfly.security.examples.custom-security-event-listener";

    private static final String CUSTOM_SECURITY_EVENT_LISTENER_CREATE =
        "custom-security-event-listener-create-" + Random.name();
    private static final String CUSTOM_SECURITY_EVENT_LISTENER_UPDATE =
        "custom-security-event-listener-update-" + Random.name();
    private static final String CUSTOM_SECURITY_EVENT_LISTENER_DELETE =
        "custom-security-event-listener-delete-" + Random.name();
    private static final String CLASS_NAME = "org.wildfly.security.examples.MySecurityEventListener";

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        System.setProperty("jboss.home.dir", Optional.ofNullable(System.getenv("JBOSS_HOME")).orElseThrow(() -> new RuntimeException("JBOSS_HOME variable not set")));
        JavaArchive archive = EmbeddedMaven.forProject(
            CustomSecurityEventListenerTest.class.getResource("custom-security-event-listener/pom.xml").getFile())
            .setGoals("package").setBatchMode(true).build().getDefaultBuiltArchive().as(JavaArchive.class);
        File archiveFile = temporaryFolder.newFile(archive.getName());
        archive.as(ZipExporter.class).exportTo(archiveFile, true);
        AddModule addModule = new AddModule.Builder(CUSTOM_SECURITY_EVENT_LISTENER_MODULE_NAME)
            .dependency("org.wildfly.security.elytron")
            .resource(archiveFile)
            .build();
        client.apply(addModule);
        createCustomSecurityEventListener(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE);
        createCustomSecurityEventListener(CUSTOM_SECURITY_EVENT_LISTENER_DELETE);
    }

    private static void createCustomSecurityEventListener(String name) throws IOException {
        operations.add(ElytronFixtures.customSecurityEventListenerAddress(name),
            Values.of("module", CUSTOM_SECURITY_EVENT_LISTENER_MODULE_NAME)
                .and("class-name", CLASS_NAME)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        try {
            operations.removeIfExists(
                ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_CREATE));
            operations.removeIfExists(
                ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE));
            operations.removeIfExists(
                ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_DELETE));
            RemoveModule removeModule = new RemoveModule(CUSTOM_SECURITY_EVENT_LISTENER_MODULE_NAME);
            client.apply(removeModule);
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Page
    private ElytronOtherSettingsPage page;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Before
    public void before() {
        page.navigate();
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.LOGS_ITEM, ElytronFixtures.CUSTOM_SECURITY_EVENT_LISTENER_ITEM);
    }

    @Test
    public void add() throws Exception {
        crudOperations.create(ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_CREATE),
            page.getCustomSecurityEventListenerTable(), formFragment -> {
                formFragment.text("name", CUSTOM_SECURITY_EVENT_LISTENER_CREATE);
                formFragment.text("class-name", CLASS_NAME);
                formFragment.text("module", CUSTOM_SECURITY_EVENT_LISTENER_MODULE_NAME);
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_DELETE),
            page.getCustomSecurityEventListenerTable(), CUSTOM_SECURITY_EVENT_LISTENER_DELETE);
    }

    @Test
    public void editModule() throws Exception {
        page.getCustomSecurityEventListenerTable().select(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE);
        crudOperations.update(ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE),
            page.getCustomSecurityEventListenerForm(), "module");
    }

    @Test
    public void editClassName() throws Exception {
        page.getCustomSecurityEventListenerTable().select(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE);
        crudOperations.update(ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE),
            page.getCustomSecurityEventListenerForm(), "class-name");
    }

    @Test
    public void editConfiguration() throws Exception {
        String key = Random.name();
        String value = Random.name();
        page.getCustomSecurityEventListenerTable().select(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE);
        crudOperations.update(ElytronFixtures.customSecurityEventListenerAddress(CUSTOM_SECURITY_EVENT_LISTENER_UPDATE),
            page.getCustomSecurityEventListenerForm(), form -> {
                form.properties("configuration").add(key, value);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("configuration",
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(key, value).build()));
    }
}
