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

import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.AlertFragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.stream.Collectors.toMap;
import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.alert;
import static org.jboss.hal.resources.CSS.key;
import static org.jboss.hal.resources.CSS.listGroup;
import static org.jboss.hal.resources.CSS.value;

/** Fragment for the finder preview. Use {@link FinderFragment#preview()} to get an instance. */
public class FinderPreviewFragment {

    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    @Inject
    private Console console;

    public AlertFragment getAlert() {
        return createPageFragment(AlertFragment.class, root.findElement(By.className(alert)));
    }

    public Map<String, String> getMainAttributes() {
        return getAttributeElementMap("Main Attributes").entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getText()));
    }

    protected Map<String, WebElement> getAttributeElementMap(String heading) {
        By attributeSelector =
            ByJQuery.selector("h2:contains('" + heading + "'):visible ~ ul." + listGroup + ":eq(0) > li:visible");
        waitGui().until().element(root, attributeSelector).is().present();
        return root.findElements(attributeSelector).stream().collect(toMap(attributeElement -> {
            return attributeElement.findElement(By.className(key)).getText();
        }, attributeElement -> attributeElement.findElement(By.className(value))));
    }
}
