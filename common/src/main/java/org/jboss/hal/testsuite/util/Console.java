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

import org.jboss.hal.resources.Ids;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

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
        waitModel(browser).withTimeout(DEFAULT_PAGE_LOAD_TIMEOUT, SECONDS)
                .until().element(By.id(Ids.ROOT_CONTAINER))
                .is().present();
        return this;
    }
}
