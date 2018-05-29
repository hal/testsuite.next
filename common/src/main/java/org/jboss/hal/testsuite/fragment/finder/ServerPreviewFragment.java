package org.jboss.hal.testsuite.fragment.finder;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.hal.resources.CSS.listGroup;

public class ServerPreviewFragment {

    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    @Inject
    private Console console;

    @FindByJQuery("h2:contains('Main Attributes'):visible ~ ul." + listGroup + " > li:visible:contains(\"URL\")")
    private WebElement url;

    public AttributeItem getUrlAttributeItem() {
        return new AttributeItem(url);
    }

    public static class AttributeItem {

        private final WebElement keyElement;
        private final WebElement valueElement;

        private AttributeItem(WebElement containingElement) {
            this.keyElement = containingElement.findElement(ByJQuery.selector("span.key"));
            this.valueElement = containingElement.findElement(ByJQuery.selector("span.value"));
        }

        public WebElement getKeyElement() {
            return keyElement;
        }

        public WebElement getValueElement() {
            return valueElement;
        }
    }

}
