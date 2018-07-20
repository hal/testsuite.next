package org.jboss.hal.testsuite.test.runtime.ejb;

import java.io.IOException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;

public abstract class AbstractEJBTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    protected static final Operations operations = new Operations(client);

    protected static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);

    protected static void enableStatistics() throws IOException {
        operations.writeAttribute(Address.subsystem("ejb3"), "statistics-enabled", true);
    }

    private static void undeployIfExists(String deploymentName)
        throws CommandFailedException, IOException, OperationException {
        if (operations.exists(Address.deployment(deploymentName))) {
            client.apply(new Undeploy.Builder(deploymentName).build());
        }
    }

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    protected static String deploymentName(String prefix) {
        return prefix + Random.name() + ".war";
    }

    protected ColumnFragment ejbColumnFragment;

    public void initPage(Console console) throws IOException {
        ejbColumnFragment = console.finder(NameTokens.RUNTIME,
            runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), "ejb3"))
            .column("ejb3");
    }

    protected abstract void invoke(int numberOfInvocations, EJBDeployment ejbDeployment);

    protected enum EJBType {

        STATELESS("stateless"),
        STATEFUL("stateful"),
        SINGLETON("singleton"),
        MESSAGE_DRIVEN("mdb");

        private String value;

        EJBType(String value) {
            this.value = value;
        }
    }

    protected static class EJBDeployment {

        private final String deploymentName;
        private final EJBType type;
        private final Class<?> ejbClass;
        private final Class<?>[] classesToBeDeployed;

        String getDeploymentName() {
            return deploymentName;
        }

        EJBDeployment(String deploymentName, Class<?> ejbClass, EJBType type, Class<?>... clazz) {
            this.deploymentName = deploymentName;
            this.ejbClass = ejbClass;
            this.type = type;
            this.classesToBeDeployed = clazz;
        }

        void deploy() throws CommandFailedException {
            WebArchive deployment = ShrinkWrap.create(WebArchive.class, deploymentName)
                .addClass(ejbClass)
                .addClasses(classesToBeDeployed)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
            Deploy deploy =
                new Deploy.Builder(deployment.as(ZipExporter.class).exportAsInputStream(), deployment.getName(), true)
                    .build();
            client.apply(deploy);
        }

        void undeploy() throws OperationException, IOException, CommandFailedException {
            undeployIfExists(deploymentName);
        }

        String toColumnId() {
            return Ids.build(deploymentName.replace(".", ""), type.value, ejbClass.getSimpleName().toLowerCase());
        }

        PlaceRequest getPlaceRequest() {
            return new PlaceRequest.Builder().nameToken(NameTokens.EJB3_RUNTIME)
                .with(NameTokens.DEPLOYMENT, deploymentName)
                .with("type", type.value)
                .with("name", ejbClass.getSimpleName()).build();
        }
    }
}
