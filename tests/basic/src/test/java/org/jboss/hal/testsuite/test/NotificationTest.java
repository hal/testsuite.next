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

import org.apache.commons.text.RandomStringGenerator;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
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
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.SystemPropertyFixtures.systemPropertyAddress;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Creates 10 system properties to trigger some notifications. */
@RunWith(Arquillian.class)
public class NotificationTest {

    private static final int COUNT = 2;
    private static final RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z')
            .build();
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @Drone private WebDriver browser;
    @Page private SystemPropertyPage page;
    @Inject private Console console;
    private HeaderFragment header;
    private HashSet<String> properties;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        TableFragment table = page.getTable();
        header = console.header();

        properties = new HashSet<>();
        for (int i = 0; i < COUNT; i++) {
            String name = Ids.build(Ids.SYSTEM_PROPERTY, NAME, String.valueOf(i), GENERATOR.generate(10));
            String value = Ids.build(Ids.SYSTEM_PROPERTY, VALUE, String.valueOf(i), GENERATOR.generate(10));

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
    public void badge() throws Exception {
        assertTrue(header.getBadgeIcon().isDisplayed());
    }

    @Test
    public void close() throws Exception {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.getClose().click();
        waitGui().until().element(notificationDrawer.getRoot()).is().not().visible();
    }

    @Test
    public void unreadNotifications() throws Exception {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        assertEquals(COUNT, notificationDrawer.getNotifications().size());
        assertTrue(notificationDrawer.getMarkAllRead().isDisplayed());
        assertTrue(notificationDrawer.getClearAll().isDisplayed());
    }

    @Test
    public void markAllRead() throws Exception {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.getMarkAllRead().click();
        waitGui().until().element(notificationDrawer.getMarkAllRead()).is().not().visible();
    }

    @Test
    public void clearAll() throws Exception {
        NotificationDrawerFragment notificationDrawer = header.openNotificationDrawer();
        notificationDrawer.getClearAll().click();
        waitGui().until().element(notificationDrawer.getBlankSlate()).is().visible();
    }
}
