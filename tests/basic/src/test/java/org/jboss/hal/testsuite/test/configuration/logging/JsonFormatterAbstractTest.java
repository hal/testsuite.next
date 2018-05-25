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
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.Before;
import org.junit.Test;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.*;

public abstract class JsonFormatterAbstractTest {

    @Inject protected Console console;
    @Inject private CrudOperations crud;
    private TableFragment table;
    private FormFragment form;
    protected abstract LoggingConfigurationPage getPage();
    protected abstract Address getFormatterAddress(String name);
    protected abstract void navigateToPage();

    @Before
    public void navigate() throws Exception {
        navigateToPage();
        table = getPage().getJsonFormatterTable();
        form = getPage().getJsonFormatterForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(getFormatterAddress(JSON_FORMATTER_CREATE), table, JSON_FORMATTER_CREATE);
    }

    @Test
    public void update() throws Exception {
        table.select(JSON_FORMATTER_UPDATE);
        crud.update(getFormatterAddress(JSON_FORMATTER_UPDATE), form, RECORD_DELIMITER, Random.name());
    }

    @Test
    public void reset() throws Exception {
        table.select(JSON_FORMATTER_RESET);
        crud.reset(getFormatterAddress(JSON_FORMATTER_RESET), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(getFormatterAddress(JSON_FORMATTER_DELETE), table, JSON_FORMATTER_DELETE);
    }
}
