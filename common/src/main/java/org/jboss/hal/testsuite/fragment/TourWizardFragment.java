package org.jboss.hal.testsuite.fragment;

import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.Ids;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class TourWizardFragment {

    private static final String BASIC_WIZARD_CSS_SELECTOR = ".popover[role='tooltip']";

    public static final By WIZARD_SELECTOR = By.cssSelector(BASIC_WIZARD_CSS_SELECTOR);

    private static final By
        WIZARD_CONTENT_SELECTOR = By.cssSelector(BASIC_WIZARD_CSS_SELECTOR + " .popover-content"),
        WIZARD_TITLE_SELECTOR = By.cssSelector(BASIC_WIZARD_CSS_SELECTOR + " .popover-title"),
        WIZARD_CLOSE_SELECTOR = By.cssSelector(BASIC_WIZARD_CSS_SELECTOR + " button.close"),
        WIZARD_BACK_BUTTON_SELECTOR = By.id(Ids.TOUR_BUTTON_BACK),
        WIZARD_NEXT_BUTTON_SELECTOR = By.id(Ids.TOUR_BUTTON_NEXT),
        WIZARD_FINISH_BUTTON_SELECTOR = By.id(Ids.TOUR_BUTTON_DONE);

    @Root protected WebElement root;

    public String getTitle() {
        return root.findElement(WIZARD_TITLE_SELECTOR).getText();
    }

    public String getContent() {
        return root.findElement(WIZARD_CONTENT_SELECTOR).getText();
    }

    public TourWizardFragment close() {
        root.findElement(WIZARD_CLOSE_SELECTOR).click();
        return this;
    }

    public boolean isClosed() {
        try {
            waitGui().until().element(By.cssSelector(BASIC_WIZARD_CSS_SELECTOR)).is().not().present();
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public TourWizardFragment next() {
        return moveInWizard(WIZARD_NEXT_BUTTON_SELECTOR);
    }

    public TourWizardFragment back() {
        return moveInWizard(WIZARD_BACK_BUTTON_SELECTOR);
    }

    public TourWizardFragment finish() {
        root.findElement(WIZARD_FINISH_BUTTON_SELECTOR).click();
        return this;
    }

    private TourWizardFragment moveInWizard(By moveButtonSelector) {
        String oldId = root.findElement(WIZARD_SELECTOR).getAttribute("id");
        root.findElement(moveButtonSelector).click();
        waitGui().until().element(By.cssSelector(BASIC_WIZARD_CSS_SELECTOR + ":not(#" + oldId + ")")).is().visible();
        return this;
    }
}
