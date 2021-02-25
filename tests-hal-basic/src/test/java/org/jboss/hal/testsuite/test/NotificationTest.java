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
package org.jboss.hal.testsuite.test;

import java.util.HashSet;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.HeaderFragment;
import org.jboss.hal.testsuite.fragment.NotificationDrawerFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SystemPropertyPage;
import org.jboss.hal.testsuite.util.Library;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.fixtures.SystemPropertyFixtures.systemPropertyAddress;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Creates 10 system properties to trigger some notifications. */
@RunWith(Arquillian.class)
public class NotificationTest {

    private static final int COUNT = 2;
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @Inject private Console console;
    @Page private SystemPropertyPage page;
    private HeaderFragment header;
    private HashSet<String> properties;

    @Before
    public void setUp() {
        page.navigate();
        TableFragment table = page.getTable();
        header = console.header();
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.clearAll(); // to get rid of any "Please reload" notifications
        notificationDrawer.close();

        properties = new HashSet<>();
        for (int i = 0; i < COUNT; i++) {
            String name = Ids.build("system-property", NAME, String.valueOf(i), Random.name());
            String value = Ids.build("system-property", VALUE, String.valueOf(i), Random.name());

            AddResourceDialogFragment dialog = table.add();
            dialog.getForm().text(NAME, name);
            dialog.getForm().text(VALUE, value);
            dialog.add();
            Library.letsSleep(500);

            properties.add(name);
        }
    }

    @After
    public void tearDown() throws Exception {
        for (String property : properties) {
            operations.removeIfExists(systemPropertyAddress(property));
        }
    }

    @Test
    public void badge() {
        assertTrue(header.getBadgeIcon().isDisplayed());
    }

    @Test
    public void close() {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.getClose().click();
        waitGui().until().element(notificationDrawer.getRoot()).is().not().visible();
    }

    @Test
    public void unreadNotifications() {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        assertEquals(COUNT, notificationDrawer.getNotifications().size());
        assertTrue(notificationDrawer.getMarkAllRead().isDisplayed());
        assertTrue(notificationDrawer.getClearAll().isDisplayed());
    }

    @Test
    public void markAllRead() {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.getMarkAllRead().click();
        waitGui().until().element(notificationDrawer.getMarkAllRead()).is().not().visible();
    }

    @Test
    public void clearAll() {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.clearAll();
        waitGui().until().element(notificationDrawer.getMarkAllRead()).is().not().visible();
        waitGui().until().element(notificationDrawer.getClearAll()).is().not().visible();
    }
}
