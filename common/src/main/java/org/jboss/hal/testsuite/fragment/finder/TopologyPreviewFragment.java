package org.jboss.hal.testsuite.fragment.finder;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.page.runtime.TopologyPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

/** Fragment for domain topology preview. Use {@link TopologyPage#navigateToTopologyFragment()} to get an instance.*/
public class TopologyPreviewFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @Inject private Console console;

    public boolean containsConnectedHostNamed(String hostName) {
        By selector = ByJQuery.selector("[data-host='" + hostName + "'] a:contains('" + hostName + "')");
        return isPresent(selector);
    }

    public boolean containsDisconnectedHostNamed(String hostName) {
        By selector = ByJQuery.selector("[data-host='" + hostName + "'] span." + CSS.disconnected + ":contains('" + hostName + "')");
        return isPresent(selector);
    }

    private boolean isPresent(By selector) {
        try {
            waitGui().until().element(root, selector).is().present();
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

}
