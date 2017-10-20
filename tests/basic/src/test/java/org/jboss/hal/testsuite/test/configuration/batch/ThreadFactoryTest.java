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
package org.jboss.hal.testsuite.test.configuration.batch;

import org.apache.commons.lang3.RandomUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.jboss.hal.testsuite.util.Notification;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.as.domain.management.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ThreadFactoryTest {

    private static final String GROUP_NAME = "group-name";
    private static final String PRIORITY = "priority";
    private static final String THREAD_NAME_PATTERN = "thread-name-pattern";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(threadFactoryAddress(THREAD_FACTORY_READ));
        operations.add(threadFactoryAddress(THREAD_FACTORY_UPDATE));
        operations.add(threadFactoryAddress(THREAD_FACTORY_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_CREATE));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_READ));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_UPDATE));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_DELETE));
    }

    @Drone private WebDriver browser;
    @Page private BatchPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getThreadFactoryItem().click();

        form = page.getThreadFactoryForm();
        table = page.getThreadFactoryTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, THREAD_FACTORY_CREATE);
        dialog.add();

        Notification.withBrowser(browser).success();
        new ResourceVerifier(threadFactoryAddress(THREAD_FACTORY_CREATE), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        table.select(THREAD_FACTORY_READ);
        assertEquals(THREAD_FACTORY_READ, form.value(NAME));
    }

    @Test
    public void update() throws Exception {
        String groupName = GENERATOR.generate(20);
        int priority = RandomUtils.nextInt(1, 10);
        String pattern = GENERATOR.generate(20);

        table.select(THREAD_FACTORY_UPDATE);
        form.edit();
        form.text(GROUP_NAME, groupName);
        form.number(PRIORITY, priority);
        form.text(THREAD_NAME_PATTERN, pattern);
        form.save();

        Notification.withBrowser(browser).success();
        new ResourceVerifier(threadFactoryAddress(THREAD_FACTORY_UPDATE), client)
                .verifyAttribute(GROUP_NAME, groupName)
                .verifyAttribute(PRIORITY, priority)
                .verifyAttribute(THREAD_NAME_PATTERN, pattern);
    }

    @Test
    public void updateInvalidPriority() throws Exception {
        int priority = 42;
        table.select(THREAD_FACTORY_UPDATE);
        form.edit();
        form.number(PRIORITY, priority);
        form.getSaveButton().click();
        form.expectError(PRIORITY);
    }

    @Test
    public void delete() throws Exception {
        table.remove(THREAD_FACTORY_DELETE);

        Notification.withBrowser(browser).success();
        new ResourceVerifier(threadFactoryAddress(THREAD_FACTORY_DELETE), client).verifyDoesNotExist();
    }
}
