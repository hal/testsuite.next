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
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.finderItem;

/** Page fragment for one finder column. Use {@link FinderFragment#column(String)} to get an instance. */
public class ColumnFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "ul > li." + finderItem) private List<WebElement> items;
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
        return items.stream().anyMatch(item -> itemId.equals(item.getAttribute("id")));
    }

    public ItemFragment selectItem(String itemId) {
        By selector = By.id(itemId);
        ItemFragment item = createPageFragment(ItemFragment.class, browser.findElement(selector));
        waitGui().until().element(selector).is().visible();
        item.getRoot().click();
        item.initItemId(itemId);
        return item;
    }

    public List<WebElement> getItems() {
        return items;
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
