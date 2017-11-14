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
package org.jboss.hal.testsuite.test.configuration;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/** Test case for the configuration top level category */
@RunWith(Arquillian.class)
public class ConfigurationTest {

    @Inject private Console console;
    private FinderFragment finder;

    @Before
    public void setUp() throws Exception {
        finder = console.finder(NameTokens.CONFIGURATION);
    }

    @Test
    public void items() throws Exception {
        ColumnFragment column = finder.column(Ids.CONFIGURATION);
        assertTrue(column.containsItem(Ids.asId(Names.SUBSYSTEMS)));
        assertTrue(column.containsItem(Ids.asId(Names.INTERFACES)));
        assertTrue(column.containsItem(Ids.asId(Names.SOCKET_BINDINGS)));
        assertTrue(column.containsItem(Ids.asId(Names.PATHS)));
        assertTrue(column.containsItem(Ids.asId(Names.SYSTEM_PROPERTIES)));
    }
}
