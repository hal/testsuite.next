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
package org.jboss.hal.testsuite.test.configuration.remoting;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROPERTY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class OutboundTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(outboundAddress(OUTBOUND_READ), Values.of(URI, uri(OUTBOUND_READ)));
        operations.add(outboundAddress(OUTBOUND_UPDATE), Values.of(URI, uri(OUTBOUND_UPDATE)));
        operations.add(outboundAddress(OUTBOUND_DELETE), Values.of(URI, uri(OUTBOUND_DELETE)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(outboundAddress(OUTBOUND_CREATE));
        operations.removeIfExists(outboundAddress(OUTBOUND_READ));
        operations.removeIfExists(outboundAddress(OUTBOUND_UPDATE));
        operations.removeIfExists(outboundAddress(OUTBOUND_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private RemotingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("remoting-outbound-connection-item",
                "remoting-outbound-sub-item");
        table = page.getOutboundTable();
        form = page.getOutboundForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(outboundAddress(OUTBOUND_CREATE), table, form -> {
            form.text(NAME, OUTBOUND_CREATE);
            form.text(URI, uri(OUTBOUND_CREATE));
        });
    }

    @Test
    public void read() {
        table.select(OUTBOUND_READ);
        assertEquals(uri(OUTBOUND_READ), form.value(URI));
    }

    @Test
    public void update() throws Exception {
        ModelNode properties = Random.properties("foo", "bar");

        table.select(OUTBOUND_UPDATE);
        crud.update(outboundAddress(OUTBOUND_UPDATE), form,
                f -> f.properties(PROPERTY).add(properties),
                resourceVerifier -> {
                    // properties are nested resources!
                    ResourceVerifier propertyVerifier = new ResourceVerifier(
                            outboundAddress(OUTBOUND_UPDATE).and(PROPERTY, "foo"), client);
                    propertyVerifier.verifyAttribute(VALUE, "bar");
                });
    }

    @Test
    public void delete() throws Exception {
        crud.delete(outboundAddress(OUTBOUND_DELETE), table, OUTBOUND_DELETE);
    }
}
