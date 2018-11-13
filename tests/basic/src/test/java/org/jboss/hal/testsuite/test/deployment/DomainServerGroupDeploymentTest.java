package org.jboss.hal.testsuite.test.deployment;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.junit.Assert.*;

@Category(Domain.class)
@RunWith(Arquillian.class)
public class DomainServerGroupDeploymentTest extends AbstractDeploymentTest {

    private static final String FILE = " file.";

    @Test
    public void uploadDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment deployment = createSimpleDeployment();

        WizardFragment wizard = deploymentPage.uploadDeploymentToServerGroup(MAIN_SERVER_GROUP);
        wizard.getUploadForm().uploadFile(deployment.getDeploymentFile());
        wizard.next(Ids.UPLOAD_NAMES_FORM);
        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();

        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        assertTrue("The deployment does not contain expected " + INDEX_HTML + FILE,
                deploymentOps.deploymentContainsPath(deployment.getName(), INDEX_HTML));
        new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                .and(DEPLOYMENT, deployment.getName()), client).verifyExists();
    }

    @Test
    public void addUnmanagedDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment deployment = createSimpleDeployment();
        String
            nameValue = deployment.getName(),
            runtimeNameValue = Random.name(),
            pathValue = deployment.getDeploymentFile().getAbsolutePath();

        DialogFragment dialog = deploymentPage.addUnmanagedDeploymentToServerGroup(MAIN_SERVER_GROUP);
        FormFragment form = dialog.getForm(Ids.UNMANAGED_FORM);
        form.text(NAME, nameValue);
        form.text(RUNTIME_NAME, runtimeNameValue);
        form.text(PATH, pathValue);
        form.flip(ARCHIVE, true);
        dialog.primaryButton();

        new ResourceVerifier(deployment.getAddress(), client).verifyExists()
            .verifyAttribute(NAME, nameValue)
            .verifyAttribute(RUNTIME_NAME, runtimeNameValue);
        assertEquals(pathValue, deploymentOps.getDeploymentPath(nameValue));
        assertEquals(false, deploymentOps.deploymentIsExploded(nameValue));
        new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                .and(DEPLOYMENT, deployment.getName()), client).verifyExists();
    }

    @Test
    public void deployExistingContent() throws Exception {
        Deployment deployment = createSimpleDeployment();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                    .and(DEPLOYMENT, deployment.getName()), client, 120000);

        client.apply(deployment.deployEnabledCommand(OTHER_SERVER_GROUP));
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        deploymentInMainServerGroupVerifier.verifyDoesNotExist();
        deploymentPage.navigate();

        deploymentPage.uploadExistingContentToServerGroup(MAIN_SERVER_GROUP)
                .selectServerGroups(deployment.getName())
                .primaryButton();
        deploymentInMainServerGroupVerifier.verifyExists();
    }

    @Test
    public void enableDeployment() throws Exception {
        Deployment deployment = createSimpleDeployment();
        client.apply(deployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        client.apply(deployment.disableCommand());
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                .and(DEPLOYMENT, deployment.getName()), client)
                .verifyExists()
                .verifyAttribute(ENABLED, false);

        deploymentPage.navigate();
        deploymentPage.checkAndSelectDefaultActionOnServerGroupDeployment(MAIN_SERVER_GROUP, deployment.getName(), "Enable");
        deploymentInMainServerGroupVerifier.verifyAttribute(ENABLED, true);
    }

    @Test
    public void disableDeployment() throws Exception {
        Deployment deployment = createSimpleDeployment();
        client.apply(deployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                .and(DEPLOYMENT, deployment.getName()), client)
                .verifyExists()
                .verifyAttribute(ENABLED, true);

        deploymentPage.navigate();
        deploymentPage.callActionOnDeploymentInServerGroup(MAIN_SERVER_GROUP, deployment.getName(), "Disable");
        deploymentInMainServerGroupVerifier.verifyAttribute(ENABLED, false);
    }

    @Test
    public void undeployDeployment() throws Exception {
        Deployment deployment = createSimpleDeployment();
        client.apply(deployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                .and(DEPLOYMENT, deployment.getName()), client)
                .verifyExists();

        deploymentPage.navigate();
        deploymentPage.callActionOnDeploymentInServerGroup(MAIN_SERVER_GROUP, deployment.getName(), "Undeploy").confirm();
        deploymentInMainServerGroupVerifier.verifyDoesNotExist();
    }

}
