package org.jboss.hal.testsuite.page.keycloak;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.*;

/**
 * Abstraction for Keycloak login page which is not part of HAL.
 */
public class KeycloakLoginPage {

    @FindBy(id = "kc-form-login")
    private WebElement loginForm;

    @FindBy(id = "username")
    private WebElement usernameInputElement;

    @FindBy(id = "password")
    private WebElement passwordInputElement;

    @FindBy(id = "kc-login")
    private WebElement saveButtonElement;

    public KeycloakLoginPage waitForLoginForm() {
        Graphene.waitModel().until().element(loginForm).is().visible();
        return this;
    }

    public KeycloakLoginPage login(String username, String password) {
        text(usernameInputElement, username)
        .text(passwordInputElement, password);
        saveButtonElement.click();
        waitModel().until().element(loginForm).is().not().visible();
        return this;
    }

    private KeycloakLoginPage text(WebElement inputElement, String value) {
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        waitGui().until().element(inputElement).value().equalTo(value);
        return this;
    }

}
