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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.handler.syslog.handler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fixtures.LoggingFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.SyslogHandlerAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.LOGGING_HANDLER_ITEM;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.PatternFormatter.PATTERN_FORMATTER_REF;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.SyslogHandler.SYSLOG_HANDLER_CREATE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.SyslogHandler.SYSLOG_HANDLER_DELETE;
import static org.jboss.hal.testsuite.fixtures.LoggingFixtures.SyslogHandler.SYSLOG_HANDLER_UPDATE;

@RunWith(Arquillian.class)
public class SyslogHandlerTest extends SyslogHandlerAbstractTest {

    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void createResources() throws IOException {
        ops.add(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_REF)).assertSuccess();
        ops.add(LoggingFixtures.SyslogHandler.syslogHandlerAddress(SYSLOG_HANDLER_UPDATE)).assertSuccess();
        ops.add(LoggingFixtures.SyslogHandler.syslogHandlerAddress(SYSLOG_HANDLER_DELETE)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload()
        throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.SyslogHandler.syslogHandlerAddress(SYSLOG_HANDLER_CREATE));
        ops.removeIfExists(LoggingFixtures.SyslogHandler.syslogHandlerAddress(SYSLOG_HANDLER_UPDATE));
        ops.removeIfExists(LoggingFixtures.SyslogHandler.syslogHandlerAddress(SYSLOG_HANDLER_DELETE));
        ops.removeIfExists(LoggingFixtures.PatternFormatter.patternFormatterAddress(PATTERN_FORMATTER_REF));
        adminOps.reloadIfRequired();
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_HANDLER_ITEM,
            "logging-handler-syslog-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address syslogHandlerAddress(String name) {
        return LoggingFixtures.SyslogHandler.syslogHandlerAddress(name);
    }

    @Override
    protected TableFragment getHandlerTable() {
        return page.getSyslogHandlerTable();
    }

    @Override
    protected FormFragment getHandlerForm() {
        return page.getSyslogHandlerForm();
    }
}
