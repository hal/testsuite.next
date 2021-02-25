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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.formatter.pattern.formatter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.PatternFormatterAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.LOGGING_FORMATTER_ITEM;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.PatternFormatter.PATTERN_FORMATTER_CREATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.PatternFormatter.PATTERN_FORMATTER_DELETE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.PatternFormatter.PATTERN_FORMATTER_UPDATE;

@RunWith(Arquillian.class)
public class PatternFormatterTest extends PatternFormatterAbstractTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        ops.add(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_UPDATE)).assertSuccess();
        ops.add(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_DELETE)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload()
        throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_CREATE));
        ops.removeIfExists(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_UPDATE));
        ops.removeIfExists(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_DELETE));
        adminOps.reloadIfRequired();
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_FORMATTER_ITEM,
            "logging-formatter-pattern-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address patternFormatterAddress(String name) {
        return LoggingFixtures.PatternFormatter.patternFormatterAddress(name);
    }
}
