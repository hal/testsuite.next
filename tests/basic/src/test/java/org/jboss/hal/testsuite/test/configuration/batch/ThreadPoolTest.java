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
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.as.domain.management.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ThreadPoolTest {

    private static final String MAX_THREADS = "max-threads";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(threadPoolAddress(THREAD_POOL_READ), Values.empty().and(MAX_THREADS, MAX_THREADS_VALUE));
        operations.add(threadPoolAddress(THREAD_POOL_UPDATE), Values.empty().and(MAX_THREADS, MAX_THREADS_VALUE));
        operations.add(threadPoolAddress(THREAD_POOL_DELETE), Values.empty().and(MAX_THREADS, MAX_THREADS_VALUE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(threadPoolAddress(THREAD_POOL_CREATE));
        operations.removeIfExists(threadPoolAddress(THREAD_POOL_READ));
        operations.removeIfExists(threadPoolAddress(THREAD_POOL_UPDATE));
        operations.removeIfExists(threadPoolAddress(THREAD_POOL_DELETE));
    }

    @Drone private WebDriver browser;
    @Page private BatchPage page;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getThreadPoolItem().click();

        form = page.getThreadPoolForm();
        table = page.getThreadPoolTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, THREAD_POOL_CREATE);
        dialog.getForm().number(MAX_THREADS, MAX_THREADS_VALUE);
        dialog.add();

        console.success();
        new ResourceVerifier(threadPoolAddress(THREAD_POOL_CREATE), client).verifyExists();
    }

    @Test
    public void createNoMaxThreads() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, THREAD_POOL_CREATE);
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(MAX_THREADS);
    }

    @Test
    public void createInvalidMaxThreads() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, THREAD_POOL_CREATE);
        dialog.getForm().number(MAX_THREADS, -1);
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(MAX_THREADS);
    }

    @Test
    public void read() throws Exception {
        table.select(THREAD_POOL_READ);
        assertEquals(THREAD_POOL_READ, form.value(NAME));
        assertEquals(MAX_THREADS_VALUE, form.intValue(MAX_THREADS));
    }

    @Test
    public void update() throws Exception {
        int maxThreads = RandomUtils.nextInt(100, 200);

        table.select(THREAD_POOL_UPDATE);
        form.edit();
        form.number(MAX_THREADS, maxThreads);
        form.save();

        console.success();
        new ResourceVerifier(threadPoolAddress(THREAD_POOL_UPDATE), client)
                .verifyAttribute(MAX_THREADS, maxThreads);
    }

    @Test
    public void updateNoMaxThreads() throws Exception {
        table.select(THREAD_POOL_UPDATE);
        form.edit();
        form.clear(MAX_THREADS);
        form.getSaveButton().click();
        form.expectError(MAX_THREADS);
    }

    @Test
    public void updateInvalidMaxThreads() throws Exception {
        table.select(THREAD_POOL_UPDATE);
        form.edit();
        form.number(MAX_THREADS, -1);
        form.getSaveButton().click();
        form.expectError(MAX_THREADS);
    }

    @Test
    public void delete() throws Exception {
        table.remove(THREAD_POOL_DELETE);

        console.success();
        new ResourceVerifier(threadPoolAddress(THREAD_POOL_DELETE), client).verifyDoesNotExist();
    }
}
