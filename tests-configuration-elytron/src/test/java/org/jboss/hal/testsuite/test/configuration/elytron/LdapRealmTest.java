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
package org.jboss.hal.testsuite.test.configuration.elytron;

import com.google.common.base.Joiner;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronSecurityRealmsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTE_MAPPING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DIRECT_VERIFICATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DIR_CONTEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.FROM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.IDENTITY_MAPPING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LDAP_REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.OTP_CREDENTIAL_MAPPER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RDN_IDENTIFIER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TO;
import static org.jboss.hal.dmr.ModelDescriptionConstants.URL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.USER_PASSWORD_MAPPER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.X509_CREDENTIAL_MAPPER;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ALGORITHM_FROM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CERTIFICATE_FROM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTER_NAME;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HASH_FROM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.IDENTITY_ATTRIBUTES_MAPPING_LB;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_AM_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_AM_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_AM_FROM_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_AM_FROM_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_AM_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_OTP_MAPPER_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_OTP_MAPPER_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_OTP_MAPPER_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_USER_MAPPER_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_USER_MAPPER_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_USER_MAPPER_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_X509_MAPPER_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_X509_MAPPER_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_RLM_X509_MAPPER_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SECURITY_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SEED_FROM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SEQUENCE_FROM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.dirContextAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ldapRealmAddress;
@RunWith(Arquillian.class)
public class LdapRealmTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String PROPERTY_DELIMITER = ".";

    @BeforeClass
    public static void beforeTests() throws Exception {

        operations.add(dirContextAddress(DIR_UPDATE), Values.of(URL, ANY_STRING));

        ModelNode amUpd = new ModelNode();
        amUpd.get(FROM).set(LDAP_RLM_AM_FROM_UPD);
        amUpd.get(TO).set(ANY_STRING);
        ModelNode amDel = new ModelNode();
        amDel.get(FROM).set(LDAP_RLM_AM_FROM_DEL);
        amDel.get(TO).set(ANY_STRING);
        Values ldapParams = Values.of(DIR_CONTEXT, DIR_UPDATE)
                .andObject(IDENTITY_MAPPING,
                        Values.of(RDN_IDENTIFIER, ANY_STRING).andList(ATTRIBUTE_MAPPING, amUpd, amDel));
        Values ldapParamsUser = Values.of(DIR_CONTEXT, DIR_UPDATE)
                .andObject(IDENTITY_MAPPING, Values.of(RDN_IDENTIFIER, ANY_STRING)
                        .andObject(USER_PASSWORD_MAPPER, Values.of(FROM, ANY_STRING)));
        Values ldapParamsOtp = Values.of(DIR_CONTEXT, DIR_UPDATE)
                .andObject(IDENTITY_MAPPING, Values.of(RDN_IDENTIFIER, ANY_STRING)
                        .andObject(OTP_CREDENTIAL_MAPPER, Values.of(ALGORITHM_FROM, ANY_STRING)
                                .and(HASH_FROM, ANY_STRING)
                                .and(SEED_FROM, ANY_STRING)
                                .and(SEQUENCE_FROM, ANY_STRING)));
        Values ldapParamsX509 = Values.of(DIR_CONTEXT, DIR_UPDATE)
                .andObject(IDENTITY_MAPPING, Values.of(RDN_IDENTIFIER, ANY_STRING)
                        .andObject(X509_CREDENTIAL_MAPPER, Values.of(CERTIFICATE_FROM, ANY_STRING)));

        operations.add(ldapRealmAddress(LDAP_RLM_UPDATE), ldapParams);
        operations.add(ldapRealmAddress(LDAP_RLM_DELETE), ldapParams);

        operations.add(ldapRealmAddress(LDAP_RLM_AM_CRT), Values.of(DIR_CONTEXT, DIR_UPDATE)
                .andObject(IDENTITY_MAPPING, Values.of(RDN_IDENTIFIER, ANY_STRING)));
        operations.add(ldapRealmAddress(LDAP_RLM_AM_UPD), ldapParams);
        operations.add(ldapRealmAddress(LDAP_RLM_AM_DEL), ldapParams);

        operations.add(ldapRealmAddress(LDAP_RLM_USER_MAPPER_CRT), ldapParams);
        operations.add(ldapRealmAddress(LDAP_RLM_USER_MAPPER_UPD), ldapParamsUser);
        operations.add(ldapRealmAddress(LDAP_RLM_USER_MAPPER_DEL), ldapParamsUser);
        operations.add(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_CRT), ldapParams);
        operations.add(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_UPD), ldapParamsOtp);
        operations.add(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_DEL), ldapParamsOtp);
        operations.add(ldapRealmAddress(LDAP_RLM_X509_MAPPER_CRT), ldapParams);
        operations.add(ldapRealmAddress(LDAP_RLM_X509_MAPPER_UPD), ldapParamsX509);
        operations.add(ldapRealmAddress(LDAP_RLM_X509_MAPPER_DEL), ldapParamsX509);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(ldapRealmAddress(LDAP_RLM_DELETE));
        operations.remove(ldapRealmAddress(LDAP_RLM_UPDATE));
        operations.remove(ldapRealmAddress(LDAP_RLM_CREATE));

        operations.remove(ldapRealmAddress(LDAP_RLM_AM_CRT));
        operations.remove(ldapRealmAddress(LDAP_RLM_AM_DEL));
        operations.remove(ldapRealmAddress(LDAP_RLM_AM_UPD));

        operations.remove(ldapRealmAddress(LDAP_RLM_USER_MAPPER_CRT));
        operations.remove(ldapRealmAddress(LDAP_RLM_USER_MAPPER_UPD));
        operations.remove(ldapRealmAddress(LDAP_RLM_USER_MAPPER_DEL));
        operations.remove(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_CRT));
        operations.remove(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_UPD));
        operations.remove(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_DEL));
        operations.remove(ldapRealmAddress(LDAP_RLM_X509_MAPPER_CRT));
        operations.remove(ldapRealmAddress(LDAP_RLM_X509_MAPPER_UPD));
        operations.remove(ldapRealmAddress(LDAP_RLM_X509_MAPPER_DEL));

        operations.remove(dirContextAddress(DIR_UPDATE));
    }

    @Page private ElytronSecurityRealmsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- ldap-realm

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        crud.create(ldapRealmAddress(LDAP_RLM_CREATE), table, f -> {
            f.text(NAME, LDAP_RLM_CREATE);
            f.text(DIR_CONTEXT, DIR_UPDATE);
            f.text(RDN_IDENTIFIER, ANY_STRING);
        });
    }

    @Test
    public void tryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> {
            f.text(NAME, LDAP_RLM_CREATE);
            f.text(RDN_IDENTIFIER, ANY_STRING);
        }, DIR_CONTEXT);
    }

    @Test
    public void update() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_UPDATE);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, TAB));
        crud.update(ldapRealmAddress(LDAP_RLM_UPDATE), form, DIRECT_VERIFICATION, true);
    }

    @Test
    public void tryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_UPDATE);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, TAB));
        crud.updateWithError(form, f -> f.clear(DIR_CONTEXT), DIR_CONTEXT);
    }


    @Test
    public void delete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        crud.delete(ldapRealmAddress(LDAP_RLM_DELETE), table, LDAP_RLM_DELETE);
    }

    @Test
    public void updateIdentityMapping() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmIdentityMappingForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_UPDATE);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, IDENTITY_MAPPING, TAB));
        String filterName = Random.name();
        crud.update(ldapRealmAddress(LDAP_RLM_UPDATE), form, f -> f.text(FILTER_NAME, filterName),
                ver -> ver.verifyAttribute(IDENTITY_MAPPING + PROPERTY_DELIMITER + FILTER_NAME, filterName));
    }

    // ------------ user-password-mapper
    @Test
    public void userPasswordMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmUserPasswordMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_USER_MAPPER_CRT);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, USER_PASSWORD_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, USER_PASSWORD_MAPPER, FROM);
        crud.createSingleton(ldapRealmAddress(LDAP_RLM_USER_MAPPER_CRT), form, f -> f.text(FROM, ANY_STRING),
                ver -> ver.verifyAttribute(attr, ANY_STRING));
    }

    @Test
    public void userPasswordMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmUserPasswordMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_USER_MAPPER_UPD);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, USER_PASSWORD_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, USER_PASSWORD_MAPPER, FROM);
        String from = Random.name();
        crud.update(ldapRealmAddress(LDAP_RLM_USER_MAPPER_UPD), form, f -> f.text(FROM, from),
                ver -> ver.verifyAttribute(attr, from));
    }

    @Test
    public void userPasswordMapperRemove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmUserPasswordMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_USER_MAPPER_DEL);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, USER_PASSWORD_MAPPER, TAB));
        crud.deleteSingleton(ldapRealmAddress(LDAP_RLM_USER_MAPPER_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(IDENTITY_MAPPING + PROPERTY_DELIMITER +  USER_PASSWORD_MAPPER));
    }

    // ------------ otp-credential-mapper
    @Test
    public void otpCredentialMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmOTPCredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_OTP_MAPPER_CRT);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, OTP_CREDENTIAL_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, OTP_CREDENTIAL_MAPPER);
        ModelNode otp = new ModelNode();
        otp.get(ALGORITHM_FROM).set(ANY_STRING);
        otp.get(HASH_FROM).set(ANY_STRING);
        otp.get(SEED_FROM).set(ANY_STRING);
        otp.get(SEQUENCE_FROM).set(ANY_STRING);
        crud.createSingleton(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_CRT), form, f -> {
                    f.text(ALGORITHM_FROM, ANY_STRING);
                    f.text(HASH_FROM, ANY_STRING);
                    f.text(SEED_FROM, ANY_STRING);
                    f.text(SEQUENCE_FROM, ANY_STRING);
                },
                ver -> ver.verifyAttribute(attr, otp));
    }

    @Test
    public void otpCredentialMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmOTPCredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_OTP_MAPPER_UPD);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, OTP_CREDENTIAL_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, OTP_CREDENTIAL_MAPPER, ALGORITHM_FROM);
        String algFrom = Random.name();
        crud.update(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_UPD), form,
                f -> f.text(ALGORITHM_FROM, algFrom), ver -> ver.verifyAttribute(attr, algFrom));
    }

    @Test
    public void otpCredentialMapperRemove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmOTPCredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_OTP_MAPPER_DEL);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, OTP_CREDENTIAL_MAPPER, TAB));
        crud.deleteSingleton(ldapRealmAddress(LDAP_RLM_OTP_MAPPER_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(IDENTITY_MAPPING + PROPERTY_DELIMITER +  OTP_CREDENTIAL_MAPPER));
    }

    // ------------ x509-credential-mapper
    @Test
    public void x509CredentialMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmX509CredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_X509_MAPPER_CRT);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, X509_CREDENTIAL_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, X509_CREDENTIAL_MAPPER, CERTIFICATE_FROM);
        String certFrom = Random.name();
        crud.createSingleton(ldapRealmAddress(LDAP_RLM_X509_MAPPER_CRT), form, f -> f.text(CERTIFICATE_FROM, certFrom),
                ver -> ver.verifyAttribute(attr, certFrom));
    }

    @Test
    public void x509CredentialMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmX509CredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_X509_MAPPER_UPD);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, X509_CREDENTIAL_MAPPER, TAB));
        String attr = Joiner.on(PROPERTY_DELIMITER).join(IDENTITY_MAPPING, X509_CREDENTIAL_MAPPER, CERTIFICATE_FROM);
        String certFrom = Random.name();
        crud.update(ldapRealmAddress(LDAP_RLM_X509_MAPPER_UPD), form,
                f -> f.text(CERTIFICATE_FROM, certFrom), ver -> ver.verifyAttribute(attr, certFrom));
    }

    @Test
    public void x509CredentialMapperRemove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment table = page.getLdapRealmTable();
        FormFragment form = page.getLdapRealmX509CredentialMapperForm();
        table.bind(form);
        table.filterAndSelect(LDAP_RLM_X509_MAPPER_DEL);
        page.getLdapRealmFormTabs().select(Ids.build(ELYTRON, LDAP_REALM, X509_CREDENTIAL_MAPPER, TAB));
        crud.deleteSingleton(ldapRealmAddress(LDAP_RLM_X509_MAPPER_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(IDENTITY_MAPPING + PROPERTY_DELIMITER +  X509_CREDENTIAL_MAPPER));
    }

    // ------------ attribute-mapping action
    @Test
    public void attributeMappingCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment ldTable = page.getLdapRealmTable();
        ldTable.filterAndSelect(LDAP_RLM_AM_CRT);
        ldTable.action(LDAP_RLM_AM_CRT, IDENTITY_ATTRIBUTES_MAPPING_LB);
        TableFragment table = page.getLdapRealmIdentityAttributeMappingsTable();
        waitGui().until().element(table.getRoot()).is().visible();
        String attr = IDENTITY_MAPPING + PROPERTY_DELIMITER +  ATTRIBUTE_MAPPING;
        crud.create(ldapRealmAddress(LDAP_RLM_AM_CRT), table, f -> {
                    f.text(FROM, ANY_STRING);
                    f.text(TO, ANY_STRING);
                },
                ver -> ver.verifyListAttributeContainsSingleValue(attr, FROM, ANY_STRING));
    }

    @Test
    public void attributeMappingTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment ldTable = page.getLdapRealmTable();
        ldTable.filterAndSelect(LDAP_RLM_AM_CRT);
        ldTable.action(LDAP_RLM_AM_CRT, IDENTITY_ATTRIBUTES_MAPPING_LB);
        TableFragment table = page.getLdapRealmIdentityAttributeMappingsTable();
        waitGui().until().element(table.getRoot()).is().visible();
        crud.createWithErrorAndCancelDialog(table, f -> { }, FROM);
    }

    @Test
    public void attributeMappingUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment ldTable = page.getLdapRealmTable();
        ldTable.filterAndSelect(LDAP_RLM_AM_UPD);
        ldTable.action(LDAP_RLM_AM_UPD, IDENTITY_ATTRIBUTES_MAPPING_LB);
        TableFragment table = page.getLdapRealmIdentityAttributeMappingsTable();
        FormFragment form = page.getLdapRealmIdentityAttributeMappingsForm();
        table.bind(form);
        waitGui().until().element(table.getRoot()).is().visible();
        String attr = IDENTITY_MAPPING + PROPERTY_DELIMITER +  ATTRIBUTE_MAPPING;
        String to = Random.name();
        table.select(LDAP_RLM_AM_FROM_UPD);
        crud.update(ldapRealmAddress(LDAP_RLM_AM_UPD), form, f -> {
            f.number("role-recursion", Random.number());
            f.text(TO, to);
                },
                ver -> ver.verifyListAttributeContainsSingleValue(attr, TO, to));
    }

    @Test
    public void attributeMappingDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, LDAP_REALM_ITEM);
        TableFragment ldTable = page.getLdapRealmTable();
        ldTable.filterAndSelect(LDAP_RLM_AM_DEL);
        ldTable.action(LDAP_RLM_AM_DEL, IDENTITY_ATTRIBUTES_MAPPING_LB);
        TableFragment table = page.getLdapRealmIdentityAttributeMappingsTable();
        waitGui().until().element(table.getRoot()).is().visible();
        String attr = IDENTITY_MAPPING + PROPERTY_DELIMITER +  ATTRIBUTE_MAPPING;
        crud.delete(ldapRealmAddress(LDAP_RLM_AM_DEL), table, LDAP_RLM_AM_FROM_DEL,
                ver -> ver.verifyListAttributeDoesNotContainSingleValue(attr, FROM, LDAP_RLM_AM_FROM_DEL));
    }

}
