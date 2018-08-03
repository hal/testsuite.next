/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.fragment;

import com.google.common.base.Strings;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.*;
import static org.jboss.hal.resources.UIConstants.MASK_CHARACTER;

/** Page fragment for a form using read-only and editing states. */
public class FormFragment {

    private static final String DOT = ".";

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @Inject private Console console;
    @FindBy(css = "a[data-operation=edit]") private WebElement editLink;
    @FindBy(css = "a[data-operation=reset]") private WebElement resetLink;
    @FindBy(css = "a[data-operation=remove]") private WebElement removeLink;
    @FindBy(css = DOT + formButtons + " ." + btnDefault) private WebElement cancelButton;
    @FindBy(css = DOT + formButtons + " ." + btnPrimary) private WebElement saveButton;
    @FindBy(css = DOT + readonly) private WebElement readOnlySection;
    @FindBy(css = DOT + editing) private WebElement editingSection;
    @FindBy(css = DOT + blankSlatePf) private WebElement blankSlate;


    // ------------------------------------------------------ empty mode

    public EmptyState emptyState() {
        String emptyId = Ids.build(rootId(), Ids.EMPTY);
        return createPageFragment(EmptyState.class, root.findElement(By.id(emptyId)));
    }

    /**
     * @return true if a blank section is visible, false otherwise
     */
    public boolean isBlank() {
        return root.findElements(ByJQuery.selector(DOT + blankSlatePf + ":visible")).size() > 0;
    }

    /**
    * Waits until the blank section is visible. If this form is part of a tab pane, make sure to {@linkplain
    * TabsFragment#select(String) select} the tab first!
    */
   public void viewBlank() {
       waitGui().until().element(blankSlate).is().visible();
   }

    // ------------------------------------------------------ read-only mode

    /**
     * Waits until the read-only section is visible. If this form is part of a tab pane, make sure to {@linkplain
     * TabsFragment#select(String) select} the tab first!
     */
    public void view() {
        waitGui().until().element(readOnlySection).is().visible();
    }

    /** Returns the value of the specified attribute in the read-only section. */
    public String value(String name) {
        return browser.findElement(By.id(readOnlyId(name))).getText();
    }

    public int intValue(String name) {
        return Integer.parseInt(value(name));
    }

    /** Shows the sensitive value of the specified field in read-only mode */
    public void showSensitive(String name) {
        WebElement element = root.findElement(By.cssSelector("#" + readOnlyId(name) + " + .fa-eye"));
        console.scrollIntoView(element).click();
        waitGui().until().element(By.cssSelector("#" + readOnlyId(name) + " + .fa-eye-slash")).is().present();
    }

    /** Verifies if the value of the specified field in read-only mode is show as masked value */
    public boolean isMasked(String name) {
        String value = value(name);
        String expected = Strings.repeat(MASK_CHARACTER, value.length());
        return value.equals(expected);
    }


    // ------------------------------------------------------ edit mode

    /** Clicks on the edit link and waits until the editing section is visible. */
    public void edit() {
        editLink.click();
        waitGui().until().element(editingSection).is().visible();
        // wait until the first input element has focus
        try {
            waitGui().until().element(By.cssSelector(DOT + editing + " input:first-of-type:focus")).is().present();
        } catch (TimeoutException ignored) {
            // some forms have a disabled first input field
        }
    }

    /**
     * Clicks on the save button w/o waiting for the read-only section to become visible. Use this method if you expect
     * (validation) errors when saving the form
     */
    public void trySave() {
        console.scrollIntoView(saveButton).click();
    }

    /**
     * Saves the form and waits until the read-only section is visible. Expects no errors. If you expect errors, use
     * {@link #trySave()} instead.
     */
    public void save() {
        console.scrollIntoView(saveButton).click();
        waitGui().until().element(readOnlySection).is().visible();
    }

    /**
     * Cancels the form and waits until the read-only section is visible. Expects no errors.
     */
    public void cancel() {
        console.scrollIntoView(cancelButton).click();
        waitGui().until().element(readOnlySection).is().visible();
    }

    /** Changes the specified text input element. */
    public void text(String name, String value) {
        console.waitNoNotification(); // sometime notification interfere with text input
        WebElement inputElement = inputElement(name);
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        waitGui().until().element(inputElement).value().equalTo(value);
    }

    /** @return value of the text input */
    public String text(String name) {
        console.waitNoNotification(); // sometime notification interfere with text input
        return inputElement(name).getAttribute("value");
    }

    /**
     * Enters text with firing additional change event if console does not register the original one.
     * <b> Use only as a dirty workaround when {@link #text(String, String)} is not working properly via Selenium!</b>
     * @param identifier identifier of element to be written to
     * @param value a value to be entered
     */
    public FormFragment editTextFiringExtraChangeEvent(String identifier, String value) {
        text(identifier, value);
        JavascriptExecutor js = (JavascriptExecutor) browser;
        js.executeScript("var evt = document.createEvent('HTMLEvents');"
                + "evt.initEvent('change', true, true);"
                + "arguments[0].dispatchEvent(evt);", inputElement(identifier));
        return this;
    }

    /** Changes the specified number input element. */
    public void number(String name, long value) {
        text(name, String.valueOf(value));
    }

    /** Changes the specified number input element. */
    public void number(String name, double value) {
        text(name, String.valueOf(value));
    }

    /** Changes the specified number input element. */
    public void number(String name, int value) {
        text(name, String.valueOf(value));
    }

    /** Returns a list input fragment for the specified form item. */
    public TagsInputFragment list(String name) {
        return createPageFragment(TagsInputFragment.class, formGroup(name));
    }

    /** Returns a properties input fragment for the specified form item. */
    public TagsInputFragment properties(String name) {
        return createPageFragment(TagsInputFragment.class, formGroup(name));
    }

    /** Changes the specified bootstrap select element. */
    public void select(String name, String value) {
        console.waitNoNotification();
        WebElement formGroup = formGroup(name);
        WebElement selectElement = formGroup.findElement(By.cssSelector(DOT + bootstrapSelect + DOT + formControl));
        try {
            console.scrollIntoView(selectElement);
            SelectFragment select = createPageFragment(SelectFragment.class, selectElement);
            select.select(value);
        } catch (ElementClickInterceptedException e) {
            console.scrollIntoView(selectElement, "{block: \"center\"}");
            SelectFragment select = createPageFragment(SelectFragment.class, selectElement);
            select.select(value);
        }
    }

    /** Changes the specified bootstrap switch element. */
    public FormFragment flip(String name, boolean value) {
        WebElement inputElement = inputElement(name);
        boolean inputValue = inputElement.isSelected();
        if (inputValue != value) {
            WebElement switchElement = root.findElement(By.cssSelector(".bootstrap-switch-id-" + editingId(name) + " .bootstrap-switch-label"));
            try {
                console.scrollIntoView(switchElement).click();
            } catch (ElementClickInterceptedException e) {
                console.scrollIntoView(switchElement, "{block: \"center\"}").click();
            }
            if (value) {
                waitGui().until().element(inputElement).is().selected();
            } else {
                waitGui().until().element(inputElement).is().not().selected();
            }
        }
        return this;
    }

    public void clear(String name) {
        console.waitNoNotification();
        WebElement inputElement = inputElement(name);
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
    }

    /** Expects an error for the specified attribute */
    public FormFragment expectError(String name) {
        By selector = By.cssSelector(DOT + hasError + "[data-form-item-group=" + editingId(name) + "]");
        WebElement formItemGroup = editingSection.findElement(selector);
        WebElement helpBlock = formItemGroup.findElement(By.cssSelector(DOT + CSS.helpBlock));

        waitGui().until().element(formItemGroup).is().visible();
        waitGui().until().element(helpBlock).is().visible();
        return this;
    }

    private WebElement formGroup(String name) {
        return browser.findElement(By.cssSelector("div[data-form-item-group=" + editingId(name) + "]"));
    }

    private WebElement inputElement(String name) {
        return browser.findElement(By.id(editingId(name)));
    }


    // ------------------------------------------------------ reset & remove

    /** Clicks on the reset link and confirms the confirmation dialog */
    public void reset() {
        resetLink.click();
        console.confirmationDialog().confirm();
    }

    /** Clicks on the remove link and confirms the confirmation dialog */
    public void remove() {
        WebElement removeLink = root.findElement(ByJQuery.selector("a[data-operation=remove]:visible"));

        // getting rid of possible tooltips hiding links
        By tooltipSelector = By.className("tooltip");
        if (!root.findElements(tooltipSelector).isEmpty()) {
            new Actions(browser).moveToElement(removeLink).perform();
            waitGui().until().element(root, tooltipSelector).is().not().present();
        }

        removeLink.click();
        console.confirmationDialog().confirm();
        waitGui().until().element(blankSlate).is().visible();
    }

    // ------------------------------------------------------ properties

    public WebElement getRoot() {
        return root;
    }


    // ------------------------------------------------------ helper methods

    private String editingId(String name) {
        return Ids.build(rootId(), name, "editing");
    }

    private String readOnlyId(String name) {
        return Ids.build(rootId(), name, "readonly");
    }

    private String rootId() {
        return root.getAttribute("id");
    }
}
