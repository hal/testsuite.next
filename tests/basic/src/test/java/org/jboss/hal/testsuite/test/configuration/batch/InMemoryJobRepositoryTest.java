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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.jboss.hal.testsuite.util.Console;
import org.jboss.hal.testsuite.util.Notification;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.IN_MEMORY_TO_BE_ADDED;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.IN_MEMORY_TO_BE_REMOVED;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.inMemoryAddress;

@RunWith(Arquillian.class)
public class InMemoryJobRepositoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @Drone private WebDriver browser;
    @Page private BatchPage page;

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(inMemoryAddress(IN_MEMORY_TO_BE_REMOVED));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getInMemoryItem().click();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(inMemoryAddress(IN_MEMORY_TO_BE_ADDED));
        operations.removeIfExists(inMemoryAddress(IN_MEMORY_TO_BE_REMOVED));
    }

    @Test
    public void create() throws Exception {
        page.getInMemoryTable().add();
        AddResourceDialogFragment dialog = Console.withBrowser(browser).addResourceDialog();
        dialog.getForm().text("name", IN_MEMORY_TO_BE_ADDED);
        dialog.add();

        Notification.withBrowser(browser).success();
        new ResourceVerifier(inMemoryAddress(IN_MEMORY_TO_BE_ADDED), client).verifyExists();
    }

    @Test
    public void delete() throws Exception {
        page.getInMemoryTable().remove(IN_MEMORY_TO_BE_REMOVED);

        Notification.withBrowser(browser).success();
        new ResourceVerifier(inMemoryAddress(IN_MEMORY_TO_BE_REMOVED), client).verifyDoesNotExist();
    }
}
