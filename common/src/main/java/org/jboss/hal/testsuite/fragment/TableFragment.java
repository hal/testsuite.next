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
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.util.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.halTableButtons;

public class TableFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "." + halTableButtons) private WebElement buttons;

    /** Clicks on the add button */
    public void add() {
        button("Add").click();
    }

    /** Clicks on the remove button and confirms the confirms the confirmation dialog */
    public void remove(String name) {
        WebElement button = button("Remove");
        By selector = ByJQuery.selector("td:contains('" + name + "')");
        root.findElement(selector).click();
        waitGui().until().element(button).is().enabled();
        button.click();
        Console.withBrowser(browser).dialog().primaryButton(); // confirm
    }

    private WebElement button(String text) {
        By selector = ByJQuery.selector(":contains('" + text + "')");
        return buttons.findElement(selector);
    }
}
