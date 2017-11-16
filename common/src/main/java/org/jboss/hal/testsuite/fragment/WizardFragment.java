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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.*;

/** Page fragment for wizards. Use {@link Console#wizard()} to get an instance. */
public class WizardFragment {

    @Root private WebElement root;
    @FindBy(css = "." + modalFooter + " ." + btnPrimary) private WebElement primaryButton;
    @FindBy(css = "." + modalFooter + " ." + btnCancel) private WebElement cancelButton;

    /** Clicks on next and returns immediately */
    public void next() {
        primaryButton.click();
    }

    /** Clicks on next and waits until the element with the specified ID (which must be part of this wizard) is visible */
    public void next(String waitForId) {
        next(By.id(waitForId));
    }

    /** Clicks on next and waits until the specified element (which must be part of this wizard) is visible */
    public void next(By waitFor) {
        primaryButton.click();
        WebElement element = root.findElement(waitFor);
        waitGui().until().element(element).is().visible();
    }

    public void cancel() {
        cancelButton.click();
        waitGui().until().element(root).is().not().visible();
    }

    /** Clicks on finish and expects the wizard is closed */
    public void finish() {
        primaryButton.click();
        waitGui().until().element(root).is().not().visible();
    }

    /** Clicks on finish and expects the wizard is <em>not</em> closed */
    public void finishStayOpen() {
        primaryButton.click();
    }

    public void close() {
        primaryButton.click();
        waitGui().until().element(root).is().not().visible();
    }

    public void verifySuccess() {
        verifySuccess(waitGui());
    }

    public void verifySuccess(WebDriverWait<Void> wait) {
        wait.until().element(By.cssSelector("." + wizardPfComplete + " ." + wizardPfSuccessIcon)).is().visible();
    }

    public FormFragment getForm(String id) {
        WebElement formElement = root.findElement(By.id(id));
        waitGui().until().element(formElement).is().visible();
        return Graphene.createPageFragment(FormFragment.class, formElement);
    }

    public WebElement getRoot() {
        return root;
    }
}
