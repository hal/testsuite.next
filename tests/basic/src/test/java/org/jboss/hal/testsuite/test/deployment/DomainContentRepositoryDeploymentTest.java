package org.jboss.hal.testsuite.test.deployment;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.UploadFormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(Domain.class)
@RunWith(Arquillian.class)
public class DomainContentRepositoryDeploymentTest extends AbstractDeploymentTest {

    private static final String FILE = " file.";

    @Test
    public void uploadDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment deployment = createSimpleDeployment();

        WizardFragment wizard = deploymentPage.uploadDeploymentToContentRepository();
        wizard.getUploadForm().uploadFile(deployment.getDeploymentFile());
        wizard.next(Ids.UPLOAD_NAMES_FORM);
        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();

        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        assertTrue("The deployment does not contain expected " + INDEX_HTML + FILE,
                deploymentOps.deploymentContainsPath(deployment.getName(), INDEX_HTML));
    }

    @Test
    public void addUnmanagedDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment deployment = createSimpleDeployment();
        String
            nameValue = deployment.getName(),
            runtimeNameValue = Random.name(),
            pathValue = deployment.getDeploymentFile().getAbsolutePath();

        DialogFragment dialog = deploymentPage.addUnmanagedDeploymentToContentRepository();
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
    }

    @Test
    public void deployToServerGroup() throws Exception {
        String deployActionName = "Deploy";
        Deployment deployment = createSimpleDeployment();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                    .and(DEPLOYMENT, deployment.getName()), client);

        client.apply(deployment.deployEnabledCommand(OTHER_SERVER_GROUP));
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        deploymentInMainServerGroupVerifier.verifyDoesNotExist();
        deploymentPage.navigate();

        deploymentPage.callActionOnDeploymentInContentRepository(deployment.getName(), deployActionName)
                .deployServerGroupDialog()
                .selectServerGroups(MAIN_SERVER_GROUP)
                .primaryButton();
        deploymentInMainServerGroupVerifier.verifyExists();
    }

    @Test
    public void undeployFromServerGroup() throws Exception {
        Deployment deployment = createSimpleDeployment();
        ResourceVerifier deploymentInMainServerGroupVerifier = new ResourceVerifier(Address.of(SERVER_GROUP, MAIN_SERVER_GROUP)
                    .and(DEPLOYMENT, deployment.getName()), client);

        client.apply(deployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        new ResourceVerifier(deployment.getAddress(), client).verifyExists();
        deploymentInMainServerGroupVerifier.verifyExists();
        deploymentPage.navigate();

        deploymentPage.callActionOnDeploymentInContentRepository(deployment.getName(), "Undeploy")
                .deployServerGroupDialog()
                .selectServerGroups(MAIN_SERVER_GROUP)
                .primaryButton();
        deploymentInMainServerGroupVerifier.verifyDoesNotExist();
    }

    @Test
    public void replaceDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment
            originalDeployment = createSimpleDeployment(),
            newDeployment = createAnotherDeployment();
        ResourceVerifier deploymentVerifier = new ResourceVerifier(originalDeployment.getAddress(), client);

        client.apply(originalDeployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        deploymentVerifier.verifyExists();
        assertTrue("The deployment does not contain expected " + INDEX_HTML + FILE,
                deploymentOps.deploymentContainsPath(originalDeployment.getName(), INDEX_HTML));
        assertFalse("The deployment should not yet contain " + OTHER_HTML + FILE,
                deploymentOps.deploymentContainsPath(originalDeployment.getName(), OTHER_HTML));

        deploymentPage.navigate();
        deploymentPage.callActionOnDeploymentInContentRepository(originalDeployment.getName(), "Replace");

        DialogFragment uploadDialog = console.dialog();
        UploadFormFragment.getUploadForm(uploadDialog.getRoot()).uploadFile(newDeployment.getDeploymentFile());
        uploadDialog.primaryButton();

        deploymentVerifier
            .verifyTrue("The deployment does not contain expected " + OTHER_HTML + FILE,
                    () -> deploymentOps.deploymentContainsPath(originalDeployment.getName(), OTHER_HTML))
            .verifyTrue("The deployment should not contain " + INDEX_HTML + " file anymore.",
                    () -> !deploymentOps.deploymentContainsPath(originalDeployment.getName(), INDEX_HTML));
    }

    @Test
    public void removeDeployment() throws Exception {
        deploymentPage.navigate();
        Deployment deployment = createSimpleDeployment();
        ResourceVerifier deploymentVerifier = new ResourceVerifier(deployment.getAddress(), client);

        client.apply(deployment.deployEnabledCommand(MAIN_SERVER_GROUP));
        deploymentVerifier.verifyExists();
        deploymentOps.undeployFromServerGroup(deployment.getName(), MAIN_SERVER_GROUP);

        deploymentPage.navigate();
        deploymentPage.callActionOnDeploymentInContentRepository(deployment.getName(), "Remove").confirm();
        deploymentVerifier.verifyDoesNotExist();
    }

}
