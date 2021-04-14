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

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CATEGORY;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category;
import static org.junit.Assert.assertEquals;

public abstract class CategoryAbstractTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations ops = new Operations(client);
    protected static final Administration adminOps = new Administration(client);

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    private static final String HAL_1469_FAIL_MESSAGE = "Fails probably due to https://issues.jboss.org/browse/HAL-1469 . ";
    @Inject protected Console console;
    @Inject private CrudOperations crud;

    @Drone
    private WebDriver browser;

    private TableFragment table;
    private FormFragment form;
    protected abstract Address categoryAddress(String name);
    protected abstract TableFragment getCategoryTable();
    protected abstract FormFragment getCategoryForm();
    protected abstract void navigateToPage();

    @Before
    public void navigate() throws Exception {
        navigateToPage();
        table = getCategoryTable();
        form = getCategoryForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(categoryAddress(Category.CATEGORY_CREATE), table, Category.CATEGORY_CREATE);
    }

    @Test
    public void read() {
        table.select(Category.CATEGORY_READ);
        assertEquals(HAL_1469_FAIL_MESSAGE, Category.CATEGORY_READ, form.value(CATEGORY));
    }

    @Test
    public void update() throws Exception {
        table.select(Category.CATEGORY_UPDATE);
        try {
            crud.update(categoryAddress(Category.CATEGORY_UPDATE), form,
                    f -> f.select(LEVEL, "CONFIG"),
                    resourceVerifier -> resourceVerifier.verifyAttribute(LEVEL, "CONFIG"));
        } catch (ElementNotInteractableException e) {
            Assert.fail(HAL_1469_FAIL_MESSAGE + e.getMessage());
        }
    }

    @Test
    public void reset() throws Exception {
        table.select(Category.CATEGORY_UPDATE);
        try {
            crud.reset(categoryAddress(Category.CATEGORY_UPDATE), form);
        } catch (ElementNotInteractableException e) {
            Assert.fail(HAL_1469_FAIL_MESSAGE + e.getMessage());
        }
    }

    @Test
    public void delete() throws Exception {
        crud.delete(categoryAddress(Category.CATEGORY_DELETE), table, Category.CATEGORY_DELETE);
    }
}
