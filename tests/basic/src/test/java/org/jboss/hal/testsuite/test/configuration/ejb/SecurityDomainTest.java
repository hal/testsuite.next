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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
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
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

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

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
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
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, ASD_CREATE);
        dialog.getForm().text(SECURITY_DOMAIN, securityDomain);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(applicationSecurityDomainAddress(ASD_CREATE), client).verifyExists();
    }

    @Test
    public void update() throws Exception {
        table.select(ASD_UPDATE);
        form.edit();
        form.text(SECURITY_DOMAIN, otherSecurityDomain);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(applicationSecurityDomainAddress(ASD_UPDATE), client)
                .verifyAttribute(SECURITY_DOMAIN, otherSecurityDomain);
    }

    @Test
    public void updateEmptySecurityDomain() throws Exception {
        table.select(ASD_UPDATE);
        form.edit();
        form.clear(SECURITY_DOMAIN);
        form.trySave();
        form.expectError(SECURITY_DOMAIN);
    }

    @Test
    public void delete() throws Exception {
        table.remove(ASD_DELETE);

        console.verifySuccess();
        new ResourceVerifier(applicationSecurityDomainAddress(ASD_DELETE), client).verifyDoesNotExist();
    }
}
