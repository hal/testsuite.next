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
package org.jboss.hal.testsuite.test.configuration.logging.logging.profiles.handler.size.handler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingProfileConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.SizeHandlerAbstractTest;
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
import static org.jboss.hal.dmr.ModelDescriptionConstants.LOGGING_PROFILE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SIZE_ROTATING_FILE_HANDLER;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.LOGGING_PROFILE_HANDLER_ITEM;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.NAME;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PATH_VALUE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUBSYSTEM_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SizeHandler.SIZE_HANDLER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SizeHandler.SIZE_HANDLER_READ;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SizeHandler.SIZE_HANDLER_UPDATE;

@RunWith(Arquillian.class)
public class SizeHandlerTest extends SizeHandlerAbstractTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    private static String profileName;
    private static Address profileAddress;
    @Page
    private LoggingProfileConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        profileName = Ids.build(SizeHandlerTest.class.getSimpleName(), Random.name());
        profileAddress = SUBSYSTEM_ADDRESS.and(LOGGING_PROFILE, profileName);
        ops.add(profileAddress).assertSuccess();
        ModelNode file = new ModelNode();
        file.get(PATH).set(PATH_VALUE);
        ops.add(handlerAddress(SIZE_HANDLER_READ), Values.of(FILE, file.clone())).assertSuccess();
        ops.add(handlerAddress(SIZE_HANDLER_UPDATE), Values.of(FILE, file.clone())).assertSuccess();
        ops.add(handlerAddress(SIZE_HANDLER_DELETE), Values.of(FILE, file.clone())).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, InterruptedException, TimeoutException {
        try {
            ops.remove(profileAddress);
            adminOps.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Override
    protected void navigateToPage() {
        page.navigate(NAME, profileName);
        console.verticalNavigation().selectSecondary(LOGGING_PROFILE_HANDLER_ITEM,
            "logging-profile-handler-size-rotating-file-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address getHandlerAddress(String name) {
        return handlerAddress(name);
    }

    @Override
    protected TableFragment getHandlerTable() {
        return page.getSizeHandlerTable();
    }

    @Override
    protected FormFragment getHandlerForm() {
        return page.getSizeHandlerForm();
    }

    private static Address handlerAddress(String name) {
        return profileAddress.and(SIZE_ROTATING_FILE_HANDLER, name);
    }
}
