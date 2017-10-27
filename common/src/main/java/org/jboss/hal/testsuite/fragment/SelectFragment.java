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

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.dropdownMenu;
import static org.jboss.hal.resources.CSS.dropdownToggle;
import static org.jboss.hal.resources.CSS.selectpicker;
import static org.jboss.hal.testsuite.Selectors.contains;

/**
 * Fragment for a bootstrap select element. Root should be the div w/ class {@link
 * org.jboss.hal.resources.CSS#bootstrapSelect}
 */
public class SelectFragment {

    @FindBy(css = "button." + dropdownToggle) private WebElement button;
    @FindBy(css = "div." + dropdownMenu) private WebElement menu;
    @FindBy(css = "select." + selectpicker) private WebElement select;

    /** Selects the specified text. */
    public void select(String text) {
        select(text, text);
    }

    /** Selects the specified text which has the specified value in the internal HTML select element. */
    public void select(String text, String value) {
        button.click();
        waitGui().until().element(menu).is().visible();
        menu.findElement(ByJQuery.selector("a" + contains(text))).click();
        waitGui().until().element(select).value().equalTo(value);
    }
}
