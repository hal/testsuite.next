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

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.core.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.hal.resources.CSS.dropdown;

/** Fragment for the breadcrumb element in the header. Use {@link HeaderFragment#breadcrumb()} to get an instance. */
public class HeaderBreadcrumbFragment {

    // Must match org.jboss.hal.client.skeleton.HeaderPresenter#MAX_BREADCRUMB_VALUE_LENGTH
    private static final int MAX_BREADCRUMB_VALUE_LENGTH = 20;

    /** Abbreviates the middle part of the specified value if necessary in the same way the console does. */
    public static String abbreviate(String value) {
        if (value.length() > MAX_BREADCRUMB_VALUE_LENGTH) {
            return Strings.abbreviateMiddle(value, MAX_BREADCRUMB_VALUE_LENGTH);
        }
        return value;
    }


    @Drone private WebDriver browser;
    @Root private WebElement root;

    public String lastValue() {
        String value;
        // last li contains the tool icons
        WebElement valueElement = root.findElement(By.cssSelector("li:nth-last-child(2) > span.value"));
        String classList = valueElement.getAttribute("class");
        if (classList.contains(dropdown)) {
            value = StringUtils.strip(valueElement.findElement(By.cssSelector("a > span")).getText());
        } else {
            value = StringUtils.strip(valueElement.getText());
        }
        return value;
    }
}
