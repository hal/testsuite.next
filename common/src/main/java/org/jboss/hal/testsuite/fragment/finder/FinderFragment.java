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
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/** Fragment for the finder. Use {@link Console#finder(String)} to get an instance. */
public class FinderFragment {

    // ------------------------------------------------------ finder path helper methods

    public static FinderPath configurationSubsystemPath(String subsystem) {
        return new FinderPath()
                .append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                .append(Ids.CONFIGURATION_SUBSYSTEM, subsystem);
    }

    @Drone private WebDriver browser;
    @Inject private Console console;
    private String place;

    /** Selects the specified finder path and waits until the last column ID in the path is present */
    public FinderFragment select(FinderPath path) {
        if (!path.isEmpty()) {
            assertPlace();
            browser.get(console.absoluteUrl(place + ";path=" + path.toString()));
            if (path.last().itemId != null) {
                waitModel().until().element(By.id(path.last().itemId)).is().present();
            }
        }
        return this;
    }

    /** Returns the specified column. */
    public ColumnFragment column(String columnId) {
        WebElement element = browser.findElement(By.id(columnId));
        waitModel().until().element(element).is().visible();
        ColumnFragment column = createPageFragment(ColumnFragment.class, element);
        column.initColumnId(columnId);
        return column;
    }

    public FinderPreviewFragment preview() {
        By selector = By.id(Ids.PREVIEW_ID);
        waitGui().until().element(selector).is().visible();
        return createPageFragment(FinderPreviewFragment.class, browser.findElement(selector));
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
