package org.jboss.hal.testsuite.test.configuration.ejb;

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
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.RemoteEJBInterface;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.SingletonCounterEJB;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.SingletonEJBWithRoles;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.SingletonServlet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.singletonEJBAddress;

@RunWith(Arquillian.class)
public class SingletonEJBTest extends AbstractEJBTest {

    private static final String SINGLETON_EJB_DEPLOYMENT_NAME = deploymentName("singleton-");
    private static final EJBDeployment SINGLETON_EJB_DEPLOYMENT =
        new EJBDeployment(SINGLETON_EJB_DEPLOYMENT_NAME, SingletonCounterEJB.class, EJBType.SINGLETON,
            SingletonServlet.class, RemoteEJBInterface.class);

    private static final String SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_NAME =
        deploymentName("singleton-invocations-");

    private static final String SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_NAME =
        deploymentName("singleton-execution-time-");

    private static final String SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME =
        deploymentName("singleton-peak-concurrent-invocations-time-");

    private static final String SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_NAME =
        deploymentName("singleton-with-roles");

    private static final EJBDeployment SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_NAME, SingletonCounterEJB.class, EJBType.SINGLETON,
            SingletonServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT =
        new EJBDeployment(SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_NAME, SingletonCounterEJB.class, EJBType.SINGLETON,
            SingletonServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME, SingletonCounterEJB.class,
            EJBType.SINGLETON,
            SingletonServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT =
        new EJBDeployment(SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_NAME, SingletonEJBWithRoles.class, EJBType.SINGLETON,
            SingletonServlet.class, RemoteEJBInterface.class);

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        enableStatistics();
        SINGLETON_EJB_DEPLOYMENT.deploy();
        SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.deploy();
        SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.deploy();
        SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.deploy();
        SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.deploy();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        SINGLETON_EJB_DEPLOYMENT.undeploy();
        SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.undeploy();
        SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.undeploy();
        SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.undeploy();
        SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.undeploy();
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
        ejbColumnFragment.selectItem(SINGLETON_EJB_DEPLOYMENT.toColumnId()).view();
        console.verify(SINGLETON_EJB_DEPLOYMENT.getPlaceRequest());
    }

    @Test
    public void verifyComponentClassName() throws Exception {
        console.navigate(SINGLETON_EJB_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Component class name in the form should be equal to the EJB's class name",
            SingletonCounterEJB.class.getSimpleName(),
            page.getSingletonEJBForm().value("component-class-name"));
        new ResourceVerifier(singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_NAME, SingletonCounterEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("component-class-name", SingletonCounterEJB.class.getSimpleName());
    }

    @Test
    public void verifyDeclaredRoles() throws Exception {
        console.navigate(SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's declared roles",
            String.join(", ", ROLE_1, ROLE_2),
            page.getSingletonEJBForm().value("declared-roles"));
        new ResourceVerifier(
            singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_NAME, SingletonEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("declared-roles",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(ROLE_1, ROLE_2).build());
    }

    @Test
    public void verifyExecutionTime() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT);
        console.navigate(SINGLETON_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.getPlaceRequest());
        int actualExecutionTime = page.getSingletonEJBForm().intValue("execution-time");
        Assert.assertTrue("Execution time in the form should be close to the total execution time of the EJB deployment",
            numberOfInvocations * SLEEP_TIME <= actualExecutionTime);
        new ResourceVerifier(singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_NAME, SingletonCounterEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("execution-time", Long.valueOf(actualExecutionTime));
    }

    @Test
    public void verifyInvocations() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT);
        console.navigate(SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals(
            "Number of invocations in the form should be equal to the total number of invocations executed",
            numberOfInvocations,
            page.getSingletonEJBForm().intValue("invocations"));
        new ResourceVerifier(
            singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_INVOCATIONS_NAME, SingletonCounterEJB.class), client)
            .verifyExists()
            .verifyAttribute("invocations", Long.valueOf(numberOfInvocations));
    }

    @Test
    public void verifyPeakConcurrentInvocations() throws Exception {
        int numberOfConcurrentInvocations = Random.number(2, 5);
        Runnable invocationTask = () -> invoke(1, SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT);
        for (int i = 0; i < numberOfConcurrentInvocations; ++i) {
            new Thread(invocationTask).start();
        }
        console.navigate(SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Number of peak concurrent invocations should be correct", numberOfConcurrentInvocations,
            page.getSingletonEJBForm().intValue("peak-concurrent-invocations"));
        new ResourceVerifier(singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME,
            SingletonCounterEJB.class), client)
            .verifyExists()
            .verifyAttribute("peak-concurrent-invocations", Long.valueOf(numberOfConcurrentInvocations));
    }

    @Test
    public void verifyRunAsRole() throws Exception {
        console.navigate(SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's run as role", ROLE_1,
            page.getSingletonEJBForm().value("run-as-role"));
        new ResourceVerifier(
            singletonEJBAddress(SINGLETON_EJB_DEPLOYMENT_WITH_ROLES_NAME, SingletonEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("run-as-role", ROLE_1);
    }

    @Override
    protected void invoke(int numberOfInvocations, EJBDeployment ejbDeployment) {
        String deploymentName = ejbDeployment.getDeploymentName().substring(0,
            ejbDeployment.getDeploymentName().indexOf(".war"));
        HttpGet httpGet;
        try {
            URL url = new URL("http://0.0.0.0:8080/" + deploymentName + "/Singleton");
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
