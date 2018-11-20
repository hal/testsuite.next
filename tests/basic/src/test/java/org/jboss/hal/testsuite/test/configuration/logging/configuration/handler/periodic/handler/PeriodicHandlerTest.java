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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.handler.periodic.handler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.PeriodicHandlerAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.FILE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PERIODIC_ROTATING_FILE_HANDLER;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.LOGGING_HANDLER_ITEM;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PATH_VALUE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PeriodicHandler.PERIODIC_HANDLER_CREATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PeriodicHandler.PERIODIC_HANDLER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PeriodicHandler.PERIODIC_HANDLER_READ;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PeriodicHandler.PERIODIC_HANDLER_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUBSYSTEM_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUFFIX;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUFFIX_VALUE;

@RunWith(Arquillian.class)
public class PeriodicHandlerTest extends PeriodicHandlerAbstractTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    private static Address handlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_HANDLER, name);
    }

    @BeforeClass
    public static void setUp() throws IOException {
        ModelNode file = new ModelNode();
        file.get(PATH).set(PATH_VALUE);
        ops.add(handlerAddress(PERIODIC_HANDLER_READ),
                Values.of(FILE, file.clone()).and(SUFFIX, SUFFIX_VALUE)).assertSuccess();
        ops.add(handlerAddress(PERIODIC_HANDLER_UPDATE),
                Values.of(FILE, file.clone()).and(SUFFIX, SUFFIX_VALUE)).assertSuccess();
        ops.add(handlerAddress(PERIODIC_HANDLER_DELETE),
                Values.of(FILE, file.clone()).and(SUFFIX, SUFFIX_VALUE)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, InterruptedException, TimeoutException {
        try {
            ops.removeIfExists(handlerAddress(PERIODIC_HANDLER_CREATE));
            ops.removeIfExists(handlerAddress(PERIODIC_HANDLER_READ));
            ops.removeIfExists(handlerAddress(PERIODIC_HANDLER_UPDATE));
            ops.removeIfExists(handlerAddress(PERIODIC_HANDLER_DELETE));
            adminOps.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Page private LoggingSubsystemConfigurationPage page;

    @Override
    protected Address getHandlerAddress(String name) {
        return handlerAddress(name);
    }

    @Override
    protected FormFragment getHandlerForm() {
        return page.getPeriodicHandlerForm();
    }

    @Override
    protected TableFragment getHandlerTable() {
        return page.getPeriodicHandlerTable();
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_HANDLER_ITEM,
                "logging-handler-periodic-rotating-file-item");
    }
}
