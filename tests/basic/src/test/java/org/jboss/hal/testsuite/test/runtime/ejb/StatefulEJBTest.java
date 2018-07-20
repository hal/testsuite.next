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
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.RemoteEJBInterface;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulEJBExecutionTime;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulEJBInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulEJBPeakInvocations;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulEJBWithRoles;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulGreeterEJB;
import org.jboss.hal.testsuite.test.runtime.ejb.ejb.StatefulServlet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

@RunWith(Arquillian.class)
public class StatefulEJBTest extends AbstractEJBTest {

    private static final String STATEFUL_EJB_DEPLOYMENT_NAME = deploymentName("stateful-");
    private static final String STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_NAME = deploymentName("stateful-with-roles-");
    private static final String STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_NAME = deploymentName("stateful-invocations-");
    private static final String STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME =
        deploymentName("stateful-peak-concurrent-invocations-");
    private static final String STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_NAME =
        deploymentName("stateful-execution-time-");

    private static final EJBDeployment STATEFUL_EJB_DEPLOYMENT = new EJBDeployment(STATEFUL_EJB_DEPLOYMENT_NAME,
        StatefulGreeterEJB.class, EJBType.STATEFUL, StatefulServlet.class, RemoteEJBInterface.class);
    private static final EJBDeployment STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT =
        new EJBDeployment(STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatefulEJBWithRoles.class, EJBType.STATEFUL,
            StatefulServlet.class, RemoteEJBInterface.class);
    private static final EJBDeployment STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT =
        new EJBDeployment(STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_NAME, StatefulEJBExecutionTime.class, EJBType.STATEFUL,
            StatefulServlet.class, RemoteEJBInterface.class);
    private static final EJBDeployment STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_NAME, StatefulEJBInvocations.class, EJBType.STATEFUL,
            StatefulServlet.class, RemoteEJBInterface.class);
    private static final EJBDeployment STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME, StatefulEJBPeakInvocations.class,
            EJBType.STATEFUL,
            StatefulServlet.class, RemoteEJBInterface.class);

    @BeforeClass
    public static void setUp() throws CommandFailedException {
        STATEFUL_EJB_DEPLOYMENT.deploy();
        STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.deploy();
        STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.deploy();
        STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.deploy();
        STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.deploy();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        STATEFUL_EJB_DEPLOYMENT.undeploy();
        STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.undeploy();
        STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.undeploy();
        STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.undeploy();
        STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.undeploy();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private EJBPage page;

    @Test
    public void view() throws IOException {
        initPage(console);
        ejbColumnFragment.selectItem(STATEFUL_EJB_DEPLOYMENT.toColumnId()).view();
        console.verify(STATEFUL_EJB_DEPLOYMENT.getPlaceRequest());
    }

    @Test
    public void verifyComponentClassName() throws Exception {
        console.navigate(STATEFUL_EJB_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Component class name in the form should be equal to the EJB's class name",
            StatefulGreeterEJB.class.getSimpleName(),
            page.getStatefulEJBForm().value("component-class-name"));
        new ResourceVerifier(EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_NAME, StatefulGreeterEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("component-class-name", StatefulGreeterEJB.class.getSimpleName());
    }

    @Test
    public void verifyDeclaredRoles() throws Exception {
        console.navigate(STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's declared roles",
            String.join(", ", EJBFixtures.ROLE_1, EJBFixtures.ROLE_2),
            page.getStatefulEJBForm().value("declared-roles"));
        new ResourceVerifier(
            EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatefulEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("declared-roles",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(EJBFixtures.ROLE_1, EJBFixtures.ROLE_2).build());
    }

    @Test
    public void verifyExecutionTime() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT);
        console.navigate(STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.getPlaceRequest());
        int actualExecutionTime = page.getStatefulEJBForm().intValue("execution-time");
        Assert.assertTrue("Execution time in the form should be close to the total execution time of the EJB deployment",
            numberOfInvocations * EJBFixtures.SLEEP_TIME <= actualExecutionTime);
        new ResourceVerifier(
            EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_EXECUTION_TIME_NAME, StatefulEJBExecutionTime.class),
            client)
            .verifyExists()
            .verifyAttribute("execution-time", Long.valueOf(actualExecutionTime));
    }

    @Test
    public void verifyInvocations() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT);
        console.navigate(STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals(
            "Number of invocations in the form should be equal to the total number of invocations executed",
            numberOfInvocations,
            page.getStatefulEJBForm().intValue("invocations"));
        new ResourceVerifier(
            EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_INVOCATIONS_NAME, StatefulEJBInvocations.class),
            client)
            .verifyExists()
            .verifyAttribute("invocations", Long.valueOf(numberOfInvocations));
    }

    @Test
    public void verifyPeakConcurrentInvocations() throws Exception {
        int numberOfConcurrentInvocations = Random.number(2, 5);
        Runnable invocationTask = () -> invoke(1, STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT);
        for (int i = 0; i < numberOfConcurrentInvocations; ++i) {
            new Thread(invocationTask).start();
        }
        console.navigate(STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Number of peak concurrent invocations should be correct", numberOfConcurrentInvocations,
            page.getStatefulEJBForm().intValue("peak-concurrent-invocations"));
        new ResourceVerifier(EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME,
            StatefulEJBPeakInvocations.class), client)
            .verifyExists()
            .verifyAttribute("peak-concurrent-invocations", Long.valueOf(numberOfConcurrentInvocations));
    }

    @Test
    public void verifyRunAsRole() throws Exception {
        console.navigate(STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's run as role", EJBFixtures.ROLE_1,
            page.getStatefulEJBForm().value("run-as-role"));
        new ResourceVerifier(
            EJBFixtures.statefulEJBAddress(STATEFUL_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatefulEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("run-as-role", EJBFixtures.ROLE_1);
    }

    @Override
    protected void invoke(int numberOfInvocations, EJBDeployment ejbDeployment) {
        String deploymentName = ejbDeployment.getDeploymentName().substring(0,
            ejbDeployment.getDeploymentName().indexOf(".war"));
        HttpGet httpGet;
        try {
            URL url = new URL("http://0.0.0.0:8080/" + deploymentName + "/Stateful");
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
