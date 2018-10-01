package org.jboss.hal.testsuite.page.keycloak;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Abstraction for Access control page variant available only when the Keycloak integration is configured.
 */
@Place(NameTokens.ACCESS_CONTROL_SSO)
public class KeycloakAccessControlPage extends BasePage {

    @FindBy(id = Ids.TLC_ACCESS_CONTROL)
    private WebElement accessControlMenuItem;

    @FindBy(id = "keycloak-form-readonly")
    private WebElement keycloakInfoForm;

    /**
     * Navigate not by URL but by clicking the top level Access Control menu item to ensure this will really
     * open the correct variant of the page.
     */
    public KeycloakAccessControlPage navigateToAccessControlSso() {
        accessControlMenuItem.click();
        Graphene.waitModel().until().element(keycloakInfoForm).is().visible();
        return this;
    }

    public String getFormText(String idFragment) {
        return getFormItem(idFragment).getText();
    }

    public String getFormLink(String idFragment) {
        return getFormItem(idFragment).getAttribute("href");
    }

    private WebElement getFormItem(String idFragment) {
        By selector = By.id("keycloak-form-" + idFragment + "-readonly");
        return keycloakInfoForm.findElement(selector);
    }
}
