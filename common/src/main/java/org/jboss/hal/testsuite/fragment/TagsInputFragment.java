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
import java.util.Map;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.tagManagerTag;
import static org.jboss.hal.resources.CSS.tags;
import static org.jboss.hal.testsuite.Selectors.contains;

public class TagsInputFragment {

    @FindBy(css = "input[type=text]." + tags) private WebElement inputElement;
    @FindBy(className = "tag-manager-container") private WebElement tagsElement;

    /** Adds the values to this list item. */
    public TagsInputFragment add(List<String> values) {
        for (String value : values) {
            internalAdd(value);
        }
        return this;
    }

    /** Adds a value to this list item. */
    public TagsInputFragment add(String value) {
        return internalAdd(value);
    }

    /** Adds the name/value pairs to this properties item. */
    public TagsInputFragment add(ModelNode values) {
        for (Property property : values.asPropertyList()) {
            internalAdd(property.getName() + "=" + property.getValue().asString());
        }
        return this;
    }

    /** Adds the name/value pairs to this properties item. */
    public TagsInputFragment add(Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            internalAdd(entry.getKey() + "=" + entry.getValue());
        }
        return this;
    }

    /** Adds a name/value pair to this properties item. */
    public TagsInputFragment add(String name, String value) {
        return internalAdd(name + "=" + value);
    }

    public void removeTags() {
        List<WebElement> tagRemoves = tagsElement.findElements(ByJQuery.className("tm-tag-remove"));
        int size = tagRemoves.size();
        for (int i = size - 1; i >= 0; i--) {
            // as each tag is removed, it is also removed from the list, so start removing from the end,
            tagRemoves.get(i).click();
        }
    }

    private TagsInputFragment internalAdd(String value) {
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        inputElement.sendKeys(Keys.RETURN);
        By tagSelector = ByJQuery.selector("." + tagManagerTag + " > span" + contains(value));
        waitGui().until().element(tagSelector).is().visible();
        return this;
    }
}
