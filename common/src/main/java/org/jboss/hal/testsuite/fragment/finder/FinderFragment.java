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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/** Fragment for the finder. Use {@link Console#finder(String)} to get an instance. */
public class FinderFragment {

    public static FinderPath configurationSubsystemPath(String subsystem) {
        return new FinderPath()
                .append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                .append(Ids.CONFIGURATION_SUBSYSTEM, subsystem);
    }

    public static FinderPath runtimeSubsystemPath(String server, String subsystem) {
        return new FinderPath()
            .append(Ids.STANDALONE_SERVER_COLUMN, "standalone-host-" + server)
            .append(Ids.RUNTIME_SUBSYSTEM, subsystem);
    }

    @Drone private WebDriver browser;

    /** Returns the specified column. */
    public ColumnFragment column(String columnId) {
        By selector = By.id(columnId);
        waitModel().until().element(selector).is().present();
        ColumnFragment column = createPageFragment(ColumnFragment.class, browser.findElement(selector));
        column.initColumnId(columnId);
        return column;
    }

    public FinderPreviewFragment preview() {
        By selector = By.id(Ids.PREVIEW_ID);
        waitGui().until().element(selector).is().present();
        return createPageFragment(FinderPreviewFragment.class, browser.findElement(selector));
    }
}
