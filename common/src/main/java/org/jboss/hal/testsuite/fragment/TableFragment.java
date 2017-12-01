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

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.columnAction;
import static org.jboss.hal.resources.CSS.halTableButtons;
import static org.jboss.hal.testsuite.Selectors.contains;

/** Fragment for a data table. */
public class TableFragment {

    @Root private WebElement root;
    @FindBy(css = "." + halTableButtons) private WebElement buttons;
    @Inject private Console console;
    private List<FormFragment> forms;

    public TableFragment() {
        this.forms = new ArrayList<>();
    }

    /** Clicks on the add button and returns an {@link AddResourceDialogFragment}. */
    public AddResourceDialogFragment add() {
        button("Add").click();
        return console.addResourceDialog();
    }

    /** Clicks on the remove button and confirms the confirms the confirmation dialog */
    public void remove(String name) {
        select(name);
        WebElement button = button("Remove");
        waitGui().until().element(button).is().enabled();
        button.click();
        console.confirmationDialog().confirm();
    }

    public WebElement button(String text) {
        By selector = ByJQuery.selector(contains(text));
        return buttons.findElement(selector);
    }

    /**
     * Selects the first {@code td} which contains the specified value, then clicks on it. If forms were bound to this
     * table, {@link FormFragment#view()} is called.
     */
    public void select(String value) {
        By selector = ByJQuery.selector("td" + contains(value));
        root.findElement(selector).click();
        if (!forms.isEmpty()) {
            for (FormFragment form : forms) {
                if (form.getRoot().isDisplayed()) {
                    form.view();
                }
            }
        }
    }

    /** Clicks on the &lt;action&gt; column in the row which contains "&lt;value&gt;". */
    public void action(String value, String action) {
        By selector = ByJQuery.selector("td" + contains(value) + " ~ td > a." + columnAction + contains(action));
        root.findElement(selector).click();
    }

    /**
     * Binds the forms to the table. Calling {@link TableFragment#select(String)} will trigger {@link
     * FormFragment#view()}.
     */
    public void bind(List<FormFragment> forms) {
        this.forms.addAll(forms);
    }

    /**
     * Binds the form to the table. Calling {@link TableFragment#select(String)} will trigger {@link
     * FormFragment#view()}.
     */
    public void bind(FormFragment form) {
        this.forms.add(form);
    }

    public WebElement getRoot() {
        return root;
    }
}
