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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;

/** Fragment for the finder. Use {@link Console#finder(String)} to get an instance. */
public class FinderFragment {

    @Drone private WebDriver browser;
    @Root private WebElement root;
    @Inject private Console console;
    private String token;

    /**
     * Initializes the finder with its token. Must not be called manually. Instead use {@link
     * org.jboss.hal.testsuite.Console#finder(String)} which calls this method automatically.
     */
    public void initToken(String token) {
        this.token = token;
    }

    /** Selects the specified finder path and waits until the last column ID in the path is present */
    public void select(FinderPath path) {
        if (!path.isEmpty()) {
            assert token != null : "No token available. Did you obtain the finder using Console.finder(String)?";
            console.waitUntilLoaded(path.getLastColumnId());
        }
    }

    /** Returns the specified column. */
    public ColumnFragment column(String columnId) {
        ColumnFragment column = createPageFragment(ColumnFragment.class, browser.findElement(By.id(columnId)));
        column.initId(columnId);
        return column;
    }
}
