package org.jboss.hal.testsuite.page.deployment;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static java.lang.String.join;
import static org.jboss.hal.meta.token.NameTokens.DEPLOYMENT;

@Place(DEPLOYMENT)
public class DeploymentContentPage extends BasePage {

    @FindBy(id = Ids.CONTENT_TREE)
    private WebElement contentTree;

    @FindBy(css = "#" + Ids.CONTENT_TAB + " ." + CSS.btnToolbar)
    private WebElement buttonToolbar;

    public DeploymentContentPage navigateToDeploymentContent(String deploymentName) {
        navigate(DEPLOYMENT, deploymentName);
        return this;
    }

    public boolean isNodeVisible(String... path) {
        return !getNodeElementList(path).isEmpty();
    }

    public DeploymentContentPage selectNode(String... path) {
        WebElement node = getNode(path);
        String ariaSelected = "aria-selected";
        String selectedAttrValue = node.findElement(By.tagName("a")).getAttribute(ariaSelected);
        if (selectedAttrValue == null) {
            throw new IllegalStateException(ariaSelected + " attribute does not exist.");
        }
        boolean selected = Boolean.valueOf(selectedAttrValue);
        if (!selected) {
            node.click();
            Graphene.waitGui().until().element(node.findElement(By.tagName("a"))).attribute(ariaSelected).contains("true");
        }
        return this;
    }

    public DeploymentContentPage openNode(String... path) {
        WebElement node = getNode(path);
        String expandedAttrValue = node.getAttribute("class");
        boolean expanded = expandedAttrValue.contains("jstree-open");
        if (!expanded) {
            node.findElement(By.cssSelector("i.jstree-icon")).click();
            Graphene.waitGui().until().element(node).attribute("class").contains("jstree-open");
        }
        return this;
    }

    public boolean isButtonAvailable(String title) {
        List<WebElement> buttonList = getButtonList(title);
        if (!buttonList.isEmpty()) {
            WebElement button = buttonList.get(0);
            if (button.isDisplayed() && !button.getAttribute("class").contains(CSS.disabled)) {
                return true;
            }
        }
        return false;
    }

    public DeploymentContentPage selectButton(String title) {
        if (!isButtonAvailable(title)) {
            throw new IllegalStateException("Button with title '" + title + "' is not available at the moment.");
        }
        getButtonList(title).get(0).click();
        return this;
    }

    public DialogFragment dialog() {
        return console.dialog();
    }

    public DeploymentContentPage confirm() {
        console.confirmationDialog().confirm();
        return this;
    }

    private List<WebElement> getNodeElementList(String... path) {
        String id = "bct-" + join("", path).replace(".", "") + "-node";
        return contentTree.findElements(By.id(id));
    }

    private WebElement getNode(String... path) {
        List<WebElement> nodes = getNodeElementList(path);
        if (nodes.isEmpty()) {
            throw new IllegalStateException("Node specified by path '" + join(";", path) + "' is not visible.");
        } else if (nodes.size() > 1) {
            throw new IllegalStateException("Node specified by path '" + join(";", path) + "' is ambiguous.");
        }
        WebElement node = nodes.get(0);
        return node;
    }

    private List<WebElement> getButtonList(String title) {
        List<WebElement> buttonList = buttonToolbar.findElements(By.cssSelector("." + CSS.btn + "[title='" + title + "']"));
        return buttonList;
    }

}
