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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.root.logger;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.RootLoggerAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.Address;


@RunWith(Arquillian.class)
public class RootLoggerTest extends RootLoggerAbstractTest {

    private static BackupAndRestoreAttributes backup;

    @Page
    protected LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void backupRootLogger() throws CommandFailedException {
        backup = new BackupAndRestoreAttributes.Builder(LoggingFixtures.ROOT_LOGGER_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void applyRootLoggerBackupAndReload() throws Exception {
        client.apply(backup.restore());
        adminOps.reloadIfRequired();
    }

    @Override
    protected Address rootLoggerAddress() {
        return LoggingFixtures.ROOT_LOGGER_ADDRESS;
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectPrimary("logging-root-logger-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }
}
