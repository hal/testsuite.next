package org.jboss.hal.testsuite.page.deployment;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.DeployServerGroupDialogFragment;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.DropdownFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER_GROUPS;

@Place(NameTokens.DEPLOYMENTS)
public class DeploymentPage extends BasePage {

    public WizardFragment uploadStandaloneDeployment() {
        getStandaloneDeploymentColumn().dropdownAction(Ids.DEPLOYMENT_ADD_ACTIONS, Ids.DEPLOYMENT_UPLOAD);
        return console.wizard();
    }

    public WizardFragment uploadDeploymentToContentRepository() {
        getContentRepositoryDeploymentColumn().dropdownAction(Ids.CONTENT_ADD_ACTIONS, Ids.CONTENT_ADD);
        return console.wizard();
    }

    public WizardFragment uploadDeploymentToServerGroup(String serverGroupName) {
        getServerGroupDeploymentColumn(serverGroupName).dropdownAction(Ids.SERVER_GROUP_DEPLOYMENT_ADD_ACTIONS,
                Ids.SERVER_GROUP_DEPLOYMENT_UPLOAD);
        return console.wizard();
    }

    public DialogFragment addUnmanagedStandaloneDeployment() {
        getStandaloneDeploymentColumn().dropdownAction(Ids.DEPLOYMENT_ADD_ACTIONS, Ids.DEPLOYMENT_UNMANAGED_ADD);
        return console.dialog();
    }

    public DialogFragment addUnmanagedDeploymentToContentRepository() {
        getContentRepositoryDeploymentColumn().dropdownAction(Ids.CONTENT_ADD_ACTIONS, Ids.CONTENT_UNMANAGED_ADD);
        return console.dialog();
    }

    public DialogFragment addUnmanagedDeploymentToServerGroup(String serverGroupName) {
        getServerGroupDeploymentColumn(serverGroupName).dropdownAction(Ids.SERVER_GROUP_DEPLOYMENT_ADD_ACTIONS,
                Ids.SERVER_GROUP_DEPLOYMENT_UNMANAGED_ADD);
        return console.dialog();
    }

    public DeployServerGroupDialogFragment uploadExistingContentToServerGroup(String serverGroupName) {
        getServerGroupDeploymentColumn(serverGroupName).dropdownAction(Ids.SERVER_GROUP_DEPLOYMENT_ADD_ACTIONS,
                Ids.SERVER_GROUP_DEPLOYMENT_ADD);
        return console.dialog(DeployServerGroupDialogFragment.class);
    }

    public DialogFragment addEmptyStandaloneDeployment() {
        getStandaloneDeploymentColumn().dropdownAction(Ids.DEPLOYMENT_ADD_ACTIONS, Ids.DEPLOYMENT_EMPTY_CREATE);
        return console.dialog();
    }

    public DeploymentPage callActionOnStandaloneDeployment(String deploymentName, String actionName) {
        openDropdownOnStandaloneDeployment(deploymentName).click(actionName);
        return this;
    }

    public DeploymentPage callActionOnDeploymentInContentRepository(String deploymentName, String actionName) {
        openDropdownOnDeploymentInContentRepository(deploymentName).click(actionName);
        return this;
    }

    public DeploymentPage callActionOnDeploymentInServerGroup(String serverGroupName, String deploymentName, String actionName) {
        openDropdownOnDeploymentInServerGroup(serverGroupName, deploymentName).click(actionName);
        return this;
    }

    public boolean isActionOnStandaloneDeploymentAvailable(String deploymentName, String actionName) {
        return openDropdownOnStandaloneDeployment(deploymentName).containsItem(actionName);
    }

    public boolean isActionOnDeploymentInContentRepository(String deploymentName, String actionName) {
        return openDropdownOnDeploymentInContentRepository(deploymentName).containsItem(actionName);
    }

    public boolean isActionOnDeploymentInServerGroup(String serverGroupName, String deploymentName, String actionName) {
        return openDropdownOnDeploymentInServerGroup(serverGroupName, deploymentName).containsItem(actionName);
    }

    public DeployServerGroupDialogFragment deployServerGroupDialog() {
        return console.dialog(DeployServerGroupDialogFragment.class);
    }

    public DeploymentPage confirm() {
        console.confirmationDialog().confirm();
        return this;
    }

    public DeploymentPage checkAndSelectDefaultActionOnServerGroupDeployment(String serverGroupName, String deploymentName,
            String expectedDefaultActionName) {
        selectServerGroupDeployment(serverGroupName, deploymentName).defaultAction(expectedDefaultActionName);
        return this;
    }

    private DropdownFragment openDropdownOnStandaloneDeployment(String deploymentName) {
        String deploymentId = Ids.deployment(deploymentName.replace(".", ""));
        return getStandaloneDeploymentColumn().selectItem(deploymentId).dropdown();
    }

    private DropdownFragment openDropdownOnDeploymentInContentRepository(String deploymentName) {
        String deploymentId = deploymentName.replace(".", "");
        return getContentRepositoryDeploymentColumn().selectItem(deploymentId).dropdown();
    }

    private DropdownFragment openDropdownOnDeploymentInServerGroup(String serverGroupName, String deploymentName) {
        return selectServerGroupDeployment(serverGroupName, deploymentName).dropdown();
    }

    private ItemFragment selectServerGroupDeployment(String serverGroupName, String deploymentName) {
        String deploymentId = Ids.build(serverGroupName, deploymentName.replace(".", ""));
        return getServerGroupDeploymentColumn(serverGroupName).selectItem(deploymentId);
    }

    private ColumnFragment getStandaloneDeploymentColumn() {
        return console.finder(NameTokens.DEPLOYMENTS).column(Ids.DEPLOYMENT);
    }

    private ColumnFragment getContentRepositoryDeploymentColumn() {
        return console.finder(NameTokens.DEPLOYMENTS, new FinderPath()
                .append(Ids.DEPLOYMENT_BROWSE_BY, "content-repository"))
                .column(Ids.CONTENT);
    }

    private ColumnFragment getServerGroupDeploymentColumn(String serverGroupName) {
        return console.finder(NameTokens.DEPLOYMENTS, new FinderPath()
                .append(Ids.DEPLOYMENT_BROWSE_BY, SERVER_GROUPS)
                .append(Ids.DEPLOYMENT_SERVER_GROUP, Ids.build("sg", serverGroupName)))
                .column(Ids.SERVER_GROUP_DEPLOYMENT);
    }

}
