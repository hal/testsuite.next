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

import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Page fragment of one finder column. Columns must be obtained using {@link FinderFragment#column(String)}. */
public class ColumnFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "ul > li." + CSS.finderItem) List<WebElement> items;
    @Inject private Console console;
    private String columnId;

    void initId(String columnId) {
        this.columnId = columnId;
    }

    public List<WebElement> getItems() {
        return items;
    }

    public boolean containsItem(String itemId) {
        return items.stream().anyMatch(item -> itemId.equals(item.getAttribute("id")));
    }

    /**
     * Shortcut for {@code action("<columnId>-add").click()}. Expects that the action opens an add resource dialog and
     * returns it.
     */
    public AddResourceDialogFragment add() {
        action(Ids.build(columnId, Ids.ADD)).click();
        return console.addResourceDialog();
    }

    /** Returns the specified column action */
    public WebElement action(String id) {
        return root.findElement(By.id(id));
    }
}
