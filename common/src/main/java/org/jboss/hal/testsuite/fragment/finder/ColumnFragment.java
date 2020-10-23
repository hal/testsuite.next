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
package org.jboss.hal.testsuite.fragment.finder;

import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.active;
import static org.jboss.hal.resources.CSS.finderItem;

/** Page fragment for one finder column. Use {@link FinderFragment#column(String)} to get an instance. */
public class ColumnFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @Inject private Console console;
    private String columnId;


    // ------------------------------------------------------ column

    /**
     * Shortcut for {@code action("<columnId>-add").click()}. Expects that the action opens an add resource dialog and
     * returns it.
     */
    public AddResourceDialogFragment add() {
        assertColumnId();
        action(Ids.build(columnId, Ids.ADD)).click();
        return console.addResourceDialog();
    }

    /**
     * Shortcut for {@code action("<columnId>-refresh").click()}.
     */
    public void refresh() {
        assertColumnId();
        action(Ids.build(columnId, Ids.REFRESH)).click();
    }

    /** Opens the action dropdown and clicks on the specified action */
    public void dropdownAction(String dropdownId, String actionId) {
        root.findElement(By.id(dropdownId)).click();
        waitGui().until().element(By.cssSelector("ul[aria-labelledby=" + dropdownId + "]")).is().visible();
        action(actionId).click();
    }

    public boolean hasDropdownActions(String dropdownId, String... actionIds) {
        root.findElement(By.id(dropdownId)).click();
        waitGui().until().element(By.cssSelector("ul[aria-labelledby=" + dropdownId + "]")).is().visible();
        for (String actionId: actionIds) {
            if (action(actionId) == null) {
                return false;
            }
        }
        return true;
    }

    /** Returns the specified column action */
    public WebElement action(String id) {
        return root.findElement(By.id(id));
    }

    public void filter(String filter) {
        assertColumnId();
        WebElement filterInput = root.findElement(By.id(Ids.build(columnId, "filter")));
        filterInput.sendKeys(filter);
    }


    // ------------------------------------------------------ items

    public boolean containsItem(String itemId) {
        return getItems().stream().anyMatch(item -> itemId.equals(item.getAttribute("id")));
    }

    public ItemFragment selectItem(String itemId) {
        By selector = By.id(itemId);
        waitGui().until().element(selector).is().visible();
        ItemFragment item = createPageFragment(ItemFragment.class, browser.findElement(selector));
        item.getRoot().click();
        item.initItemId(itemId);
        return item;
    }

    public boolean isSelected(String itemId) {
        By selector = By.cssSelector("#" + itemId + "." + active);
        waitGui().until().element(selector).is().visible();
        return true;
    }

    public List<WebElement> getItems() {
        return root.findElements(By.cssSelector("ul > li." + finderItem));
    }


    // ------------------------------------------------------ internals

    /**
     * Initializes the column with its ID. Must not be called manually. Instead use {@link
     * FinderFragment#column(String)} which calls this method automatically.
     */
    @SuppressWarnings("WeakerAccess") // public bc it's called from generated proxies
    public void initColumnId(String columnId) {
        this.columnId = columnId;
    }

    private void assertColumnId() {
        assert columnId != null : "No column ID available. Did you obtain the column using FinderFragment.column(String)?";
    }
}
