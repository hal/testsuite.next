/*
 * Copyright 2015-2018 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.test.configuration.security_legacy;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SecurityDomainLegacyPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REQUIRED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.ACL;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.ACL_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.ACL_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.ACL_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUDIT;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUDIT_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUDIT_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUDIT_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHENTICATION;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHENT_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHENT_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHENT_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHORIZATION;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHOR_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHOR_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.AUTHOR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.CACHE_TYPE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.CLASSIC;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.CODE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.FLAG;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.IDENTITY_TRUST;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.IDENT_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.IDENT_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.IDENT_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.MAPPING;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.MAPPING_CREATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.MAPPING_DELETE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.MAPPING_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_ACL_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_AUDIT_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_AUTHENTICATION_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_AUTHORIZATION_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_CONFIGURATION_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_IDENTITY_TRUST_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOMAIN_MAPPING_ITEM;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.SEC_DOM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.aclModuleAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.auditProviderModuleAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.authenticationLoginModuleAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.authorizationPolicyModuleAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.identityTrustModuleAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.mappingAddress;
import static org.jboss.hal.testsuite.fixtures.SecurityLegacyFixtures.securityDomainAddress;

@RunWith(Arquillian.class)
public class SecurityDomainTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(securityDomainAddress(SEC_DOM_UPDATE));

        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(ACL, CLASSIC));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(AUDIT, CLASSIC));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(AUTHENTICATION, CLASSIC));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(AUTHORIZATION, CLASSIC));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(MAPPING, CLASSIC));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE).and(IDENTITY_TRUST, CLASSIC));

        Values params = Values.of(CODE, ANY_STRING).and(FLAG, REQUIRED);
        operations.add(aclModuleAddress(SEC_DOM_UPDATE, ACL_UPDATE), params);
        operations.add(aclModuleAddress(SEC_DOM_UPDATE, ACL_DELETE), params);

        operations.add(auditProviderModuleAddress(SEC_DOM_UPDATE, AUDIT_UPDATE), Values.of(CODE, ANY_STRING));
        operations.add(auditProviderModuleAddress(SEC_DOM_UPDATE, AUDIT_DELETE), Values.of(CODE, ANY_STRING));

        operations.add(authenticationLoginModuleAddress(SEC_DOM_UPDATE, AUTHENT_UPDATE), params);
        operations.add(authenticationLoginModuleAddress(SEC_DOM_UPDATE, AUTHENT_DELETE), params);

        operations.add(authorizationPolicyModuleAddress(SEC_DOM_UPDATE, AUTHOR_UPDATE), params);
        operations.add(authorizationPolicyModuleAddress(SEC_DOM_UPDATE, AUTHOR_DELETE), params);

        operations.add(identityTrustModuleAddress(SEC_DOM_UPDATE, IDENT_UPDATE), params);
        operations.add(identityTrustModuleAddress(SEC_DOM_UPDATE, IDENT_DELETE), params);

        Values mappingParams = Values.of(CODE, ANY_STRING).and(TYPE, ANY_STRING);
        operations.add(mappingAddress(SEC_DOM_UPDATE, MAPPING_UPDATE), mappingParams);
        operations.add(mappingAddress(SEC_DOM_UPDATE, MAPPING_DELETE), mappingParams);

    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE));
    }

    @Page private SecurityDomainLegacyPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, SEC_DOM_UPDATE);
    }

    @Test
    public void configurationUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(SEC_DOMAIN_CONFIGURATION_ITEM);
        FormFragment form = page.getConfigurationForm();
        crud.update(securityDomainAddress(SEC_DOM_UPDATE), form, f -> f.select(CACHE_TYPE, DEFAULT),
                ver -> ver.verifyAttribute(CACHE_TYPE, DEFAULT));
        // undefine it as it was before the update
        operations.undefineAttribute(securityDomainAddress(SEC_DOM_UPDATE), CACHE_TYPE);
    }

    @Test
    public void authenticationCreate() throws Exception {
        templateCreate(SEC_DOMAIN_AUTHENTICATION_ITEM, page.getAuthenticationTable(), AUTHENT_CREATE,
                authenticationLoginModuleAddress(SEC_DOM_UPDATE, AUTHENT_CREATE));
    }

    @Test
    public void authenticationUpdate() throws Exception {
        templateUpdate(SEC_DOMAIN_AUTHENTICATION_ITEM, page.getAuthenticationTable(), page.getAuthenticationForm(),
                AUTHENT_UPDATE, authenticationLoginModuleAddress(SEC_DOM_UPDATE, AUTHENT_UPDATE));
    }

    @Test
    public void authenticationDelete() throws Exception {
        templateDelete(SEC_DOMAIN_AUTHENTICATION_ITEM, page.getAuthenticationTable(), AUTHENT_DELETE,
                authenticationLoginModuleAddress(SEC_DOM_UPDATE, AUTHENT_DELETE));
    }

    @Test
    public void authorizationCreate() throws Exception {
        templateCreate(SEC_DOMAIN_AUTHORIZATION_ITEM, page.getAuthorizationTable(), AUTHOR_CREATE,
                authorizationPolicyModuleAddress(SEC_DOM_UPDATE, AUTHOR_CREATE));
    }

    @Test
    public void authorizationUpdate() throws Exception {
        templateUpdate(SEC_DOMAIN_AUTHORIZATION_ITEM, page.getAuthorizationTable(), page.getAuthorizationForm(),
                AUTHOR_UPDATE, authorizationPolicyModuleAddress(SEC_DOM_UPDATE, AUTHOR_UPDATE));
    }

    @Test
    public void authorizationDelete() throws Exception {
        templateDelete(SEC_DOMAIN_AUTHORIZATION_ITEM, page.getAuthorizationTable(), AUTHOR_DELETE,
                authorizationPolicyModuleAddress(SEC_DOM_UPDATE, AUTHOR_DELETE));
    }

    @Test
    public void aclCreate() throws Exception {
        templateCreate(SEC_DOMAIN_ACL_ITEM, page.getAclTable(), ACL_CREATE,
                aclModuleAddress(SEC_DOM_UPDATE, ACL_CREATE));
    }

    @Test
    public void aclUpdate() throws Exception {
        templateUpdate(SEC_DOMAIN_ACL_ITEM, page.getAclTable(), page.getAclForm(),
                ACL_UPDATE, aclModuleAddress(SEC_DOM_UPDATE, ACL_UPDATE));
    }

    @Test
    public void aclDelete() throws Exception {
        templateDelete(SEC_DOMAIN_ACL_ITEM, page.getAclTable(), ACL_DELETE,
                aclModuleAddress(SEC_DOM_UPDATE, ACL_DELETE));
    }

    @Test
    public void auditCreate() throws Exception {
        templateCreate(SEC_DOMAIN_AUDIT_ITEM, page.getAuditTable(), AUDIT_CREATE,
                auditProviderModuleAddress(SEC_DOM_UPDATE, AUDIT_CREATE));
    }

    @Test
    public void auditUpdate() throws Exception {
        templateUpdate(SEC_DOMAIN_AUDIT_ITEM, page.getAuditTable(), page.getAuditForm(),
                AUDIT_UPDATE, auditProviderModuleAddress(SEC_DOM_UPDATE, AUDIT_UPDATE));
    }

    @Test
    public void auditDelete() throws Exception {
        templateDelete(SEC_DOMAIN_AUDIT_ITEM, page.getAuditTable(), AUDIT_DELETE,
                auditProviderModuleAddress(SEC_DOM_UPDATE, AUDIT_DELETE));
    }

    @Test
    public void identityTrustCreate() throws Exception {
        templateCreate(SEC_DOMAIN_IDENTITY_TRUST_ITEM, page.getIdentityTrustTable(), IDENT_CREATE,
                identityTrustModuleAddress(SEC_DOM_UPDATE, IDENT_CREATE));
    }

    @Test
    public void identityTrustUpdate() throws Exception {
        templateUpdate(SEC_DOMAIN_IDENTITY_TRUST_ITEM, page.getIdentityTrustTable(), page.getIdentityTrustForm(),
                IDENT_UPDATE, identityTrustModuleAddress(SEC_DOM_UPDATE, IDENT_UPDATE));
    }

    @Test
    public void identityTrustDelete() throws Exception {
        templateDelete(SEC_DOMAIN_IDENTITY_TRUST_ITEM, page.getIdentityTrustTable(), IDENT_DELETE,
                identityTrustModuleAddress(SEC_DOM_UPDATE, IDENT_DELETE));
    }

    @Test
    public void mappingCreate() throws Exception {
        console.verticalNavigation().selectPrimary(SEC_DOMAIN_MAPPING_ITEM);
        crud.create(mappingAddress(SEC_DOM_UPDATE, MAPPING_CREATE), page.getMappingTable(), f -> {
            f.text(NAME, MAPPING_CREATE);
            f.text(CODE, ANY_STRING);
            f.text(TYPE, ANY_STRING);
        });

    }

    @Test
    public void mappingUpdate() throws Exception {
        TableFragment table = page.getMappingTable();
        FormFragment form = page.getMappingForm();
        console.verticalNavigation().selectPrimary(SEC_DOMAIN_MAPPING_ITEM);
        table.bind(form);
        table.select(MAPPING_UPDATE);
        String code = Random.name();
        crud.update(mappingAddress(SEC_DOM_UPDATE, MAPPING_UPDATE), form, CODE, code);
    }

    @Test
    public void mappingDelete() throws Exception {
        console.verticalNavigation().selectPrimary(SEC_DOMAIN_MAPPING_ITEM);
        crud.delete(mappingAddress(SEC_DOM_UPDATE, MAPPING_DELETE), page.getMappingTable(), MAPPING_DELETE);

    }

    // ---------------- crud templates

    public void templateCreate(String item, TableFragment table, String resourceName, Address address) throws Exception {
        console.verticalNavigation().selectPrimary(item);
        crud.create(address, table, f -> {
            f.text(NAME, resourceName);
            f.text(CODE, ANY_STRING);
        });
    }

    public void templateUpdate(String item, TableFragment table, FormFragment form, String resourceName,
            Address address) throws Exception {
        console.verticalNavigation().selectPrimary(item);
        table.bind(form);
        table.select(resourceName);
        String code = Random.name();
        crud.update(address, form, CODE, code);
    }

    public void templateDelete(String item, TableFragment table, String resourceName, Address address)
            throws Exception {
        console.verticalNavigation().selectPrimary(item);
        crud.delete(address, table, resourceName);
    }

}
