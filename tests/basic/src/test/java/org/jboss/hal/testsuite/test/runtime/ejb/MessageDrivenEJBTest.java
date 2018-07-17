package org.jboss.hal.testsuite.test.runtime.ejb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.runtime.ejb.EJBPage;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageDrivenEJB;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageDrivenEJBExecutionTime;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageDrivenEJBInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageDrivenEJBPeakConcurrentInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageDrivenEJBWithRoles;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageServlet;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageServletExecutionTime;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageServletInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageServletPeakConcurrentInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.MessageServletWithRoles;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

@RunWith(Arquillian.class)
public class MessageDrivenEJBTest extends AbstractEJBTest {

    private static final String MESSAGE_DRIVEN_EJB_DEPLOYMENT_NAME = deploymentName("message-driven-");

    private static final EJBDeployment MESSAGE_DRIVEN_EJB_DEPLOYMENT =
        new EJBDeployment(MESSAGE_DRIVEN_EJB_DEPLOYMENT_NAME,
            MessageDrivenEJB.class, EJBType.MESSAGE_DRIVEN, MessageServlet.class);

    private static final String MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT_NAME =
        deploymentName("message-driven-with-roles-");

    private static final EJBDeployment MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT =
        new EJBDeployment(MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT_NAME,
            MessageDrivenEJBWithRoles.class, EJBType.MESSAGE_DRIVEN, MessageServletWithRoles.class);

    private static final String MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT_NAME =
        deploymentName("message-driven-invocations-");

    private static final EJBDeployment MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT_NAME,
            MessageDrivenEJBInvocations.class, EJBType.MESSAGE_DRIVEN, MessageServletInvocations.class);

    private static final String MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT_NAME =
        deploymentName("message-driven-invocations-");

    private static final EJBDeployment MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT_NAME,
            MessageDrivenEJBPeakConcurrentInvocations.class, EJBType.MESSAGE_DRIVEN,
            MessageServletPeakConcurrentInvocations.class);

    private static final String MESSAGE_DRIVEN_EJB_DEPLOYMENT_EXECUTION_TIME_NAME =
        deploymentName("message-driven-execution-time-");

    private static final EJBDeployment MESSAGE_DRIVEN_EJB_EXECUTION_TIME_DEPLOYMENT =
        new EJBDeployment(MESSAGE_DRIVEN_EJB_DEPLOYMENT_EXECUTION_TIME_NAME,
            MessageDrivenEJBExecutionTime.class, EJBType.MESSAGE_DRIVEN, MessageServletExecutionTime.class);

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        enableStatistics();
        MESSAGE_DRIVEN_EJB_DEPLOYMENT.deploy();
        MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT.deploy();
        MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT.deploy();
        MESSAGE_DRIVEN_EJB_EXECUTION_TIME_DEPLOYMENT.deploy();
        MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.deploy();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        try {
            MESSAGE_DRIVEN_EJB_DEPLOYMENT.undeploy();
            MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT.undeploy();
            MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT.undeploy();
            MESSAGE_DRIVEN_EJB_EXECUTION_TIME_DEPLOYMENT.undeploy();
            MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.undeploy();
        } finally {
            client.close();
        }
    }

    @Inject
    private Console console;

    @Drone
    private WebDriver webDriver;

    @Page
    private EJBPage page;

    @Test
    public void view() throws IOException {
        initPage(console);
        ejbColumnFragment.selectItem(MESSAGE_DRIVEN_EJB_DEPLOYMENT.toColumnId()).view();
        console.verify(MESSAGE_DRIVEN_EJB_DEPLOYMENT.getPlaceRequest());
    }

    @Test
    public void verifyComponentClassName() throws Exception {
        console.navigate(MESSAGE_DRIVEN_EJB_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Component class name in the form should be equal to the EJB's class name",
            MessageDrivenEJB.class.getSimpleName(),
            page.getMessageDrivenBeanEJBForm().value("component-class-name"));
        new ResourceVerifier(
            EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_DEPLOYMENT_NAME, MessageDrivenEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("component-class-name", MessageDrivenEJB.class.getSimpleName());
    }

    @Test
    public void verifyDeclaredRoles() throws Exception {
        console.navigate(MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's declared roles",
            String.join(", ", EJBFixtures.ROLE_1, EJBFixtures.ROLE_2),
            page.getMessageDrivenBeanEJBForm().value("declared-roles"));
        new ResourceVerifier(
            EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT_NAME,
                MessageDrivenEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("declared-roles",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(EJBFixtures.ROLE_1, EJBFixtures.ROLE_2).build());
    }

    @Test
    public void verifyExecutionTime() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, MESSAGE_DRIVEN_EJB_EXECUTION_TIME_DEPLOYMENT);
        console.navigate(MESSAGE_DRIVEN_EJB_EXECUTION_TIME_DEPLOYMENT.getPlaceRequest());
        int actualExecutionTime = page.getMessageDrivenBeanEJBForm().intValue("execution-time");
        Assert.assertTrue("Execution time in the form should be close to the total execution time of the EJB deployment",
            numberOfInvocations * 5000 <= actualExecutionTime);
        new ResourceVerifier(EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_DEPLOYMENT_EXECUTION_TIME_NAME,
            MessageDrivenEJBExecutionTime.class),
            client)
            .verifyExists()
            .verifyAttribute("execution-time", Long.valueOf(actualExecutionTime));
    }

    @Test
    public void verifyInvocations() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT);
        console.navigate(MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals(
            "Number of invocations in the form should be equal to the total number of invocations executed",
            numberOfInvocations,
            page.getMessageDrivenBeanEJBForm().intValue("invocations"));
        new ResourceVerifier(
            EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_INVOCATIONS_DEPLOYMENT_NAME,
                MessageDrivenEJBInvocations.class),
            client)
            .verifyExists()
            .verifyAttribute("invocations", Long.valueOf(numberOfInvocations));
    }

    @Test
    public void verifyPeakConcurrentInvocations() throws Exception {
        int numberOfConcurrentInvocations = Random.number(2, 5);
        Runnable invocationTask = () -> invoke(1, MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT);
        for (int i = 0; i < numberOfConcurrentInvocations; ++i) {
            new Thread(invocationTask).start();
        }
        console.navigate(MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Number of peak concurrent invocations should be correct", numberOfConcurrentInvocations,
            page.getMessageDrivenBeanEJBForm().intValue("peak-concurrent-invocations"));
        new ResourceVerifier(
            EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT_NAME,
                MessageDrivenEJBPeakConcurrentInvocations.class), client)
            .verifyExists()
            .verifyAttribute("peak-concurrent-invocations", Long.valueOf(numberOfConcurrentInvocations));
    }

    @Test
    public void verifyRunAsRole() throws Exception {
        console.navigate(MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's run as role", EJBFixtures.ROLE_1,
            page.getMessageDrivenBeanEJBForm().value("run-as-role"));
        new ResourceVerifier(
            EJBFixtures.messageDrivenEJBAddress(MESSAGE_DRIVEN_EJB_WITH_ROLES_DEPLOYMENT_NAME,
                MessageDrivenEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("run-as-role", EJBFixtures.ROLE_1);
    }

    private static void invoke(int numberOfInvocations, EJBDeployment ejbDeployment) {
        String deploymentName = ejbDeployment.getDeploymentName().substring(0,
            ejbDeployment.getDeploymentName().indexOf(".war"));
        HttpGet httpGet;
        try {
            URL url = new URL("http://0.0.0.0:8080/" + deploymentName + "/MessageDriven");
            httpGet = new HttpGet(url.toURI());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Invalid URL", e);
        }
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            for (int i = 0; i < numberOfInvocations; ++i) {
                EntityUtils.consumeQuietly(httpClient.execute(httpGet).getEntity());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
