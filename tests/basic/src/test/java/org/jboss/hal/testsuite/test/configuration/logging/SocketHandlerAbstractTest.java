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

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.NAMED_FORMATTER;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_CREATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_UPDATE;

public abstract class SocketHandlerAbstractTest {

    private static final String OUTBOUND_SOCKET_BINDING_REF = "outbound-socket-binding-ref-" + Random.name();
    private static final String SSL_CONTEXT = "client-ssl-context-" + Random.name();

    protected static final String XML_FORMATTER = "xml-formatter-" + Random.name();

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations ops = new Operations(client);
    protected static final Administration adminOps = new Administration(client);

    @BeforeClass
    public static void createAdditionalResources() throws IOException, CommandFailedException {
        AddRemoteSocketBinding addRemoteSocketBinding = new AddRemoteSocketBinding(OUTBOUND_SOCKET_BINDING_REF,
            ConfigUtils.getDefaultHost(), AvailablePortFinder.getNextAvailableTCPPort());
        client.apply(addRemoteSocketBinding);
        ops.add(ElytronFixtures.clientSslContextAddress(SSL_CONTEXT)).assertSuccess();
    }

    @AfterClass
    public static void closeClient() throws IOException, OperationException, CommandFailedException {
        try {
            RemoveSocketBinding removeSocketBinding = new RemoveSocketBinding(OUTBOUND_SOCKET_BINDING_REF);
            client.apply(removeSocketBinding);
            ops.removeIfExists(ElytronFixtures.clientSslContextAddress(SSL_CONTEXT));
        } finally {
            client.close();
        }
    }

    @Inject
    protected Console console;
    @Inject
    private CrudOperations crud;

    @Drone
    private WebDriver browser;

    private TableFragment table;
    private FormFragment form;

    protected abstract LoggingConfigurationPage getPage();

    protected abstract Address socketHandlerAddress(String name);

    protected abstract TableFragment getHandlerTable();

    protected abstract FormFragment getHandlerForm();

    protected abstract void navigateToPage();

    // there must be a separate pattern-formatter name for logging-profile
    // subclasses may override this method to provide a different name
    protected String getPatternFormatter() {
        return "PATTERN";
    }

    @Before
    public void navigate() throws Exception {
        navigateToPage();
        table = getPage().getSocketHandlerTable();
        form = getPage().getSocketHandlerForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(socketHandlerAddress(SOCKET_HANDLER_CREATE), table, f -> {
            f.text(NAME, SOCKET_HANDLER_CREATE);
            f.text(NAMED_FORMATTER, getPatternFormatter());
            f.text(ModelDescriptionConstants.OUTBOUND_SOCKET_BINDING_REF, "mail-smtp");
        });
    }

    @Test
    public void reset() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.reset(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(socketHandlerAddress(SOCKET_HANDLER_DELETE), table, SOCKET_HANDLER_DELETE);
    }

    @Test
    public void toggleAutoFlush() throws Exception {
        boolean autoflush =
            ops.readAttribute(socketHandlerAddress(SOCKET_HANDLER_UPDATE), "autoflush").booleanValue(true);
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "autoflush", !autoflush);
    }

    @Test
    public void toggleBlockOnReconnect() throws Exception {
        boolean blockOnReconnect =
            ops.readAttribute(socketHandlerAddress(SOCKET_HANDLER_UPDATE), "block-on-reconnect").booleanValue(true);
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "block-on-reconnect", !blockOnReconnect);
    }

    @Test
    public void toggleEnabled() throws Exception {
        boolean enabled =
            ops.readAttribute(socketHandlerAddress(SOCKET_HANDLER_UPDATE), "enabled").booleanValue(true);
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "enabled", !enabled);
    }

    @Test
    public void editEncoding() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "encoding");
    }

    @Test
    public void editFilterSpec() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "filter-spec", "not(match(\"JBAS.*\"))");
    }

    @Test
    public void editLevel() throws Exception {
        String[] levels =
            {"ALL", "FINEST", "FINER", "TRACE", "DEBUG", "FINE", "CONFIG", "INFO", "WARN", "WARNING", "ERROR", "SEVERE",
                "FATAL", "OFF"};
        String level = levels[Random.number(1, levels.length)];
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, formFragment -> {
            formFragment.select("level", level);
        }, resourceVerifier -> resourceVerifier.verifyAttribute("level", level));
    }

    @Test
    public void editNamedFormatter() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "named-formatter", XML_FORMATTER);
    }

    @Test
    public void editOutboundSocketBindingRef() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "outbound-socket-binding-ref",
            OUTBOUND_SOCKET_BINDING_REF);
    }

    @Test
    public void editProtocol() throws Exception {
        String[] protocols = {"TCP", "UDP", "SSL_TCP"};
        String protocol = protocols[Random.number(1, protocols.length)];
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form,
            formFragment -> formFragment.select("protocol", protocol),
            resourceVerifier -> resourceVerifier.verifyAttribute("protocol", protocol));
    }

    @Test
    public void editSSLContext() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(socketHandlerAddress(SOCKET_HANDLER_UPDATE), form, "ssl-context", SSL_CONTEXT);
    }
}
