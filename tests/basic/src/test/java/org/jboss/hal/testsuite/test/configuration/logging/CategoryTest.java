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
package org.jboss.hal.testsuite.test.configuration.logging;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CategoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(categoryAddress(CATEGORY_READ));
        operations.add(categoryAddress(CATEGORY_UPDATE));
        operations.add(categoryAddress(CATEGORY_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(categoryAddress(CATEGORY_CREATE));
        operations.removeIfExists(categoryAddress(CATEGORY_READ));
        operations.removeIfExists(categoryAddress(CATEGORY_UPDATE));
        operations.removeIfExists(categoryAddress(CATEGORY_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private LoggingConfigurationPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("logging-category-item");
        table = page.getCategoryTable();
        form = page.getCategoryForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(categoryAddress(CATEGORY_CREATE), table, CATEGORY_CREATE);
    }

    @Test
    public void read() {
        table.select(CATEGORY_READ);
        assertEquals(CATEGORY_READ, form.value(CATEGORY));
    }

    @Test
    public void update() throws Exception {
        table.select(CATEGORY_UPDATE);
        crud.update(categoryAddress(CATEGORY_UPDATE), form,
                f -> f.select(LEVEL, "CONFIG"),
                resourceVerifier -> resourceVerifier.verifyAttribute(LEVEL, "CONFIG"));
    }

    @Test
    public void reset() throws Exception {
        table.select(CATEGORY_UPDATE);
        crud.reset(categoryAddress(CATEGORY_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(categoryAddress(CATEGORY_DELETE), table, CATEGORY_DELETE);
    }
}
