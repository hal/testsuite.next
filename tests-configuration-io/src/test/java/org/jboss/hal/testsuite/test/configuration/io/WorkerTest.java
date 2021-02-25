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
package org.jboss.hal.testsuite.test.configuration.io;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.IOPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IO_THREADS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.WORKER_CREATE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.WORKER_DELETE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.WORKER_READ;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.WORKER_UPDATE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.workerAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class WorkerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(workerAddress(WORKER_READ), Values.empty().and(IO_THREADS, 11));
        operations.add(workerAddress(WORKER_UPDATE), Values.empty().and(IO_THREADS, 123));
        operations.add(workerAddress(WORKER_DELETE), Values.empty().and(IO_THREADS, 321));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(workerAddress(WORKER_CREATE));
        operations.removeIfExists(workerAddress(WORKER_READ));
        operations.removeIfExists(workerAddress(WORKER_UPDATE));
        operations.removeIfExists(workerAddress(WORKER_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private IOPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("io-worker-item");

        form = page.getWorkerForm();
        table = page.getWorkerTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(workerAddress(WORKER_CREATE), table,
                form -> {
                    form.text(NAME, WORKER_CREATE);
                    form.number(IO_THREADS, 12);
                    form.number("stack-size", 1024);
                    form.number("task-keepalive", 2233);
                    form.number("task-max-threads", 12345);
                });
    }

    @Test
    public void read() {
        table.select(WORKER_READ);
        assertEquals(11, form.intValue(IO_THREADS));
    }

    @Test
    public void update() throws Exception {
        table.select(WORKER_UPDATE);
        crud.update(workerAddress(WORKER_UPDATE), form, IO_THREADS, Random.number());
    }

    @Test
    public void updateInvalidMaxThreads() {
        table.select(WORKER_UPDATE);
        crud.updateWithError(form, IO_THREADS, -1);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(workerAddress(WORKER_DELETE), table, WORKER_DELETE);
    }
}
