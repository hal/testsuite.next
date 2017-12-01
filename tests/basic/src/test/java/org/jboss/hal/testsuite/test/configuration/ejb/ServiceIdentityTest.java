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

import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.ELYTRON_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.OUTFLOW_SECURITY_DOMAINS;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.SERVICE_IDENTITY_ADDRESS;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class ServiceIdentityTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String securityDomain;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ModelNodeResult result = operations.readChildrenNames(ELYTRON_ADDRESS, SECURITY_DOMAIN);
        List<String> secDomains = result.stringListValue();
        if (secDomains.size() > 1) {
            securityDomain = secDomains.get(0);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(SERVICE_IDENTITY_ADDRESS);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;
    private EmptyState emptyState;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-service-item", "ejb3-service-identity-item");

        form = page.getServiceIdentityForm();
        emptyState = page.getServiceIdentityEmptyState();
    }

    // order is important: add, addOutflow, remove

    @Test
    public void add() throws Exception {
        emptyState.mainAction();

        console.verifySuccess();
        new ResourceVerifier(EJBFixtures.SERVICE_IDENTITY_ADDRESS, client)
                .verifyExists();
    }

    @Test
    public void addOutflow() throws Exception {
        crud.update(SERVICE_IDENTITY_ADDRESS, form,
                f -> f.list(OUTFLOW_SECURITY_DOMAINS).add(securityDomain),
                resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(OUTFLOW_SECURITY_DOMAINS,
                        securityDomain));
    }

    @Test
    public void remove() throws Exception {
        crud.deleteSingleton(SERVICE_IDENTITY_ADDRESS, form);
    }
}
