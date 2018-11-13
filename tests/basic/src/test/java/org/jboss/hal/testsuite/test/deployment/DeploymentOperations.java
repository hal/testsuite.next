package org.jboss.hal.testsuite.test.deployment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator.ModelNodePropertiesBuilder;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public class DeploymentOperations {
    private Operations ops;

    public DeploymentOperations(OnlineManagementClient client) {
        this.ops = new Operations(client);
    }

    public boolean deploymentContainsPath(String deploymentName, String pathValue) throws IOException {
        return ops.invoke(BROWSE_CONTENT, Address.deployment(deploymentName), Values.of(PATH, pathValue)).isSuccess();
    }

    public long deploymentPathFileSize(Deployment deployment, String pathValue) throws IOException {
        ModelNodeResult result = ops.invoke(BROWSE_CONTENT, deployment.getAddress(), Values.of(PATH, pathValue));
        result.assertDefinedValue();
        return result.listValue().get(0).get("file-size").asLong();
    }

    public boolean deploymentIsExploded(String deploymentName) throws IOException {
        ModelNode isArchiveNode = getFirstContentNode(deploymentName).get("archive");
        return isArchiveNode.isDefined() && !isArchiveNode.asBoolean();
    }

    public DeploymentOperations explode(Deployment deployment) throws IOException {
        ops.invoke(EXPLODE, deployment.getAddress()).assertSuccess();
        return this;
    }

    public String getDeploymentPath(String deploymentName) throws IOException {
        return getFirstContentNode(deploymentName).get(PATH).asString();
    }

    public DeploymentOperations undeployFromServerGroup(String deploymentName, String serverGroupName) throws Exception {
        if (!ConfigUtils.isDomain()) {
            throw new IllegalStateException("Should be called in domain mode only.");
        }
        ops.removeIfExists(Address.of(SERVER_GROUP, serverGroupName).and(DEPLOYMENT, deploymentName));
        return this;
    }

    public DeploymentOperations removeDeploymentsIfExist(List<String> deploymentsToBeRemoved) throws Exception {
        if (ConfigUtils.isDomain()) {
            List<String> serverGroupNames = ops.readChildrenNames(Address.root(), SERVER_GROUP).stringListValue();
            for (String serverGroupName : serverGroupNames) {
                List<String> serverGroupDeploymentNames =
                        ops.readChildrenNames(Address.of(SERVER_GROUP, serverGroupName), DEPLOYMENT).stringListValue();
                for (String deploymentName : deploymentsToBeRemoved) {
                    if (serverGroupDeploymentNames.contains(deploymentName)) {
                        undeployFromServerGroup(deploymentName, serverGroupName);
                    }
                }
            }
        }
        for (String deploymentName : deploymentsToBeRemoved) {
            ops.removeIfExists(Address.deployment(deploymentName));
        }
        return this;
    }

    public CompletableFuture<Void> deployAsync(Deployment deployment) throws InterruptedException {
        CompletableFuture<Void> deployFuture = runAsync(() -> {
            try {
                addContent(deployment);
                deploy(deployment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        TimeUnit.SECONDS.sleep(2);
        return deployFuture;
    }

    private DeploymentOperations addContent(Deployment deployment) throws MalformedURLException, IOException {
        ops.add(deployment.getAddress(), Values.ofList(CONTENT,
                new ModelNodePropertiesBuilder().addProperty(URL,
                        deployment.getDeploymentFile().toURI().toURL().toExternalForm()).build()))
           .assertSuccess();
        return this;
    }

    private DeploymentOperations deploy(Deployment deployment) throws IOException {
        ops.invoke(DEPLOY, deployment.getAddress());
        return this;
    }

    private ModelNode getFirstContentNode(String deploymentName) throws IOException {
        ModelNodeResult contentResult =  ops.readAttribute(Address.deployment(deploymentName), CONTENT);
        contentResult.assertDefinedValue();
        ModelNode firstContentNode = contentResult.listValue().get(0);
        return firstContentNode;
    }

}
