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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.handler.custom.handler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.CustomHandlerAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CLASS_VALUE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CustomHandler.CUSTOM_HANDLER_CREATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CustomHandler.CUSTOM_HANDLER_DELETE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CustomHandler.CUSTOM_HANDLER_READ;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.CustomHandler.CUSTOM_HANDLER_UPDATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.LOGGING_HANDLER_ITEM;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.MODULE_VALUE;

@RunWith(Arquillian.class)
public class CustomHandlerTest extends CustomHandlerAbstractTest {

    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void createResources() throws IOException {
        ops.add(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_READ),
            Values.of(MODULE, MODULE_VALUE).and(CLASS, CLASS_VALUE)).assertSuccess();
        ops.add(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_UPDATE),
            Values.of(MODULE, MODULE_VALUE).and(CLASS, CLASS_VALUE)).assertSuccess();
        ops.add(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_DELETE),
            Values.of(MODULE, MODULE_VALUE).and(CLASS, CLASS_VALUE)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload() throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_CREATE));
        ops.removeIfExists(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_READ));
        ops.removeIfExists(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_UPDATE));
        ops.removeIfExists(LoggingFixtures.CustomHandler.customHandlerAddress(CUSTOM_HANDLER_DELETE));
        adminOps.reloadIfRequired();
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_HANDLER_ITEM,
            "logging-handler-custom-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address customHandlerAddress(String name) {
        return LoggingFixtures.CustomHandler.customHandlerAddress(name);
    }

    @Override
    protected TableFragment getHandlerTable() {
        return page.getCustomHandlerTable();
    }

    @Override
    protected FormFragment getHandlerForm() {
        return page.getCustomHandlerForm();
    }
}
