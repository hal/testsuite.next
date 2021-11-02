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
package org.jboss.hal.testsuite.test.configuration.management;

import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.runtime.StandaloneServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONSTANT_HEADERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.CONSTANT_HEADERS_PATH_READ;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_NAME_CREATE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_NAME_DELETE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_NAME_READ;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_NAME_UPDATE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_VALUE_CREATE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_VALUE_DELETE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_VALUE_READ;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HEADER_VALUE_UPDATE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HTTP_INTERFACE_ADDRESS;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.constantHeaders;
import static org.jboss.hal.testsuite.test.configuration.management.ConstantHeadersChecks.verifyConstantHeader;
import static org.jboss.hal.testsuite.test.configuration.management.ConstantHeadersChecks.verifyConstantHeaderNameValueDeleted;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ConstantHeadersHeaderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Map<String, String> headerNameValues = new HashMap<>();
        headerNameValues.put(HEADER_NAME_READ, HEADER_VALUE_READ);
        headerNameValues.put(HEADER_NAME_UPDATE, HEADER_VALUE_UPDATE);
        headerNameValues.put(HEADER_NAME_DELETE, HEADER_VALUE_DELETE);
        operations.writeListAttribute(HTTP_INTERFACE_ADDRESS, CONSTANT_HEADERS,
                constantHeaders(CONSTANT_HEADERS_PATH_READ, headerNameValues));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.undefineAttribute(HTTP_INTERFACE_ADDRESS, CONSTANT_HEADERS);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private StandaloneServerPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.CONSTANT_HEADERS_ITEM);
        page.getConstantHeadersTable().action(CONSTANT_HEADERS_PATH_READ, "Headers");
        table = page.getHeadersTable();
        form = page.getHeadersForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(HTTP_INTERFACE_ADDRESS, table,
                form -> {
                    form.text(NAME, HEADER_NAME_CREATE);
                    form.text(VALUE, HEADER_VALUE_CREATE);
                },
                resourceVerifier -> resourceVerifier.verifyTrue("Header name/value not created",
                        () -> verifyConstantHeader(operations,
                                CONSTANT_HEADERS_PATH_READ,
                                HEADER_NAME_CREATE,
                                HEADER_VALUE_CREATE)));
    }

    @Test
    public void read() {
        table.select(HEADER_NAME_READ);
        assertEquals(HEADER_NAME_READ, form.value(NAME));
        assertEquals(HEADER_VALUE_READ, form.value(VALUE));
    }

    @Test
    public void update() throws Exception {
        String name = Random.name();
        String value = Random.name();
        table.select(HEADER_NAME_UPDATE);
        crud.update(HTTP_INTERFACE_ADDRESS, form,
                form -> {
                    form.text(NAME, name);
                    form.text(VALUE, value);
                },
                resourceVerifier -> resourceVerifier.verifyTrue("Header name/value not updated",
                        () -> verifyConstantHeader(operations,
                                CONSTANT_HEADERS_PATH_READ,
                                name, value)));
    }

    @Test
    public void delete() throws Exception {
        crud.delete(HTTP_INTERFACE_ADDRESS, table, HEADER_NAME_DELETE,
                resourceVerifier -> resourceVerifier.verifyTrue("Header name/value still exists",
                        () -> verifyConstantHeaderNameValueDeleted(operations,
                                CONSTANT_HEADERS_PATH_READ,
                                HEADER_NAME_DELETE,
                                HEADER_VALUE_DELETE)));
    }
}
