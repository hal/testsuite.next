package org.jboss.hal.testsuite.test.deployment;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.deployment.DeploymentPage;
import org.junit.AfterClass;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

public abstract class AbstractDeploymentTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations ops = new Operations(client);
    protected static final DeploymentOperations deploymentOps = new DeploymentOperations(client);
    protected static final List<String> deploymentsToBeCleanedUp = new ArrayList<>();
    protected static final String
        INDEX_HTML = "index.html",
        OTHER_HTML = "other.html",
        MAIN_SERVER_GROUP = "main-server-group",
        OTHER_SERVER_GROUP = "other-server-group";

    @Page
    protected DeploymentPage page;

    @Inject
    protected Console console;

    @AfterClass
    public static void cleanUp() throws Exception {
        try {
            deploymentOps.removeDeploymentsIfExist(deploymentsToBeCleanedUp);
        } finally {
            client.close();
        }
    }

    protected Deployment createSimpleDeployment() {
        String deploymentName = Random.name() + ".war";
        deploymentsToBeCleanedUp.add(deploymentName);
        return new Deployment.Builder(deploymentName)
                .textFile(INDEX_HTML, "<h1>HAL to rule them all</h1>")
                .build();
    }

    protected Deployment createAnotherDeployment() {
        String deploymentName = Random.name() + ".war";
        deploymentsToBeCleanedUp.add(deploymentName);
        return new Deployment.Builder(deploymentName)
                .textFile(OTHER_HTML, "<h1>Another HAL to rule them all</h1>")
                .build();
    }

}
