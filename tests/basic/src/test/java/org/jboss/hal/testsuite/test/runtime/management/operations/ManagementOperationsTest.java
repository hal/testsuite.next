package org.jboss.hal.testsuite.test.runtime.management.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.runtime.ManagementOperationsPage;
import org.jboss.hal.testsuite.test.deployment.Deployment;
import org.jboss.hal.testsuite.test.deployment.DeploymentOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.testsuite.test.runtime.management.operations.LongRunningServlet.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ManagementOperationsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Administration adminOps = new Administration(client);
    private static final ManagementOperations managementOps = new ManagementOperations(client);
    private static final DeploymentOperations deploymentOps = new DeploymentOperations(client);
    private static final List<String> deploymentsToBeCleanedUp = new ArrayList<>();

    @Page
    private ManagementOperationsPage page;

    @Inject
    private Console console;

    @AfterClass
    public static void closeClient() throws Exception {
        client.close();
    }

    @After
    public void cleanUp() throws Exception {
        deploymentOps.removeDeploymentsIfExist(deploymentsToBeCleanedUp);
        deploymentsToBeCleanedUp.clear();
        adminOps.restartIfRequired();
    }

    @Test
    public void listOperations() throws Exception {
        Deployment deployment = createSlowDeployment(6);
        CompletableFuture<Void> deployFuture = deploymentOps.deployAsync(deployment);

        long operationId = managementOps.getActiveDeploymentOperationId();
        page.navigate();
        assertTrue(page.isVisibleActiveOperation(operationId));

        deployFuture.join();
    }

    @Test
    public void reloadOperations() throws Exception {
        Deployment deployment = createSlowDeployment(6);
        page.navigate();

        CompletableFuture<Void> deployFuture = deploymentOps.deployAsync(deployment);
        long operationId = managementOps.getActiveDeploymentOperationId();
        assertFalse(page.isVisibleActiveOperation(operationId));

        page.reloadManagementOperationsList();
        assertTrue(page.isVisibleActiveOperation(operationId));

        deployFuture.join();
    }

    @Test
    public void cancelOperation() throws Exception {
        Deployment deployment = createSlowDeployment(40);
        CompletableFuture<Void> deployFuture = deploymentOps.deployAsync(deployment);

        long operationId = managementOps.getActiveDeploymentOperationId();
        page.navigate();
        assertTrue(page.isVisibleActiveOperation(operationId));
        page.cancelActiveOperation(operationId);

        deployFuture.join();
    }

    @Test
    public void cancelNonProgressingOperation() throws Exception {
        Deployment deployment = createSlowDeployment(40);
        CompletableFuture<Void> deployFuture = deploymentOps.deployAsync(deployment);

        page.navigate();
        managementOps.waitForNonProgressingOperation(25);
        page.cancelNonProgressingOperation();
        assertTrue(managementOps.thereIsNoNonProgressingOperationATM());

        deployFuture.join();
    }

    private Deployment createSlowDeployment(Integer deploymentDurationInSeconds) {
        String propertiesContent = DURATION_IN_SECONDS + "=" + deploymentDurationInSeconds;
        Deployment deployment = new Deployment.Builder(Random.name() + ".war")
                .classFile(LongRunningServlet.class)
                .textFile(DURATION_PROPERTIES_PATH, propertiesContent)
                .build();

        deploymentsToBeCleanedUp.add(deployment.getName());
        return deployment;
    }
}
