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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.Ids;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.CSS.blankSlatePf;

public class NotificationDrawerFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "." + blankSlatePf) private WebElement blankSlate;
    @FindBy(id = Ids.NOTIFICATION_DRAWER_CLOSE) private WebElement close;
    @FindBy(id = Ids.NOTIFICATION_DRAWER_CLEAR_ALL) private WebElement clearAll;
    @FindBy(id = Ids.NOTIFICATION_DRAWER_MARK_ALL_READ) private WebElement markAllRead;

    public WebElement getRoot() {
        return root;
    }

    public WebElement getBlankSlate() {
        return blankSlate;
    }

    public WebElement getClose() {
        return close;
    }

    public List<WebElement> getNotifications() {
        return root.findElements(By.cssSelector("div[id^=gwt-uid]"));
    }

    public WebElement getClearAll() {
        return clearAll;
    }

    public WebElement getMarkAllRead() {
        return markAllRead;
    }
}
