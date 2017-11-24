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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.ROOT_LOGGER_ADDRESS;

@RunWith(Arquillian.class)
public class RootLoggerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws Exception {
        backup = new BackupAndRestoreAttributes.Builder(ROOT_LOGGER_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Page private LoggingConfigurationPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void updateRootLogger() throws Exception {
        console.verticalNavigation().selectPrimary("logging-root-logger-item");
        form = page.getRootLoggerForm();

        form.edit();
        form.select(LEVEL, "ERROR");
        form.save();

        console.verifySuccess();
        new ResourceVerifier(ROOT_LOGGER_ADDRESS, client)
                .verifyAttribute(LEVEL, "ERROR");
    }

    @Test
    public void resetRootLogger() throws Exception {
        console.verticalNavigation().selectPrimary("logging-root-logger-item");
        form = page.getRootLoggerForm();
        form.reset();

        console.verifySuccess();
        new ResourceVerifier(ROOT_LOGGER_ADDRESS, client)
                .verifyReset();
    }
}