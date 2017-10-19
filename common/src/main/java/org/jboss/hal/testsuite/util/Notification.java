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
package org.jboss.hal.testsuite.util;

import org.jboss.hal.resources.CSS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.toastNotificationsListPf;

public class Notification {

    public static Notification withBrowser(WebDriver browser) {
        return new Notification(browser);
    }

    private final WebDriver browser;

    private Notification(WebDriver browser) {
        this.browser = browser;
    }

    public void success() {
        By selector = By.cssSelector("." + toastNotificationsListPf + " > ." + CSS.alertSuccess);
        waitGui(browser).until().element(selector).is().present();
    }
}
