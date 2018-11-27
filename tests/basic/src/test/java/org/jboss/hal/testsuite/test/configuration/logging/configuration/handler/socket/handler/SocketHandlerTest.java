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
package org.jboss.hal.testsuite.test.configuration.logging.configuration.handler.socket.handler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.page.configuration.LoggingSubsystemConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures;
import org.jboss.hal.testsuite.test.configuration.logging.SocketHandlerAbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.OUTBOUND_SOCKET_BINDING_REF;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.*;

@RunWith(Arquillian.class)
public class SocketHandlerTest extends SocketHandlerAbstractTest {

    @Page
    private LoggingSubsystemConfigurationPage page;

    @BeforeClass
    public static void createResources() throws IOException {
        Values params = Values.of(NAMED_FORMATTER, "PATTERN").and(OUTBOUND_SOCKET_BINDING_REF, "mail-smtp");
        ops.add(LoggingFixtures.SocketHandler.socketHandlerAddress(SocketHandler.SOCKET_HANDLER_UPDATE), params).assertSuccess();
        ops.add(LoggingFixtures.SocketHandler.socketHandlerAddress(SocketHandler.SOCKET_HANDLER_DELETE), params).assertSuccess();
        ops.add(LoggingFixtures.XmlFormatter.xmlFormatterAddress(XML_FORMATTER)).assertSuccess();
    }

    @AfterClass
    public static void removeResourcesAndReload()
        throws IOException, OperationException, InterruptedException, TimeoutException {
        ops.removeIfExists(LoggingFixtures.SocketHandler.socketHandlerAddress(SocketHandler.SOCKET_HANDLER_CREATE));
        ops.removeIfExists(LoggingFixtures.SocketHandler.socketHandlerAddress(SocketHandler.SOCKET_HANDLER_UPDATE));
        ops.removeIfExists(LoggingFixtures.SocketHandler.socketHandlerAddress(SocketHandler.SOCKET_HANDLER_DELETE));
        adminOps.reloadIfRequired();
    }

    @Override
    protected void navigateToPage() {
        page.navigate();
        console.verticalNavigation().selectSecondary(LOGGING_HANDLER_ITEM,
            "logging-handler-socket-item");
    }

    @Override
    protected LoggingConfigurationPage getPage() {
        return page;
    }

    @Override
    protected Address socketHandlerAddress(String name) {
        return LoggingFixtures.SocketHandler.socketHandlerAddress(name);
    }

    @Override
    protected TableFragment getHandlerTable() {
        return page.getSocketHandlerTable();
    }

    @Override
    protected FormFragment getHandlerForm() {
        return page.getSocketHandlerForm();
    }
}
