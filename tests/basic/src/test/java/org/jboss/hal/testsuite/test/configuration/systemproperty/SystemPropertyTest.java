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
package org.jboss.hal.testsuite.test.configuration.systemproperty;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SystemPropertyPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.systemproperty.SystemPropertyFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class SystemPropertyTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(systemPropertyAddress(READ_NAME), Values.empty().and(VALUE, READ_VALUE));
        operations.add(systemPropertyAddress(UPDATE_NAME), Values.empty().and(VALUE, UPDATE_VALUE));
        operations.add(systemPropertyAddress(DELETE_NAME), Values.empty().and(VALUE, DELETE_VALUE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(systemPropertyAddress(CREATE_NAME));
        operations.removeIfExists(systemPropertyAddress(READ_NAME));
        operations.removeIfExists(systemPropertyAddress(UPDATE_NAME));
        operations.removeIfExists(systemPropertyAddress(DELETE_NAME));
    }

    @Page private SystemPropertyPage page;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();

        form = page.getForm();
        table = page.getTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, CREATE_NAME);
        dialog.getForm().text(VALUE, CREATE_VALUE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(systemPropertyAddress(CREATE_NAME), client)
                .verifyExists();
    }

    @Test
    public void read() throws Exception {
        table.select(READ_NAME);
        assertEquals(READ_VALUE, form.value(VALUE));
    }

    @Test
    public void update() throws Exception {
        String value = Random.name();
        table.select(UPDATE_NAME);
        form.edit();
        form.text(VALUE, value);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(systemPropertyAddress(UPDATE_NAME), client)
                .verifyAttribute(VALUE, value);

    }

    @Test
    public void delete() throws Exception {
        table.remove(DELETE_NAME);

        console.verifySuccess();
        new ResourceVerifier(systemPropertyAddress(DELETE_NAME), client).verifyDoesNotExist();
    }
}
