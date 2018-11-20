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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.formatter.custom.formatter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.CustomFormatterAbstractTest;
import org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROPERTIES;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CUSTOM_FORMATTER_CLASS_1_VALUE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CustomFormatter.CUSTOM_FORMATTER_CREATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CustomFormatter.CUSTOM_FORMATTER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CUSTOM_FORMATTER_MODULE_VALUE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CustomFormatter.CUSTOM_FORMATTER_RESET;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.CustomFormatter.CUSTOM_FORMATTER_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.LOGGING_FORMATTER_ITEM;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.RECORD_DELIMITER_PROPERTY_NAME;

@RunWith(Arquillian.class)
public class CustomFormatterTest extends CustomFormatterAbstractTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        ops.add(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_UPDATE),
            Values.of(MODULE, CUSTOM_FORMATTER_MODULE_VALUE)
                .and(CLASS, CUSTOM_FORMATTER_CLASS_1_VALUE)).assertSuccess();
        ops.add(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_RESET),
            Values.of(MODULE, CUSTOM_FORMATTER_MODULE_VALUE)
                .and(CLASS, CUSTOM_FORMATTER_CLASS_1_VALUE).and(PROPERTIES,
                new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(RECORD_DELIMITER_PROPERTY_NAME, Random.name()).build())).assertSuccess();
        ops.add(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_DELETE),
            Values.of(MODULE, CUSTOM_FORMATTER_MODULE_VALUE)
                .and(CLASS, CUSTOM_FORMATTER_CLASS_1_VALUE)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, InterruptedException, TimeoutException {
        try {
            ops.removeIfExists(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_CREATE));
            ops.removeIfExists(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_UPDATE));
            ops.removeIfExists(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_RESET));
            ops.removeIfExists(LoggingFixtures.CustomFormatter.customFormatterAddress(CUSTOM_FORMATTER_DELETE));
            adminOps.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_FORMATTER_ITEM,
            "logging-formatter-custom-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address customFormatterAddress(String name) {
        return LoggingFixtures.CustomFormatter.customFormatterAddress(name);
    }
}
