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
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.runtime.ejb.EJBPage;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.RemoteEJBInterface;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.StatelessEJBWithRoles;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.StatelessGreeterEJB;
import org.jboss.hal.testsuite.test.configuration.ejb.ejb.StatelessServlet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.statelessEJBAddress;

@RunWith(Arquillian.class)
public class StatelessEJBTest extends AbstractEJBTest {

    private static final String STATELESS_EJB_DEPLOYMENT_NAME = deploymentName("stateless-");

    private static final String STATELESS_EJB_DEPLOYMENT_INVOCATIONS_NAME = deploymentName("stateless-invocations-");

    private static final String STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_NAME =
        deploymentName("stateless-execution-time-");

    private static final String STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME =
        deploymentName("stateless-peak-concurrent-invocations-time-");

    private static final String STATELESS_EJB_DEPLOYMENT_WITH_ROLES_NAME =
        deploymentName("stateless-with-roles");

    private static final EJBDeployment STATELESS_EJB_DEPLOYMENT =
        new EJBDeployment(STATELESS_EJB_DEPLOYMENT_NAME, StatelessGreeterEJB.class, EJBType.STATELESS,
            StatelessServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment STATELESS_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(STATELESS_EJB_DEPLOYMENT_INVOCATIONS_NAME, StatelessGreeterEJB.class, EJBType.STATELESS,
            StatelessServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT =
        new EJBDeployment(STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_NAME, StatelessGreeterEJB.class, EJBType.STATELESS,
            StatelessServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT =
        new EJBDeployment(STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME, StatelessGreeterEJB.class,
            EJBType.STATELESS,
            StatelessServlet.class, RemoteEJBInterface.class);

    private static final EJBDeployment STATELESS_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT =
        new EJBDeployment(STATELESS_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatelessEJBWithRoles.class, EJBType.STATELESS,
            StatelessServlet.class, RemoteEJBInterface.class);

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException {
        enableStatistics();
        STATELESS_EJB_DEPLOYMENT.deploy();
        STATELESS_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.deploy();
        STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.deploy();
        STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.deploy();
        STATELESS_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.deploy();
    }

    @AfterClass
    public static void tearDown() throws OperationException, IOException, CommandFailedException {
        STATELESS_EJB_DEPLOYMENT.undeploy();
        STATELESS_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.undeploy();
        STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.undeploy();
        STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.undeploy();
        STATELESS_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.undeploy();
    }

    @Page
    private EJBPage page;

    @Inject
    private Console console;

    @Test
    public void view() throws IOException {
        initPage(console);
        ejbColumnFragment.selectItem(STATELESS_EJB_DEPLOYMENT.toColumnId()).view();
        console.verify(STATELESS_EJB_DEPLOYMENT.getPlaceRequest());
    }

    @Test
    public void verifyComponentClassName() throws Exception {
        console.navigate(STATELESS_EJB_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Component class name in the form should be equal to the EJB's class name",
            StatelessGreeterEJB.class.getSimpleName(),
            page.getStatelessEJBForm().value("component-class-name"));
        new ResourceVerifier(statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_NAME, StatelessGreeterEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("component-class-name", StatelessGreeterEJB.class.getSimpleName());
    }

    @Test
    public void verifyDeclaredRoles() throws Exception {
        console.navigate(STATELESS_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's declared roles",
            String.join(", ", ROLE_1, ROLE_2),
            page.getStatelessEJBForm().value("declared-roles"));
        new ResourceVerifier(
            statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatelessEJBWithRoles.class),
            client)
            .verifyExists()
            .verifyAttribute("declared-roles",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(ROLE_1, ROLE_2).build());
    }

    @Test
    public void verifyExecutionTime() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT);
        console.navigate(STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_DEPLOYMENT.getPlaceRequest());
        int actualExecutionTime = page.getStatelessEJBForm().intValue("execution-time");
        Assert.assertTrue("Execution time in the form should be close to the total execution time of the EJB org.jboss.hal.testsuite.test.deployment",
            numberOfInvocations * SLEEP_TIME <= actualExecutionTime);
        new ResourceVerifier(
            statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_EXECUTION_TIME_NAME, StatelessGreeterEJB.class),
            client)
            .verifyExists()
            .verifyAttribute("execution-time", Long.valueOf(actualExecutionTime));
    }

    @Test
    public void verifyInvocations() throws Exception {
        int numberOfInvocations = Random.number(1, 5);
        invoke(numberOfInvocations, STATELESS_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT);
        console.navigate(STATELESS_EJB_DEPLOYMENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals(
            "Number of invocations in the form should be equal to the total number of invocations executed",
            numberOfInvocations,
            page.getStatelessEJBForm().intValue("invocations"));
        new ResourceVerifier(
            statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_INVOCATIONS_NAME, StatelessGreeterEJB.class), client)
            .verifyExists()
            .verifyAttribute("invocations", Long.valueOf(numberOfInvocations));
    }

    @Test
    public void verifyPeakConcurrentInvocations() throws Exception {
        int numberOfConcurrentInvocations = Random.number(2, 5);
        Runnable invocationTask = () -> invoke(1, STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT);
        for (int i = 0; i < numberOfConcurrentInvocations; ++i) {
            new Thread(invocationTask).start();
        }
        console.navigate(STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Number of peak concurrent invocations should be correct", numberOfConcurrentInvocations,
            page.getStatelessEJBForm().intValue("peak-concurrent-invocations"));
        new ResourceVerifier(statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_PEAK_CONCURRENT_INVOCATIONS_NAME,
            StatelessGreeterEJB.class), client)
            .verifyExists()
            .verifyAttribute("peak-concurrent-invocations", Long.valueOf(numberOfConcurrentInvocations));
    }

    @Test
    public void verifyRunAsRole() throws Exception {
        console.navigate(STATELESS_EJB_DEPLOYMENT_WITH_ROLES_DEPLOYMENT.getPlaceRequest());
        Assert.assertEquals("Declared roles in the form should be equal to the EJB's run as role", ROLE_1,
            page.getStatelessEJBForm().value("run-as-role"));
        new ResourceVerifier(
            statelessEJBAddress(STATELESS_EJB_DEPLOYMENT_WITH_ROLES_NAME, StatelessEJBWithRoles.class),
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
            URL url = new URL("http://0.0.0.0:8080/" + deploymentName + "/Stateless");
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