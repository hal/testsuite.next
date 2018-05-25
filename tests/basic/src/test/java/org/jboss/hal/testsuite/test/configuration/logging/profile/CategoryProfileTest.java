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
package org.jboss.hal.testsuite.test.configuration.logging.profile;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingProfileConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.CategoryAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOGGER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LOGGING_PROFILE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CATEGORY_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CATEGORY_READ;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CATEGORY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.NAME;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUBSYSTEM_ADDRESS;

@RunWith(Arquillian.class)
public class CategoryProfileTest extends CategoryAbstractTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    private static String profileName;
    private static Address profileAddress;
    @Page private LoggingProfileConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        profileName = Ids.build(CategoryProfileTest.class.getSimpleName(), Random.name());
        profileAddress = SUBSYSTEM_ADDRESS.and(LOGGING_PROFILE, profileName);
        ops.add(profileAddress).assertSuccess();
        ops.add(categoryAddress(CATEGORY_READ)).assertSuccess();
        ops.add(categoryAddress(CATEGORY_UPDATE)).assertSuccess();
        ops.add(categoryAddress(CATEGORY_DELETE)).assertSuccess();
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
    protected Address getCategoryAddress(String name) {
        return categoryAddress(name);
    }

    @Override
    protected void navigateToPage() {
        page.navigate(NAME, profileName);
        console.verticalNavigation().selectPrimary("logging-profile-category-item");
    }

    @Override
    protected TableFragment getCategoryTable() {
        return page.getCategoryTable();
    }

    @Override
    protected FormFragment getCategoryForm() {
        return page.getCategoryForm();
    }

    private static Address categoryAddress(String name) {
        return profileAddress.and(LOGGER, name);
    }

}
