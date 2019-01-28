package org.jboss.hal.testsuite.fragment;

import java.util.Arrays;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.hal.resources.Ids;

import static org.jboss.hal.testsuite.Selectors.contains;

/** Deploy to/undeploy from server group dialog abstraction. */
public class DeployServerGroupDialogFragment extends DialogFragment {

    public DeployServerGroupDialogFragment selectServerGroups(String... serverGroupNames) {
        return selectItems(serverGroupNames);
    }

    public DeployServerGroupDialogFragment selectDeployments(String... deploymentNames) {
        return selectItems(deploymentNames);
    }

    private DeployServerGroupDialogFragment selectItems(String... items) {
        Arrays.stream(items).forEach(itemName -> {
            String itemRowSelector = "#" + Ids.SERVER_GROUP_DEPLOYMENT_TABLE + " td" + contains(itemName);
            root.findElement(ByJQuery.selector(itemRowSelector)).click();
        });
        return this;
    }

}
