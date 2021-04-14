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
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ASD_CREATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ASD_DELETE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ASD_UPDATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ELYTRON_ADDRESS;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.applicationSecurityDomainAddress;

@RunWith(Arquillian.class)
public class SecurityDomainTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String securityDomain;
    private static String otherSecurityDomain;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ModelNodeResult result = operations.readChildrenNames(ELYTRON_ADDRESS, SECURITY_DOMAIN);
        List<String> secDomains = result.stringListValue();
        if (secDomains.size() > 1) {
            securityDomain = secDomains.get(0);
            otherSecurityDomain = secDomains.get(1);
        }
        operations.add(applicationSecurityDomainAddress(ASD_UPDATE), Values.of(SECURITY_DOMAIN, securityDomain));
        operations.add(applicationSecurityDomainAddress(ASD_DELETE), Values.of(SECURITY_DOMAIN, securityDomain));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(applicationSecurityDomainAddress(ASD_CREATE));
        operations.removeIfExists(applicationSecurityDomainAddress(ASD_UPDATE));
        operations.removeIfExists(applicationSecurityDomainAddress(ASD_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("ejb3-app-security-domain-item");

        table = page.getSecurityDomainTable();
        form = page.getSecurityDomainForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(applicationSecurityDomainAddress(ASD_CREATE), table, form -> {
            form.text(NAME, ASD_CREATE);
            form.text(SECURITY_DOMAIN, securityDomain);
        });
    }

    @Test
    public void update() throws Exception {
        table.select(ASD_UPDATE);
        crud.update(applicationSecurityDomainAddress(ASD_UPDATE), form, SECURITY_DOMAIN, otherSecurityDomain);
    }

    @Test
    public void updateEmptySecurityDomain() {
        table.select(ASD_UPDATE);
        crud.updateWithError(form, f -> f.clear(SECURITY_DOMAIN), SECURITY_DOMAIN);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(applicationSecurityDomainAddress(ASD_DELETE), table, ASD_DELETE);
    }
}
