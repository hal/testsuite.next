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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.testsuite.Console.DEFAULT_LOAD_TIMEOUT;

/** Fragment for the finder. Use {@link Console#finder(String)} to get an instance. */
public class FinderFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @Inject private Console console;
    private String place;

    /** Selects the specified finder path and waits until the last column ID in the path is present */
    public FinderFragment select(FinderPath path) {
        if (!path.isEmpty()) {
            assertPlace();
            browser.get(console.absoluteUrl(place + ";path=" + path.toString()));
        }
        return this;
    }

    /** Returns the specified column. */
    public ColumnFragment column(String columnId) {
        By selector = By.id(columnId);
        waitGui().withTimeout(DEFAULT_LOAD_TIMEOUT, SECONDS)
                .until().element(selector).is().visible();
        ColumnFragment column = createPageFragment(ColumnFragment.class, browser.findElement(selector));
        column.initColumnId(columnId);
        return column;
    }

    public FinderPreviewFragment preview() {
        By selector = By.id(Ids.PREVIEW_ID);
        waitGui().until().element(selector).is().visible();
        FinderPreviewFragment preview = createPageFragment(FinderPreviewFragment.class, browser.findElement(selector));
        return preview;
    }

    /**
     * Initializes the finder with its place. Must not be called manually. Instead use {@link
     * org.jboss.hal.testsuite.Console#finder(String)} which calls this method automatically.
     */
    public void initPlace(String place) {
        this.place = place;
    }

    private void assertPlace() {
        assert place != null : "No place available. Did you obtain the finder using Console.finder(String)?";
    }
}
