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
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CLASS_VALUE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CustomHandler;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.MODULE_VALUE;
import static org.junit.Assert.assertEquals;

public abstract class CustomHandlerAbstractTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations ops = new Operations(client);
    protected static final Administration adminOps = new Administration(client);

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Inject protected Console console;
    @Inject private CrudOperations crud;

    @Drone
    private WebDriver browser;

    private TableFragment table;
    private FormFragment form;
    protected abstract LoggingConfigurationPage getPage();
    protected abstract Address customHandlerAddress(String name);
    protected abstract TableFragment getHandlerTable();
    protected abstract FormFragment getHandlerForm();
    protected abstract void navigateToPage();

    @Before
    public void navigate() throws Exception {
        navigateToPage();
        table = getPage().getCustomHandlerTable();
        form = getPage().getCustomHandlerForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(customHandlerAddress(CustomHandler.CUSTOM_HANDLER_CREATE), table, form -> {
        form.text(NAME, CustomHandler.CUSTOM_HANDLER_CREATE);
        form.text(MODULE, MODULE_VALUE);
        form.text(CLASS, CLASS_VALUE);
        });
    }

    @Test
    public void read() {
        table.select(CustomHandler.CUSTOM_HANDLER_READ);
        assertEquals(MODULE_VALUE, form.value(MODULE));
        assertEquals(CLASS_VALUE, form.value(CLASS));
    }

    @Test
    public void update() throws Exception {
        table.select(CustomHandler.CUSTOM_HANDLER_UPDATE);
        crud.update(customHandlerAddress(CustomHandler.CUSTOM_HANDLER_UPDATE), form,
                f -> f.select(LEVEL, "CONFIG"),
                resourceVerifier -> resourceVerifier.verifyAttribute(LEVEL, "CONFIG"));
    }

    @Test
    public void reset() throws Exception {
        table.select(CustomHandler.CUSTOM_HANDLER_UPDATE);
        crud.reset(customHandlerAddress(CustomHandler.CUSTOM_HANDLER_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(customHandlerAddress(CustomHandler.CUSTOM_HANDLER_DELETE), table, CustomHandler.CUSTOM_HANDLER_DELETE);
    }
}
