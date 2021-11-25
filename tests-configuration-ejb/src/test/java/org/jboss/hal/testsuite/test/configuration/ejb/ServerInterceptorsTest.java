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
package org.jboss.hal.testsuite.test.configuration.ejb;

import java.io.IOException;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SERVER_INTERCEPTORS;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_CLASS_CREATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_CLASS_DELETE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_MODULE_CREATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_MODULE_DELETE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SUBSYSTEM_ADDRESS;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.serverInterceptor;

@RunWith(Arquillian.class)
public class ServerInterceptorsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.writeListAttribute(SUBSYSTEM_ADDRESS, SERVER_INTERCEPTORS,
                serverInterceptor(SI_CLASS_DELETE, SI_MODULE_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, SERVER_INTERCEPTORS);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-container-item", "ejb3-configuration-item");
        form = page.getConfigurationForm();
    }

    @Test
    public void create() throws Exception {
        crud.update(SUBSYSTEM_ADDRESS, form,
                form -> form.list(SERVER_INTERCEPTORS).add(SI_CLASS_CREATE + "," + SI_MODULE_CREATE),
                resourceVerifier -> resourceVerifier.verifyTrue("Server interceptors not updated",
                        this::verifyServerInterceptorCreated));
    }

    @Test
    public void delete() throws Exception {
        crud.update(SUBSYSTEM_ADDRESS, form,
                form -> form.list(SERVER_INTERCEPTORS).removeTags(),
                resourceVerifier -> resourceVerifier.verifyTrue("No server interceptors expected",
                        this::verifyServerInterceptorsDeleted));
    }

    private boolean verifyServerInterceptorCreated() throws IOException {
        ModelNodeResult result = operations.readAttribute(SUBSYSTEM_ADDRESS, SERVER_INTERCEPTORS);
        if (result.isSuccess()) {
            List<ModelNode> serverInterceptors = result.value().asList();
            return serverInterceptors.stream().anyMatch(node ->
                    org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_CLASS_CREATE.equals(node.get(CLASS).asString()) && org.jboss.hal.testsuite.fixtures.EJBFixtures.SI_MODULE_CREATE.equals(node.get(MODULE).asString()));
        }
        return false;
    }

    private boolean verifyServerInterceptorsDeleted() throws IOException {
        ModelNodeResult result = operations.readAttribute(SUBSYSTEM_ADDRESS, SERVER_INTERCEPTORS);
        if (result.isSuccess()) {
            return !result.value().isDefined();
        }
        return false;
    }
}
