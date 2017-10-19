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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.Ids;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.btnPrimary;
import static org.jboss.hal.resources.CSS.editing;
import static org.jboss.hal.resources.CSS.formButtons;
import static org.jboss.hal.resources.CSS.readonly;

/** Page fragment for a form using read-only and editing states. */
public class FormFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "a[data-operation=edit]") private WebElement editLink;
    @FindBy(css = "." + formButtons + " ." + btnPrimary) private WebElement saveButton;
    @FindBy(css = "." + readonly) private WebElement readOnlySection;
    @FindBy(css = "." + editing) private WebElement editingSection;

    // ------------------------------------------------------ edit mode

    public void edit() {
        editLink.click();
        waitGui().until().element(editingSection).is().visible();
    }

    public void save() {
        saveButton.click();
        waitGui().until().element(readOnlySection).is().visible();
    }

    public void text(String name, String value) {
        WebElement inputElement = inputElement(name);
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        waitGui().until().element(inputElement).value().equalTo(value);
    }

    public void checkbox(String name, boolean value) {
        WebElement inputElement = inputElement(name);
        boolean inputValue = parseBoolean(inputElement.getAttribute("value"));
        if (inputValue != value) {
            WebElement switchContainer = root.findElement(By.cssSelector(".bootstrap-switch-id-" + editingId(name)));
            switchContainer.click();
            if (value) {
                waitGui().until().element(inputElement).is().selected();
            } else {
                waitGui().until().element(inputElement).is().not().selected();
            }
        }
    }

    private boolean parseBoolean(String text) {
        return "on".equals(text) || Boolean.parseBoolean(text);
    }

    private WebElement inputElement(String name) {
        return browser.findElement(By.id(editingId(name)));
    }

    private String editingId(String name) {
        String id = root.getAttribute("id");
        return Ids.build(id, name, "editing");
    }
}
