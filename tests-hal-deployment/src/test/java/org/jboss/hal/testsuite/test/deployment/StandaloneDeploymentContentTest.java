package org.jboss.hal.testsuite.test.deployment;

import java.io.File;
import java.io.PrintWriter;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.tooling.deployment.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class StandaloneDeploymentContentTest extends AbstractDeploymentTest {

    private static final String
        REFRESH_TITLE = "Refresh",
        COLLAPSE_TITLE = "Collapse",
        ADD_EMPTY_TITLE = "Add an empty file",
        ADD_CONTENT_TITLE = "Add Content",
        DOWNLOAD_FILE_TITLE = "Add Content",
        REMOVE_CONTENT_TITLE = "Remove Content";

    @Test
    public void browseContent() throws Exception {
        Deployment deployment = createTreeDeployment();
        ResourceVerifier deploymentVerifier = new ResourceVerifier(deployment.getAddress(), client);

        client.apply(deployment.deployEnabledCommand());
        deploymentVerifier.verifyExists().verifyAttribute(ENABLED, true);
        deploymentContentPage.navigateToDeploymentContent(deployment.getName());

        assertTrue(deploymentContentPage.isNodeVisible(PATH));
        assertFalse(deploymentContentPage.isNodeVisible(PATH, TO));

        deploymentContentPage.openNode(PATH);
        assertTrue(deploymentContentPage.isNodeVisible(PATH, TO));
        assertFalse(deploymentContentPage.isNodeVisible(PATH, TO, INDEX_HTML));

        deploymentContentPage.openNode(PATH, TO);
        assertTrue(deploymentContentPage.isNodeVisible(PATH, TO, INDEX_HTML));
    }

    @Test
    public void buttonsAvailableForNotExplodedDeployment() throws Exception {
        Deployment deployment = createTreeDeployment();
        ResourceVerifier deploymentVerifier = new ResourceVerifier(deployment.getAddress(), client);

        client.apply(deployment.deployEnabledCommand());
        deploymentVerifier.verifyExists().verifyAttribute(ENABLED, true);
        deploymentContentPage
            .navigateToDeploymentContent(deployment.getName())
            .selectNode(PATH);

        assertTrue(deploymentContentPage.isButtonAvailable(REFRESH_TITLE));
        assertTrue(deploymentContentPage.isButtonAvailable(COLLAPSE_TITLE));

        assertFalse(deploymentContentPage.isButtonAvailable(ADD_EMPTY_TITLE));
        assertFalse(deploymentContentPage.isButtonAvailable(ADD_CONTENT_TITLE));
        assertFalse(deploymentContentPage.isButtonAvailable(DOWNLOAD_FILE_TITLE));
        assertFalse(deploymentContentPage.isButtonAvailable(REMOVE_CONTENT_TITLE));
    }

    @Test
    public void addEmptyFile() throws Exception {

        Deployment deployment = createTreeDeployment();
        String fileName = Random.name();

        client.apply(deployment.deployEnabledCommand());
        client.apply(deployment.disableCommand());
        deploymentOps.explode(deployment);
        DialogFragment dialog = deploymentContentPage
            .navigateToDeploymentContent(deployment.getName())
            .openNode(PATH)
            .selectNode(PATH, TO)
            .selectButton(ADD_EMPTY_TITLE)
            .dialog();
        FormFragment form = dialog.getForm(Ids.CONTENT_NEW);
        String
            expectedPathStart = PATH + "/" + TO + "/",
            targetPathValue = expectedPathStart + fileName;
        assertEquals(expectedPathStart, form.text(TARGET_PATH));
        form.text(TARGET_PATH, targetPathValue);
        dialog.primaryButton();
        assertTrue(deploymentOps.deploymentContainsPath(deployment.getName(), targetPathValue));
    }

    @Test
    public void uploadFile() throws Exception {

        Deployment deployment = createTreeDeployment();
        File fileToBeAdded = File.createTempFile("test-", ".html");
        fileToBeAdded.deleteOnExit();
        PrintWriter writer = new PrintWriter(fileToBeAdded);
        writer.println("<b>test</b>");
        writer.close();

        client.apply(deployment.deployEnabledCommand());
        client.apply(deployment.disableCommand());
        deploymentOps.explode(deployment);
        DialogFragment dialog = deploymentContentPage
            .navigateToDeploymentContent(deployment.getName())
            .selectNode(PATH)
            .selectButton(ADD_CONTENT_TITLE)
            .dialog();
        dialog.getForm(Ids.CONTENT_NEW).text(URL, fileToBeAdded.toURI().toURL().toExternalForm());
        dialog.primaryButton();
        String newFilePath = PATH + "/" + fileToBeAdded.getName();
        assertTrue(deploymentOps.deploymentContainsPath(deployment.getName(), newFilePath));
        assertEquals(12L, deploymentOps.deploymentPathFileSize(deployment, newFilePath));
    }

    @Test
    public void removeFile() throws Exception {

        Deployment deployment = createTreeDeployment();
        client.apply(deployment.deployEnabledCommand());
        client.apply(deployment.disableCommand());
        deploymentOps.explode(deployment);
        String pathToIndexHtml = String.join("/", PATH, TO, INDEX_HTML);
        assertTrue(deploymentOps.deploymentContainsPath(deployment.getName(), pathToIndexHtml));

        deploymentContentPage
            .navigateToDeploymentContent(deployment.getName())
            .openNode(PATH)
            .openNode(PATH, TO)
            .selectNode(PATH, TO, INDEX_HTML)
            .selectButton(REMOVE_CONTENT_TITLE)
            .confirm();
        assertFalse(deploymentOps.deploymentContainsPath(deployment.getName(), pathToIndexHtml));
    }
}
