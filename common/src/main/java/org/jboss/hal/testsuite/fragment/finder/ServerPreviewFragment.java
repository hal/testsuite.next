package org.jboss.hal.testsuite.fragment.finder;

import java.util.List;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static java.util.stream.Collectors.toMap;
import static org.jboss.hal.resources.CSS.key;
import static org.jboss.hal.resources.CSS.listGroup;
import static org.jboss.hal.resources.CSS.listGroupItem;
import static org.jboss.hal.resources.CSS.value;
import static org.openqa.selenium.By.className;

public class ServerPreviewFragment {

    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    @Inject
    private Console console;

    @FindByJQuery("h2:contains('Main Attributes'):visible ~ ul." + listGroup + " > li:visible:contains(\"URL\")")
    private WebElement url;

    @FindBy(css = "#open-ports ." + listGroupItem)
    private List<WebElement> openPorts;

    public AttributeItem getUrlAttributeItem() {
        return new AttributeItem(url);
    }

    public Map<String, Integer> getOpenPorts() {
        return openPorts.stream().collect(toMap(
                element -> element.findElement(className(key)).getText(),
                element -> Integer.parseInt(element.findElement(className(value)).getText())));
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
