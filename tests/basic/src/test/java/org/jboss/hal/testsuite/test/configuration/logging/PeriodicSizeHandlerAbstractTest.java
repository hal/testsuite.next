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

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.*;
import static org.junit.Assert.assertEquals;

public abstract class PeriodicSizeHandlerAbstractTest {

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
    protected abstract Address periodicSizeHandlerAddress(String name);
    protected abstract TableFragment getHandlerTable();
    protected abstract FormFragment getHandlerForm();
    protected abstract void navigateToPage();

    @Before
    public void navigate() throws Exception {
        navigateToPage();
        table = getPage().getPeriodicSizeHandlerTable();
        form = getPage().getPeriodicSizeHandlerForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(periodicSizeHandlerAddress(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_CREATE), table, form -> {
            form.text(NAME, PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_CREATE);
            form.text(SUFFIX, SUFFIX_VALUE);
            FileInputFragment fileInput = createPageFragment(FileInputFragment.class,
                    getPage().getNewPeriodicSizeFileInputElement());
            fileInput.setPath(PATH_VALUE);
        });
    }

    @Test
    public void read() {
        table.select(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_READ);
        FileInputFragment fileInput = createPageFragment(FileInputFragment.class,
                getPage().getReadPeriodicSizeFileInputElement());
        assertEquals(PATH_VALUE, fileInput.getPath());
    }

    @Test
    public void update() throws Exception {
        table.select(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_UPDATE);
        crud.update(periodicSizeHandlerAddress(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_UPDATE), form,
                f -> f.select(LEVEL, "CONFIG"),
                resourceVerifier -> resourceVerifier.verifyAttribute(LEVEL, "CONFIG"));
    }

    @Test
    public void reset() throws Exception {
        table.select(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_UPDATE);
        crud.reset(periodicSizeHandlerAddress(PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(periodicSizeHandlerAddress(
            PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_DELETE), table, PeriodicSizeHandler.PERIODIC_SIZE_HANDLER_DELETE);
    }
}
