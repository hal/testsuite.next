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
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.drawerPf;
import static org.jboss.hal.resources.CSS.drawerPfHal;

/** Fragment for the console header. Use {@link Console#header()} to get an instance. */
public class HeaderFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @FindBy(css = "a[data-element='reloadLink']") private WebElement reload;
    @FindBy(css = "a[data-element='messages']") private WebElement notifications;
    @FindBy(css = "span[data-element='badgeIcon']") private WebElement badgeIcon;
    @FindBy(css = "." + drawerPf + "." + drawerPfHal) private NotificationDrawerFragment notificationDrawer;
    @FindBy(css = "ul[data-element=topLevelCategories] > li > a") private List<WebElement> topLevelCategories;
    @FindBy(css = "ul[data-element=topLevelCategories] > li.active > a") private WebElement selectedTopLevelCategory;
    @FindBy(css = "ol[data-element=breadcrumb]") private HeaderBreadcrumbFragment breadcrumb;

    public NotificationDrawerFragment openNotificationDrawer() {
        notifications.click();
        waitGui().until().element(notificationDrawer.getRoot()).is().visible();
        return notificationDrawer;
    }

    public WebElement getNotifications() {
        return notifications;
    }

    public WebElement getBadgeIcon() {
        return badgeIcon;
    }

    public List<WebElement> getTopLevelCategories() {
        return topLevelCategories;
    }

    public WebElement getSelectedTopLevelCategory() {
        return selectedTopLevelCategory;
    }

    public HeaderBreadcrumbFragment breadcrumb() {
        return breadcrumb;
    }
}
