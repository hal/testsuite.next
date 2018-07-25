package org.jboss.hal.testsuite.fragment.finder;

import java.util.List;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.hal.resources.CSS.listGroup;
import static org.jboss.hal.resources.CSS.progressBar;

public class IOWorkerPreviewFragment {

    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    @Inject
    private Console console;

    @FindByJQuery("h2:contains('Connections'):visible ~ ul." + listGroup + " > li")
    private List<WebElement> connections;

    @FindByJQuery("div.progress-description[title='Core Pool Size'] + div")
    private WebElement corePoolSize;

    @FindByJQuery("div.progress-description[title='Max Pool Size'] + div")
    private WebElement maxPoolSize;

    @FindByJQuery("div.progress-description[title='IO Thread Count'] + div")
    private WebElement ioThreadCount;

    @FindByJQuery("a.clickable > span.fa-refresh")
    private WebElement refreshButton;

    public List<IOWorkerPreviewFragment.AttributeItem> getConnections() {
        return connections.stream().map(AttributeItem::new).collect(Collectors.toList());
    }

    public WebElement getCorePoolSize() {
        return corePoolSize;
    }

    public WebElement getMaxPoolSize() {
        return maxPoolSize;
    }

    public WebElement getIoThreadCount() {
        return ioThreadCount;
    }

    public void refresh() {
        refreshButton.click();
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

    public static class ProgressItem {

        private final int minValue;

        private final int maxValue;

        private final int currentValue;

        public ProgressItem(WebElement containingElement) {
            WebElement progressBarElement = containingElement
                .findElements(By.tagName("div"))
                .stream()
                .filter(element -> element.getAttribute("class").equals(progressBar))
                .findFirst()
                .get();
            this.minValue = Integer.parseInt(progressBarElement.getAttribute("aria-valuemin"));
            this.maxValue = Integer.parseInt(progressBarElement.getAttribute("aria-valuemax"));
            this.currentValue = Integer.parseInt(progressBarElement.getAttribute("aria-valuenow"));
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getMinValue() {
            return minValue;
        }
    }
}
