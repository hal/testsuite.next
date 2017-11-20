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
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.IOPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.io.IOFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class BufferPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(bufferPool(BP_READ), Values.empty().and(BUFFER_SIZE, 11).and(BUFFERS_PER_SLICE, 22));
        operations.add(bufferPool(BP_UPDATE), Values.empty());
        operations.add(bufferPool(BP_DELETE), Values.empty());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(bufferPool(BP_CREATE));
        operations.removeIfExists(bufferPool(BP_READ));
        operations.removeIfExists(bufferPool(BP_UPDATE));
        operations.removeIfExists(bufferPool(BP_DELETE));
    }

    @Drone private WebDriver browser;
    @Page private IOPage page;
    @Inject private Console console;
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
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, BP_CREATE);
        dialog.getForm().number(BUFFER_SIZE, 12);
        dialog.getForm().number(BUFFERS_PER_SLICE, 23);
        dialog.getForm().flip(DIRECT_BUFFERS, true);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(bufferPool(BP_CREATE), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        table.select(BP_READ);
        assertEquals(11, form.intValue(BUFFER_SIZE));
    }

    @Test
    public void update() throws Exception {
        int bufferSize = Random.number();

        table.select(BP_UPDATE);
        form.edit();
        form.number(BUFFER_SIZE, bufferSize);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(bufferPool(BP_UPDATE), client)
                .verifyAttribute(BUFFER_SIZE, bufferSize);
    }

    @Test
    public void updateInvalidBufferSize() throws Exception {
        table.select(BP_UPDATE);
        form.edit();
        form.number(BUFFER_SIZE, 0);
        form.trySave();
        form.expectError(BUFFER_SIZE);
    }

    @Test
    public void updateInvalidBuffersPerSlice() throws Exception {
        table.select(BP_UPDATE);
        form.edit();
        form.number(BUFFERS_PER_SLICE, 0);
        form.trySave();
        form.expectError(BUFFERS_PER_SLICE);
    }

    @Test
    public void delete() throws Exception {
        table.remove(BP_DELETE);

        console.verifySuccess();
        new ResourceVerifier(bufferPool(BP_DELETE), client).verifyDoesNotExist();
    }
}
