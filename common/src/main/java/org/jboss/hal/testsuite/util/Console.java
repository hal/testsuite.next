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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class Console {

    private static final long DEFAULT_PAGE_LOAD_TIMEOUT = 30;

    public static Console withBrowser(WebDriver browser) {
        return new Console(browser);
    }

    private final WebDriver browser;

    private Console(WebDriver browser) {
        this.browser = browser;
    }

    /** Waits until the console is loaded. */
    public Console waitUntilLoaded() {
        waitGui().withTimeout(DEFAULT_PAGE_LOAD_TIMEOUT, SECONDS)
                .until().element(By.id(Ids.ROOT_CONTAINER))
                .is().present();
        return this;
    }

    /** Returns the currently opened dialog */
    public DialogFragment dialog() {
        return dialog(DialogFragment.class);
    }

    /** Returns the currently opened add resource dialog */
    public AddResourceDialogFragment addResourceDialog() {
        return dialog(AddResourceDialogFragment.class);
    }

    private <T extends DialogFragment> T dialog(Class<T> dialogClass) {
        WebElement dialogElement = browser.findElement(By.id(Ids.HAL_MODAL));
        T dialog = Graphene.createPageFragment(dialogClass, dialogElement);
        waitGui().until().element(dialogElement).is().visible();
        return dialog;
    }
}
