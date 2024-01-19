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

import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.*;

/**
 * Fragment for the console header. Use {@link Console#header()} to get an instance.
 */
public class HeaderFragment {

    @FindBy(id = Ids.RELOAD_LINK)
    private WebElement reload;

    @FindBy(id = Ids.NONE_PROGRESSING_LINK)
    private WebElement nonProgressingOp;

    @FindBy(id = Ids.MESSAGES_LINK)
    private WebElement notifications;

    @FindBy(id = Ids.BADGE_ICON)
    private WebElement badgeIcon;

    @FindBy(css = "." + drawerPf + "." + drawerPfHal)
    private NotificationDrawerFragment notificationDrawer;

    @FindBy(css = "ul." + nav + "." + navbarNav + "." + navbarPrimary + " > li > a")
    private List<WebElement> topLevelCategories;

    @FindBy(css = "ul." + nav + "." + navbarNav + "." + navbarPrimary + " > li.active > a")
    private WebElement selectedTopLevelCategory;

    @FindBy(css = "ol." + CSS.breadcrumb + "." + halBreadcrumb)
    private HeaderBreadcrumbFragment breadcrumb;

    public NotificationDrawerFragment openNotificationDrawer() {
        notifications.click();
        waitGui().until().element(notificationDrawer.getRoot()).is().visible();
        return notificationDrawer;
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

    public WebElement getNonProgressingOp() {
        return nonProgressingOp;
    }
}
