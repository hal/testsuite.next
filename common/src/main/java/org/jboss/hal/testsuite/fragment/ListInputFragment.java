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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.tagManagerTag;
import static org.jboss.hal.resources.CSS.tags;
import static org.jboss.hal.testsuite.Selectors.contains;

public class ListInputFragment {

    @FindBy(css = "input[type=text]." + tags) private WebElement inputElement;

    /** Adds an value to this list item. */
    public void add(String value) {
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        inputElement.sendKeys(Keys.RETURN);
        By tagSelector = ByJQuery.selector("." + tagManagerTag + " > span" + contains(value));
        waitGui().until().element(tagSelector).is().visible();
    }
}
