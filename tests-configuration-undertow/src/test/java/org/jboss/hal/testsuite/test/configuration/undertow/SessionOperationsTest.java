package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.drone.api.annotation.lifecycle.MethodLifecycle;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.DeploymentDrone;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.runtime.UndertowRuntimePage;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class SessionOperationsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String DEPLOYMENT_NAME = "sample-demo-app-" + Random.name();
    private static final String ARCHIVE_NAME = DEPLOYMENT_NAME + ".war";
    private static final String DEPLOYMENT_URL =
        "http:" + System.getProperty("as.managementAddress", "localhost") + ":8080/" + DEPLOYMENT_NAME;
    private static final Address UNDERTOW_RUNTIME_ADDRESS = Address.deployment(ARCHIVE_NAME).and("subsystem", "undertow");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy, h:mm:ss a");

    private void invalidateSession(String sessionId) throws IOException {
        operations.invoke("invalidate-session", UNDERTOW_RUNTIME_ADDRESS, Values.of("session-id", sessionId));
    }

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        Package resourcePackage = SessionOperationsTest.class.getPackage();
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, ARCHIVE_NAME)
            .addClass(Counter.class)
            .addAsWebInfResource(resourcePackage, "WEB-INF/beans.xml", "beans.xml")
            .addAsWebInfResource(resourcePackage, "WEB-INF/faces-config.xml", "faces-config.xml")
            .addAsWebResource(resourcePackage, "home.xhtml", "home.xhtml")
            .addAsWebResource(resourcePackage, "index.html", "index.html")
            .addAsWebResource(resourcePackage, "template.xhtml", "template.xhtml");
        Deploy deploy =
            new Deploy.Builder(webArchive.as(ZipExporter.class).exportAsInputStream(), ARCHIVE_NAME, true).build();
        client.apply(deploy);
        operations.writeAttribute(UndertowFixtures.UNDERTOW_ADDRESS, "statistics-enabled", true).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws CommandFailedException, IOException {
        try {
            Undeploy undeploy = new Undeploy.Builder(ARCHIVE_NAME).build();
            client.apply(undeploy);
        } finally {
            client.close();
        }
    }

    private static List<String> getSessionsFromModel() throws IOException {
        return operations.invoke("list-sessions", UNDERTOW_RUNTIME_ADDRESS).stringListValue();
    }

    private static Map<String, String> assertAndGetSessionAttributes(String sessionId) throws IOException {
        ModelNodeResult result =
            operations.invoke("list-session-attributes", UNDERTOW_RUNTIME_ADDRESS, Values.of("session-id", sessionId));
        result.assertSuccess();
        return result.value().asList().stream()
            .map(modelNode -> {
                ModelNode objectNode = modelNode.asObject();
                String key = objectNode.keys().iterator().next();
                String value = objectNode.get(key).toString().replaceAll("\"","");
                return new String[]{key, value};
            })
            .collect(Collectors.toMap(keyPair -> keyPair[0], pair -> pair[1]));
    }

    @Drone
    private WebDriver browser;

    @Drone
    @DeploymentDrone
    @MethodLifecycle
    private WebDriver deploymentBrowser;

    @Inject
    private Console console;

    @Page
    private UndertowRuntimePage page;

    private void navigateToDeploymentRuntime() {
        page.navigate("deployment", ARCHIVE_NAME);
        console.verticalNavigation().selectPrimary("undertow-deployment-session-item");

    }

    @SuppressWarnings("unchecked")
    private List<WebElement> getSessionsFromView() {
        if (!page.getSessionsTable().getRoot().findElements(By.cssSelector("tbody > tr > td.dataTables_empty")).isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return page.getSessionsTable().getRoot().findElements(By.cssSelector("tbody > tr"));
    }

    private List<WebElement> getAttributes() {
        return browser.findElements(ByJQuery.selector("h2:contains(\"Attributes\") ~ table > tbody > tr"));
    }

    private void verifyAttributeIsContained(String key, String value, List<WebElement> attributeRows) {
        for (WebElement row : attributeRows) {
            List<WebElement> tableData = row.findElements(By.tagName("td"));
            if (tableData.get(0).getText().equals(key) && tableData.get(1).getText().equals(value)) {
                return;
            }
        }
        Assert.fail("Attribute with key:" + key + " and value: " + value + " is not contained in the Attributes view");
    }

    private void reloadSessions() {
        page.getSessionsTable().button("Reload").click();
    }

    @Test
    public void listSessions() throws IOException {
        navigateToDeploymentRuntime();
        Assert.assertTrue("No sessions should be present in the model before accessing the deployment",
            getSessionsFromModel()
                .isEmpty());
        deploymentBrowser.get(DEPLOYMENT_URL);
        reloadSessions();
        Assert.assertFalse("A session should be present in the model after accessing the deployment",
            getSessionsFromModel()
                .isEmpty());
        Assert.assertFalse("The session table should not be empty after accessing the deployment",
            getSessionsFromView().isEmpty());
        invalidateSession(getSessionsFromModel().get(0));
    }

    @Test
    public void invalidateSession() throws IOException {
        deploymentBrowser.get(DEPLOYMENT_URL);
        navigateToDeploymentRuntime();
        reloadSessions();
        Assert.assertFalse("Session should be present in the model", getSessionsFromModel().isEmpty());
        Assert.assertFalse("Session should be present in the view", getSessionsFromView().isEmpty());
        String sessionId = getSessionsFromModel().get(0);
        page.getSessionsTable().select(sessionId);
        page.getSessionsTable().button("Invalidate session").click();
        console.confirmationDialog().confirm();
        console.verifySuccess();
        Assert.assertTrue("Session should not be present in the model after invalidation",
            getSessionsFromModel().isEmpty());
        Assert.assertTrue("Session should not be present in the view after invalidation",
            getSessionsFromView().isEmpty());
    }

    @Test
    public void verifyAttributes() throws IOException {
        deploymentBrowser.get(DEPLOYMENT_URL);
        navigateToDeploymentRuntime();
        reloadSessions();
        Assert.assertFalse("Session should be present in the model", getSessionsFromModel().isEmpty());
        Assert.assertFalse("Session should be present in the view", getSessionsFromView().isEmpty());
        String sessionId = getSessionsFromModel().get(0);
        page.getSessionsTable().select(sessionId);
        Map<String, String> attributesMap = assertAndGetSessionAttributes(sessionId);
        for (Map.Entry<String, String> keyVal : attributesMap.entrySet()) {
            List<WebElement> tableRows = getAttributes();
            verifyAttributeIsContained(keyVal.getKey(), keyVal.getValue(), tableRows);
        }
        invalidateSession(sessionId);
    }

    @Test
    public void verifyCreationTime() throws IOException {
        navigateToDeploymentRuntime();
        long creationTimeInMillis = System.currentTimeMillis();
        deploymentBrowser.get(DEPLOYMENT_URL);
        reloadSessions();
        String sessionId = getSessionsFromModel().get(0);
        page.getSessionsTable().select(sessionId);
        List<WebElement> selectedRowColumns = page.getSessionsTable().getRoot().findElements(By.cssSelector("tr.selected > td"));
        String creationTime = selectedRowColumns.get(1).getText();
        Calendar creationTimeCalendar = Calendar.getInstance();
        creationTimeCalendar.setTimeInMillis(creationTimeInMillis);
        Assert.assertEquals("Creation time should be matching", DATE_FORMAT.format(creationTimeCalendar.getTime()), creationTime);
        invalidateSession(sessionId);
    }

    @Test
    public void verifyLastAccessedTime() throws IOException, InterruptedException {
        navigateToDeploymentRuntime();
        deploymentBrowser.get(DEPLOYMENT_URL);
        reloadSessions();
        String sessionId = getSessionsFromModel().get(0);
        page.getSessionsTable().select(sessionId);
        long start = System.currentTimeMillis();
        long end = start + TimeUnit.MINUTES.toMillis(1);
        TimeUnit.MINUTES.sleep(1);
        deploymentBrowser.findElement(By.cssSelector("input[value=\"Increment\"")).click();
        reloadSessions();
        List<WebElement> selectedRowColumns = page.getSessionsTable().getRoot().findElements(By.cssSelector("tr.selected > td"));
        String lastAccessedTime = selectedRowColumns.get(2).getText();
        Calendar lastAccessedTimeCalendar = Calendar.getInstance();
        lastAccessedTimeCalendar.setTimeInMillis(end);
        Assert.assertEquals("Last accessed time should be matching", DATE_FORMAT.format(lastAccessedTimeCalendar.getTime()), lastAccessedTime);
        invalidateSession(sessionId);
    }
}
