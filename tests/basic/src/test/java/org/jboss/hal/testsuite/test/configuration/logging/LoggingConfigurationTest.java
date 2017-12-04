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
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.ADD_LOGGING_API_DEPENDENCIES;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SUBSYSTEM_ADDRESS;

@RunWith(Arquillian.class)
public class LoggingConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws Exception {
        backup = new BackupAndRestoreAttributes.Builder(SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private LoggingConfigurationPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void updateConfiguration() throws Exception {
        console.verticalNavigation().selectPrimary("logging-config-item");
        form = page.getConfigurationForm();
        crud.update(SUBSYSTEM_ADDRESS, form, ADD_LOGGING_API_DEPENDENCIES, false);
    }

    @Test
    public void resetConfiguration() throws Exception {
        console.verticalNavigation().selectPrimary("logging-config-item");
        form = page.getConfigurationForm();
        crud.reset(SUBSYSTEM_ADDRESS, form);
    }
}