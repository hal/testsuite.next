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
package org.jboss.hal.testsuite.test.configuration.distributableweb;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.DistributableWebPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTE_CACHE_CONTAINER;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.ATTRIBUTE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.GRANULARITY;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.HOTROD_SESSION_CREATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.HOTROD_SESSION_DELETE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.HOTROD_SESSION_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.REMOTE_SOCKET_BINDING;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.SESSION;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.hotrodSessionAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.REMOTE_CC_CREATE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addRemoteCacheContainer;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addRemoteSocketBinding;

@RunWith(Arquillian.class)
public class HotrodSessionTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        addRemoteSocketBinding(client, operations, REMOTE_SOCKET_BINDING);
        addRemoteCacheContainer(client, operations, REMOTE_CC_CREATE, REMOTE_SOCKET_BINDING);
        Values values = Values.of(REMOTE_CACHE_CONTAINER, REMOTE_CC_CREATE).and(GRANULARITY, SESSION);
        operations.add(hotrodSessionAddress(HOTROD_SESSION_UPDATE), values);
        operations.add(hotrodSessionAddress(HOTROD_SESSION_DELETE), values);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(hotrodSessionAddress(HOTROD_SESSION_DELETE));
        operations.removeIfExists(hotrodSessionAddress(HOTROD_SESSION_UPDATE));
        operations.removeIfExists(hotrodSessionAddress(HOTROD_SESSION_CREATE));
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CC_CREATE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING));
    }

    @Page private DistributableWebPage page;
    @Inject private CrudOperations crud;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("dw-hotrod-session-management-item");
        table = page.getHotrodSessionManagementTable();
        form = page.getHotrodSessionManagementForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(hotrodSessionAddress(HOTROD_SESSION_CREATE), table, f -> {
            f.text(NAME, HOTROD_SESSION_CREATE);
            f.text(REMOTE_CACHE_CONTAINER, REMOTE_CC_CREATE);
        });
    }

    @Test
    public void update() throws Exception {
        table.select(HOTROD_SESSION_UPDATE);
        crud.update(hotrodSessionAddress(HOTROD_SESSION_UPDATE), form,
                f -> f.select(GRANULARITY, ATTRIBUTE),
                verifier -> verifier.verifyAttribute(GRANULARITY, ATTRIBUTE));
    }

    @Test
    public void reset() throws Exception {
        table.select(HOTROD_SESSION_UPDATE);
        crud.reset(hotrodSessionAddress(HOTROD_SESSION_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        table.select(HOTROD_SESSION_DELETE);
        crud.delete(hotrodSessionAddress(HOTROD_SESSION_DELETE), table, HOTROD_SESSION_DELETE);
    }
}
