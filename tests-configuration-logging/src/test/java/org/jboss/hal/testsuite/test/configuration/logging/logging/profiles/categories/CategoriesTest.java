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
package org.jboss.hal.testsuite.test.configuration.logging.logging.profiles.categories;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingProfileConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.CategoryAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_DELETE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_READ;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.NAME;

@RunWith(Arquillian.class)
public class CategoriesTest extends CategoryAbstractTest {

    private static final String LOGGING_PROFILE = "logging-profile-" + Random.name();

    @Page
    private LoggingProfileConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        ops.add(LoggingFixtures.LoggingProfile.loggingProfileAddress(LOGGING_PROFILE)).assertSuccess();
        ops.add(LoggingFixtures.LoggingProfile.categoryAddress(LOGGING_PROFILE, CATEGORY_READ)).assertSuccess();
        ops.add(LoggingFixtures.LoggingProfile.categoryAddress(LOGGING_PROFILE, CATEGORY_UPDATE)).assertSuccess();
        ops.add(LoggingFixtures.LoggingProfile.categoryAddress(LOGGING_PROFILE, CATEGORY_DELETE)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload() throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.LoggingProfile.loggingProfileAddress(LOGGING_PROFILE));
        adminOps.reloadIfRequired();
    }

    @Override
    protected Address categoryAddress(String name) {
        return LoggingFixtures.LoggingProfile.categoryAddress(LOGGING_PROFILE, name);
    }

    @Override
    protected void navigateToPage() {
        page.navigate(NAME, LOGGING_PROFILE);
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
}
