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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.categories;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.CategoryAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_CREATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_DELETE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_READ;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.Category.CATEGORY_UPDATE;

@RunWith(Arquillian.class)
public class CategoriesTest extends CategoryAbstractTest {

    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        ops.add(LoggingFixtures.Category.categoryAddress(CATEGORY_READ)).assertSuccess();
        ops.add(LoggingFixtures.Category.categoryAddress(CATEGORY_UPDATE)).assertSuccess();
        ops.add(LoggingFixtures.Category.categoryAddress(CATEGORY_DELETE)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload()
        throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.Category.categoryAddress(CATEGORY_CREATE));
        ops.removeIfExists(LoggingFixtures.Category.categoryAddress(CATEGORY_READ));
        ops.removeIfExists(LoggingFixtures.Category.categoryAddress(CATEGORY_UPDATE));
        ops.removeIfExists(LoggingFixtures.Category.categoryAddress(CATEGORY_DELETE));
        adminOps.reloadIfRequired();
    }

    @Override
    protected Address categoryAddress(String name) {
        return LoggingFixtures.Category.categoryAddress(name);
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectPrimary("logging-category-item");
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
