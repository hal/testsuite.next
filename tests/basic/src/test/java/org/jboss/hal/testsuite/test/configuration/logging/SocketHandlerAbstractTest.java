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
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.Before;
import org.junit.Test;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.OUTBOUND_SOCKET_BINDING_REF;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.NAMED_FORMATTER;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_CREATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.SocketHandler.SOCKET_HANDLER_UPDATE;

public abstract class SocketHandlerAbstractTest {

    @Inject protected Console console;
    @Inject private CrudOperations crud;
    private TableFragment table;
    private FormFragment form;
    protected abstract LoggingConfigurationPage getPage();
    protected abstract Address getHandlerAddress(String name);
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
        crud.create(getHandlerAddress(SOCKET_HANDLER_CREATE), table, f -> {
            f.text(NAME, SOCKET_HANDLER_CREATE);
            f.text(NAMED_FORMATTER, getPatternFormatter());
            f.text(OUTBOUND_SOCKET_BINDING_REF, "mail-smtp");
        });
    }

    @Test
    public void update() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.update(getHandlerAddress(SOCKET_HANDLER_UPDATE), form, "filter-spec", "not(match(\"JBAS.*\"))");
    }

    @Test
    public void reset() throws Exception {
        table.select(SOCKET_HANDLER_UPDATE);
        crud.reset(getHandlerAddress(SOCKET_HANDLER_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(getHandlerAddress(SOCKET_HANDLER_DELETE), table, SOCKET_HANDLER_DELETE);
    }
}
