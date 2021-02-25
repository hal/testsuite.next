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

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BP_CREATE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BP_DELETE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BP_READ;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BUFFERS_PER_SLICE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.BUFFER_SIZE;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.DIRECT_BUFFERS;
import static org.jboss.hal.testsuite.fixtures.IOFixtures.bufferPoolAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class BufferPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(bufferPoolAddress(BP_READ), Values.empty().and(BUFFER_SIZE, 11).and(BUFFERS_PER_SLICE, 22));
        operations.add(bufferPoolAddress(BP_UPDATE), Values.empty());
        operations.add(bufferPoolAddress(BP_DELETE), Values.empty());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(bufferPoolAddress(BP_CREATE));
        operations.removeIfExists(bufferPoolAddress(BP_READ));
        operations.removeIfExists(bufferPoolAddress(BP_UPDATE));
        operations.removeIfExists(bufferPoolAddress(BP_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private IOPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("io-buffer-pool-item");

        form = page.getBufferPoolForm();
        table = page.getBufferPoolTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(bufferPoolAddress(BP_CREATE), table,
                form -> {
                    form.text(NAME, BP_CREATE);
                    form.number(BUFFER_SIZE, 12);
                    form.number(BUFFERS_PER_SLICE, 23);
                    form.flip(DIRECT_BUFFERS, true);
                });
    }

    @Test
    public void read() {
        table.select(BP_READ);
        assertEquals(11, form.intValue(BUFFER_SIZE));
    }

    @Test
    public void update() throws Exception {
        table.select(BP_UPDATE);
        crud.update(bufferPoolAddress(BP_UPDATE), form, BUFFER_SIZE, Random.number());
    }

    @Test
    public void updateInvalidBufferSize() {
        table.select(BP_UPDATE);
        crud.updateWithError(form, BUFFER_SIZE, 0);
    }

    @Test
    public void updateInvalidBuffersPerSlice() {
        table.select(BP_UPDATE);
        crud.updateWithError(form, BUFFERS_PER_SLICE, 0);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(bufferPoolAddress(BP_DELETE), table, BP_DELETE);
    }
}
