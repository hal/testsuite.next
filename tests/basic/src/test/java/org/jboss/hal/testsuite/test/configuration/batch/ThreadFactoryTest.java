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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PRIORITY;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ThreadFactoryTest {

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

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private BatchPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("batch-thread-factory-item");

        form = page.getThreadFactoryForm();
        table = page.getThreadFactoryTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(threadFactoryAddress(THREAD_FACTORY_CREATE), table, THREAD_FACTORY_CREATE);
    }

    @Test
    public void read() {
        table.select(THREAD_FACTORY_READ);
        assertEquals(THREAD_FACTORY_READ, form.value(NAME));
    }

    @Test
    public void update() throws Exception {
        String groupName = Random.name();
        int priority = Random.number(1, 10);
        String pattern = Random.name();

        table.select(THREAD_FACTORY_UPDATE);
        crud.update(threadFactoryAddress(THREAD_FACTORY_UPDATE), form,
                f -> {
                    f.text(GROUP_NAME, groupName);
                    f.number(PRIORITY, priority);
                    f.text(THREAD_NAME_PATTERN, pattern);
                }, resourceVerifier -> {
                    resourceVerifier.verifyAttribute(GROUP_NAME, groupName);
                    resourceVerifier.verifyAttribute(PRIORITY, priority);
                    resourceVerifier.verifyAttribute(THREAD_NAME_PATTERN, pattern);
                });
    }

    @Test
    public void updateInvalidPriority() {
        table.select(THREAD_FACTORY_UPDATE);
        crud.updateWithError(form, PRIORITY, 42);
    }

    @Test
    public void reset() throws Exception {
        table.select(THREAD_FACTORY_UPDATE);
        crud.reset(threadFactoryAddress(THREAD_FACTORY_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(threadFactoryAddress(THREAD_FACTORY_DELETE), table, THREAD_FACTORY_DELETE);
    }
}
