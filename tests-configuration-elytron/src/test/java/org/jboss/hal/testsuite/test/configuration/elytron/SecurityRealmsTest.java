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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronSecurityRealmsPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUPS_ATTRIBUTE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.IDENTITY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.USERS_PROPERTIES;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGGREGATE_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.APP_ROLES_PROPS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.APP_USERS_PROPS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ATTRIBUTE_NAME;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUDIENCE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHENTICATION_REALM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHORIZATION_REALM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CACHING_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CAC_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CAC_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CAC_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CLIENT_ID;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CLIENT_SECRET;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONSTANT_REALM_MAPPER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CON_RM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CON_RM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CON_RM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CREDENTIAL_REFERENCE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DELEGATE_REALM_MAPPER;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIGEST_REALM_NAME;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYSTEM_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYS_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYS_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYS_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYS_RLM_UPDATE2;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILESYS_RLM_UPDATE3;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.GROUPS_PROPERTIES;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.IDENTITY_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.IDEN_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.IDEN_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.IDEN_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.INTROSPECTION_URL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.JBOSS_SRV_CONFIG_DIR;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.JKS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.JWT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEYSTORE_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KS_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KS_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KS_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LEVELS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAPPED_REGEX_REALM_MAPPER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAPP_RM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAPP_RM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAPP_RM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAXIMUM_AGE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.OAUTH2_INTROSPECTION;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PRINCIPAL_CLAIM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROPERTIES_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_GP_ADD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_GP_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_GP_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROP_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PUBLIC_KEY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.REALM_MAP;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.REALM_MAPPER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.REGEX_PATTERN;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SECURITY_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SIMPLE_REGEX_REALM_MAPPER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SIMP_RM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SIMP_RM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SIMP_RM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_JWT_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_JWT_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_JWT_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_OAU_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_OAU_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_OAU_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TKN_RLM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.TOKEN_REALM_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.aggregateRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.cachingRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.constantRealmMapperAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.filesystemRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.identityRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keystoreRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.mappedRegexRealmMapperAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.propertiesRealmAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.simpleRegexRealmMapperAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.tokenRealmAddress;

@RunWith(Arquillian.class)
public class SecurityRealmsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String PROPERTY_DELMITER = ".";

    @BeforeClass
    public static void beforeTests() throws Exception {

        // a realm is required for aggregate-realm, so create it first
        operations.add(filesystemRealmAddress(FILESYS_RLM_UPDATE), Values.of(PATH, ANY_STRING));
        operations.add(filesystemRealmAddress(FILESYS_RLM_UPDATE2), Values.of(PATH, ANY_STRING));
        operations.add(filesystemRealmAddress(FILESYS_RLM_UPDATE3), Values.of(PATH, ANY_STRING));
        operations.add(filesystemRealmAddress(FILESYS_RLM_DELETE), Values.of(PATH, ANY_STRING));

        operations.add(aggregateRealmAddress(AGG_RLM_UPDATE),
                Values.of(AUTHENTICATION_REALM, FILESYS_RLM_UPDATE).and(AUTHORIZATION_REALM, FILESYS_RLM_UPDATE2));
        operations.add(aggregateRealmAddress(AGG_RLM_DELETE),
                Values.of(AUTHENTICATION_REALM, FILESYS_RLM_UPDATE).and(AUTHORIZATION_REALM, FILESYS_RLM_UPDATE2));

        operations.add(cachingRealmAddress(CAC_RLM_UPDATE), Values.of(REALM, FILESYS_RLM_UPDATE));
        operations.add(cachingRealmAddress(CAC_RLM_DELETE), Values.of(REALM, FILESYS_RLM_UPDATE));

        operations.add(identityRealmAddress(IDEN_RLM_UPDATE), Values.of(IDENTITY, ANY_STRING));
        operations.add(identityRealmAddress(IDEN_RLM_DELETE), Values.of(IDENTITY, ANY_STRING));

        ModelNode credRef = new ModelNode();
        credRef.get(CLEAR_TEXT).set(ANY_STRING);
        Values ksParams = Values.of(TYPE, JKS).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(keyStoreAddress(KEY_ST_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_CREATE), ksParams);

        operations.add(keystoreRealmAddress(KS_RLM_UPDATE), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(keystoreRealmAddress(KS_RLM_DELETE), Values.of(KEY_STORE, KEY_ST_UPDATE));

        Values propParams = Values.ofObject(USERS_PROPERTIES,
                Values.of(PATH, APP_USERS_PROPS).and(RELATIVE_TO, JBOSS_SRV_CONFIG_DIR));
        Values propGroupParams = Values.ofObject(USERS_PROPERTIES,
                Values.of(PATH, APP_USERS_PROPS).and(RELATIVE_TO, JBOSS_SRV_CONFIG_DIR))
                .andObject(GROUPS_PROPERTIES, Values.of(PATH, APP_ROLES_PROPS).and(RELATIVE_TO, JBOSS_SRV_CONFIG_DIR));
        operations.add(propertiesRealmAddress(PROP_RLM_UPDATE), propParams);
        operations.add(propertiesRealmAddress(PROP_RLM_DELETE), propParams);
        operations.add(propertiesRealmAddress(PROP_RLM_GP_ADD), propParams);
        operations.add(propertiesRealmAddress(PROP_RLM_GP_UPD), propGroupParams);
        operations.add(propertiesRealmAddress(PROP_RLM_GP_DEL), propGroupParams);

        Values jwtParams = Values.ofObject(JWT, Values.ofList(AUDIENCE, ANY_STRING));
        String url = "http://" + ANY_STRING;
        Values oauth2Params = Values.ofObject(OAUTH2_INTROSPECTION,
                Values.of(CLIENT_ID, ANY_STRING).and(CLIENT_SECRET, ANY_STRING).and(INTROSPECTION_URL, url));
        operations.add(tokenRealmAddress(TKN_RLM_UPDATE));
        operations.add(tokenRealmAddress(TKN_RLM_DELETE));
        operations.add(tokenRealmAddress(TKN_RLM_JWT_CRT));
        operations.add(tokenRealmAddress(TKN_RLM_JWT_UPD), jwtParams);
        operations.add(tokenRealmAddress(TKN_RLM_JWT_DEL), jwtParams);
        operations.add(tokenRealmAddress(TKN_RLM_OAU_CRT));
        operations.add(tokenRealmAddress(TKN_RLM_OAU_UPD), oauth2Params);
        operations.add(tokenRealmAddress(TKN_RLM_OAU_DEL), oauth2Params);

        operations.add(constantRealmMapperAddress(CON_RM_UPDATE), Values.of(REALM_NAME, ANY_STRING));
        operations.add(constantRealmMapperAddress(CON_RM_DELETE), Values.of(REALM_NAME, ANY_STRING));

        Values mappedParams = Values.of(PATTERN, REGEX_PATTERN).andObject(REALM_MAP, Values.of("a", "b"));
        operations.add(mappedRegexRealmMapperAddress(MAPP_RM_UPDATE), mappedParams);
        operations.add(mappedRegexRealmMapperAddress(MAPP_RM_DELETE), mappedParams);

        Values simpleParams = Values.of(PATTERN, REGEX_PATTERN);
        operations.add(simpleRegexRealmMapperAddress(SIMP_RM_UPDATE), simpleParams);
        operations.add(simpleRegexRealmMapperAddress(SIMP_RM_DELETE), simpleParams);

    }

    @AfterClass
    public static void tearDown() throws Exception {

        operations.remove(aggregateRealmAddress(AGG_RLM_UPDATE));
        operations.remove(aggregateRealmAddress(AGG_RLM_DELETE));
        operations.remove(aggregateRealmAddress(AGG_RLM_CREATE));

        operations.remove(cachingRealmAddress(CAC_RLM_UPDATE));
        operations.remove(cachingRealmAddress(CAC_RLM_CREATE));
        operations.remove(cachingRealmAddress(CAC_RLM_DELETE));

        operations.remove(identityRealmAddress(IDEN_RLM_DELETE));
        operations.remove(identityRealmAddress(IDEN_RLM_UPDATE));
        operations.remove(identityRealmAddress(IDEN_RLM_CREATE));

        // remove the filesystem-realm last as it is required for other resources
        operations.remove(filesystemRealmAddress(FILESYS_RLM_UPDATE));
        operations.remove(filesystemRealmAddress(FILESYS_RLM_UPDATE2));
        operations.remove(filesystemRealmAddress(FILESYS_RLM_UPDATE3));
        operations.remove(filesystemRealmAddress(FILESYS_RLM_DELETE));
        operations.remove(filesystemRealmAddress(FILESYS_RLM_CREATE));


        operations.remove(keystoreRealmAddress(KS_RLM_UPDATE));
        operations.remove(keystoreRealmAddress(KS_RLM_DELETE));
        operations.remove(keystoreRealmAddress(KS_RLM_CREATE));

        operations.remove(keyStoreAddress(KEY_ST_UPDATE));
        operations.remove(keyStoreAddress(KEY_ST_CREATE));

        operations.remove(propertiesRealmAddress(PROP_RLM_UPDATE));
        operations.remove(propertiesRealmAddress(PROP_RLM_CREATE));
        operations.remove(propertiesRealmAddress(PROP_RLM_DELETE));
        operations.remove(propertiesRealmAddress(PROP_RLM_GP_ADD));
        operations.remove(propertiesRealmAddress(PROP_RLM_GP_UPD));
        operations.remove(propertiesRealmAddress(PROP_RLM_GP_DEL));

        operations.remove(tokenRealmAddress(TKN_RLM_CREATE));
        operations.remove(tokenRealmAddress(TKN_RLM_UPDATE));
        operations.remove(tokenRealmAddress(TKN_RLM_DELETE));
        operations.remove(tokenRealmAddress(TKN_RLM_JWT_CRT));
        operations.remove(tokenRealmAddress(TKN_RLM_JWT_UPD));
        operations.remove(tokenRealmAddress(TKN_RLM_JWT_DEL));
        operations.remove(tokenRealmAddress(TKN_RLM_OAU_CRT));
        operations.remove(tokenRealmAddress(TKN_RLM_OAU_UPD));
        operations.remove(tokenRealmAddress(TKN_RLM_OAU_DEL));

        operations.remove(mappedRegexRealmMapperAddress(MAPP_RM_CREATE));
        operations.remove(mappedRegexRealmMapperAddress(MAPP_RM_UPDATE));
        operations.remove(mappedRegexRealmMapperAddress(MAPP_RM_DELETE));

        operations.remove(simpleRegexRealmMapperAddress(SIMP_RM_CREATE));
        operations.remove(simpleRegexRealmMapperAddress(SIMP_RM_UPDATE));
        operations.remove(simpleRegexRealmMapperAddress(SIMP_RM_DELETE));

        operations.remove(constantRealmMapperAddress(CON_RM_CREATE));
        operations.remove(constantRealmMapperAddress(CON_RM_UPDATE));
        operations.remove(constantRealmMapperAddress(CON_RM_DELETE));

    }

    @Page private ElytronSecurityRealmsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- aggregate-realm

    @Test
    public void aggregateRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, AGGREGATE_REALM_ITEM);
        TableFragment table = page.getAggregateRealmTable();

        crud.create(aggregateRealmAddress(AGG_RLM_CREATE), table, f -> {
            f.text(NAME, AGG_RLM_CREATE);
            f.text(AUTHENTICATION_REALM, FILESYS_RLM_UPDATE);
            f.text(AUTHORIZATION_REALM, FILESYS_RLM_UPDATE2);
        });
    }

    @Test
    public void aggregateRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, AGGREGATE_REALM_ITEM);
        TableFragment table = page.getAggregateRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, AGG_RLM_CREATE), AUTHENTICATION_REALM);
    }

    @Test
    public void aggregateRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, AGGREGATE_REALM_ITEM);
        TableFragment table = page.getAggregateRealmTable();
        FormFragment form = page.getAggregateRealmForm();
        table.bind(form);
        table.select(AGG_RLM_UPDATE);
        crud.update(aggregateRealmAddress(AGG_RLM_UPDATE), form, AUTHENTICATION_REALM, FILESYS_RLM_UPDATE3);
    }

    @Test
    public void aggregateRealmTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, AGGREGATE_REALM_ITEM);
        TableFragment table = page.getAggregateRealmTable();
        FormFragment form = page.getAggregateRealmForm();
        table.bind(form);
        table.select(AGG_RLM_UPDATE);
        crud.updateWithError(form, f -> f.clear(AUTHENTICATION_REALM), AUTHENTICATION_REALM);
    }

    @Test
    public void aggregateRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, AGGREGATE_REALM_ITEM);
        TableFragment table = page.getAggregateRealmTable();
        crud.delete(aggregateRealmAddress(AGG_RLM_DELETE), table, AGG_RLM_DELETE);
    }

    // --------------- caching-realm

    @Test
    public void cachingRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, CACHING_REALM_ITEM);
        TableFragment table = page.getCachingRealmTable();

        crud.create(cachingRealmAddress(CAC_RLM_CREATE), table, f -> {
            f.text(NAME, CAC_RLM_CREATE);
            f.text(REALM, FILESYS_RLM_UPDATE);
        });
    }

    @Test
    public void cachingRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, CACHING_REALM_ITEM);
        TableFragment table = page.getCachingRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CAC_RLM_CREATE), REALM);
    }

    @Test
    public void cachingRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, CACHING_REALM_ITEM);
        TableFragment table = page.getCachingRealmTable();
        FormFragment form = page.getCachingRealmForm();
        table.bind(form);
        table.select(CAC_RLM_UPDATE);
        crud.update(cachingRealmAddress(CAC_RLM_UPDATE), form, MAXIMUM_AGE, 123L);
    }

    @Test
    public void cachingRealmTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, CACHING_REALM_ITEM);
        TableFragment table = page.getCachingRealmTable();
        FormFragment form = page.getCachingRealmForm();
        table.bind(form);
        table.select(CAC_RLM_UPDATE);
        crud.updateWithError(form, f -> f.clear(REALM), REALM);
    }

    @Test
    public void cachingRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, CACHING_REALM_ITEM);
        TableFragment table = page.getCachingRealmTable();
        crud.delete(cachingRealmAddress(CAC_RLM_DELETE), table, CAC_RLM_DELETE);
    }

    // --------------- filesystem-realm

    @Test
    public void filesystemRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, FILESYSTEM_REALM_ITEM);
        TableFragment table = page.getFilesystemRealmTable();

        crud.create(filesystemRealmAddress(FILESYS_RLM_CREATE), table, f -> {
            f.text(NAME, FILESYS_RLM_CREATE);
            f.text(PATH, ANY_STRING);
        });
    }

    @Test
    public void filesystemRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, FILESYSTEM_REALM_ITEM);
        TableFragment table = page.getFilesystemRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, FILESYS_RLM_CREATE), PATH);
    }

    @Test
    public void filesystemRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, FILESYSTEM_REALM_ITEM);
        TableFragment table = page.getFilesystemRealmTable();
        FormFragment form = page.getFilesystemRealmForm();
        table.bind(form);
        table.select(FILESYS_RLM_UPDATE);
        crud.update(filesystemRealmAddress(FILESYS_RLM_UPDATE), form, LEVELS, 123);
    }

    @Test
    public void filesystemRealmTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, FILESYSTEM_REALM_ITEM);
        TableFragment table = page.getFilesystemRealmTable();
        FormFragment form = page.getFilesystemRealmForm();
        table.bind(form);
        table.select(FILESYS_RLM_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void filesystemRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, FILESYSTEM_REALM_ITEM);
        TableFragment table = page.getFilesystemRealmTable();
        crud.delete(filesystemRealmAddress(FILESYS_RLM_DELETE), table, FILESYS_RLM_DELETE);
    }

    // --------------- identity-realm

    @Test
    public void identityRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, IDENTITY_REALM_ITEM);
        TableFragment table = page.getIdentityRealmTable();

        crud.create(identityRealmAddress(IDEN_RLM_CREATE), table, f -> {
            f.text(NAME, IDEN_RLM_CREATE);
            f.text(IDENTITY, ANY_STRING);
        });
    }

    @Test
    public void identityRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, IDENTITY_REALM_ITEM);
        TableFragment table = page.getIdentityRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, IDEN_RLM_CREATE), IDENTITY);
    }

    @Test
    public void identityRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, IDENTITY_REALM_ITEM);
        TableFragment table = page.getIdentityRealmTable();
        FormFragment form = page.getIdentityRealmForm();
        table.bind(form);
        table.select(IDEN_RLM_UPDATE);
        crud.update(identityRealmAddress(IDEN_RLM_UPDATE), form, ATTRIBUTE_NAME);
    }

    @Test
    public void identityRealmTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, IDENTITY_REALM_ITEM);
        TableFragment table = page.getIdentityRealmTable();
        FormFragment form = page.getIdentityRealmForm();
        table.bind(form);
        table.select(IDEN_RLM_UPDATE);
        crud.updateWithError(form, f -> f.clear(IDENTITY), IDENTITY);
    }

    @Test
    public void identityRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, IDENTITY_REALM_ITEM);
        TableFragment table = page.getIdentityRealmTable();
        crud.delete(identityRealmAddress(IDEN_RLM_DELETE), table, IDEN_RLM_DELETE);
    }

    // --------------- keystore-realm

    @Test
    public void keystoreRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, KEYSTORE_REALM_ITEM);
        TableFragment table = page.getKeyStoreRealmTable();

        crud.create(keystoreRealmAddress(KS_RLM_CREATE), table, f -> {
            f.text(NAME, KS_RLM_CREATE);
            f.text(KEY_STORE, KEY_ST_UPDATE);
        });
    }

    @Test
    public void keystoreRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, KEYSTORE_REALM_ITEM);
        TableFragment table = page.getKeyStoreRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, KS_RLM_CREATE), KEY_STORE);
    }

    @Test
    public void keystoreRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, KEYSTORE_REALM_ITEM);
        TableFragment table = page.getKeyStoreRealmTable();
        FormFragment form = page.getKeyStoreRealmForm();
        table.bind(form);
        table.select(KS_RLM_UPDATE);
        crud.update(keystoreRealmAddress(KS_RLM_UPDATE), form, KEY_STORE, KEY_ST_CREATE);
    }

    @Test
    public void keystoreRealmTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, KEYSTORE_REALM_ITEM);
        TableFragment table = page.getKeyStoreRealmTable();
        FormFragment form = page.getKeyStoreRealmForm();
        table.bind(form);
        table.select(KS_RLM_UPDATE);
        crud.updateWithError(form, f -> f.clear(KEY_STORE), KEY_STORE);
    }

    @Test
    public void keystoreRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, KEYSTORE_REALM_ITEM);
        TableFragment table = page.getKeyStoreRealmTable();
        crud.delete(keystoreRealmAddress(KS_RLM_DELETE), table, KS_RLM_DELETE);
    }

    // --------------- properties-realm

    @Test
    public void propertiesRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();

        crud.create(propertiesRealmAddress(PROP_RLM_CREATE), table, f -> {
            f.text(NAME, PROP_RLM_CREATE);
            f.text(USERS_PROPERTIES + PATH, APP_USERS_PROPS);
            f.text(USERS_PROPERTIES + RELATIVE_TO, JBOSS_SRV_CONFIG_DIR);
        });
    }

    @Test
    public void propertiesRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, PROP_RLM_CREATE), USERS_PROPERTIES + PATH);
    }

    @Test
    public void propertiesRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        FormFragment form = page.getPropertiesRealmForm();
        table.bind(form);
        table.select(PROP_RLM_UPDATE);
        page.getPropertiesRealmFormTabs().select(Ids.build(Ids.ELYTRON_PROPERTIES_REALM, ATTRIBUTES, TAB));
        crud.update(propertiesRealmAddress(PROP_RLM_UPDATE), form, GROUPS_ATTRIBUTE, ANY_STRING);
    }

    @Test
    public void propertiesRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        crud.delete(propertiesRealmAddress(PROP_RLM_DELETE), table, PROP_RLM_DELETE);
    }

    @Test
    public void propertiesRealmGroupsPropertiesAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        FormFragment form = page.getPropertiesRealmGroupsForm();
        table.bind(form);
        table.select(PROP_RLM_GP_ADD);
        page.getPropertiesRealmFormTabs().select(Ids.build(Ids.ELYTRON_PROPERTIES_REALM, GROUPS_PROPERTIES, TAB));
        crud.createSingleton(propertiesRealmAddress(PROP_RLM_GP_ADD), form, f -> f.text(PATH, ANY_STRING),
                ver -> ver.verifyAttribute(GROUPS_PROPERTIES + PROPERTY_DELMITER + PATH, ANY_STRING));
    }

    @Test
    public void propertiesRealmGroupsPropertiesUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        FormFragment form = page.getPropertiesRealmGroupsForm();
        table.bind(form);
        table.select(PROP_RLM_GP_UPD);
        page.getPropertiesRealmFormTabs().select(Ids.build(Ids.ELYTRON_PROPERTIES_REALM, GROUPS_PROPERTIES, TAB));
        crud.update(propertiesRealmAddress(PROP_RLM_GP_UPD), form, f -> f.text(RELATIVE_TO, ANY_STRING),
                ver -> ver.verifyAttribute(GROUPS_PROPERTIES + PROPERTY_DELMITER + RELATIVE_TO, ANY_STRING));
    }

    @Test
    public void propertiesRealmGroupsPropertiesRemove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        FormFragment form = page.getPropertiesRealmGroupsForm();
        table.bind(form);
        table.select(PROP_RLM_GP_DEL);
        page.getPropertiesRealmFormTabs().select(Ids.build(Ids.ELYTRON_PROPERTIES_REALM, GROUPS_PROPERTIES, TAB));
        crud.deleteSingleton(propertiesRealmAddress(PROP_RLM_GP_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(GROUPS_PROPERTIES));
    }

    @Test
    public void propertiesRealmUsersPropertiesUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, PROPERTIES_REALM_ITEM);
        TableFragment table = page.getPropertiesRealmTable();
        FormFragment form = page.getPropertiesRealmUsersForm();
        table.bind(form);
        table.select(PROP_RLM_UPDATE);
        page.getPropertiesRealmFormTabs().select(Ids.build(Ids.ELYTRON_PROPERTIES_REALM, USERS_PROPERTIES, TAB));
        crud.update(propertiesRealmAddress(PROP_RLM_UPDATE), form, f -> f.text(DIGEST_REALM_NAME, ANY_STRING),
                ver -> ver.verifyAttribute(USERS_PROPERTIES + PROPERTY_DELMITER + DIGEST_REALM_NAME, ANY_STRING));
    }

    // --------------- token-realm

    @Test
    public void tokenRealmCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        crud.create(tokenRealmAddress(TKN_RLM_CREATE), table, f -> f.text(NAME, TKN_RLM_CREATE));
    }

    @Test
    public void tokenRealmTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        // do not set the NAME attribute
        crud.createWithErrorAndCancelDialog(table, f -> { }, NAME);
    }

    @Test
    public void tokenRealmUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmForm();
        table.bind(form);
        table.select(TKN_RLM_UPDATE);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, ATTRIBUTES, TAB));
        crud.update(tokenRealmAddress(TKN_RLM_UPDATE), form, PRINCIPAL_CLAIM, ANY_STRING);
    }

    @Test
    public void tokenRealmDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        crud.delete(tokenRealmAddress(TKN_RLM_DELETE), table, TKN_RLM_DELETE);
    }

    @Test
    public void tokenRealmJwtAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmJWTForm();
        table.bind(form);
        table.select(TKN_RLM_JWT_CRT);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, JWT, TAB));
        form.emptyState().mainAction();
        console.verifySuccess();
        // the UI "add" operation adds a jwt with no inner attributes, as they are not required
        ModelNodeResult actualResult = operations.readAttribute(tokenRealmAddress(TKN_RLM_JWT_CRT), JWT);
        Assert.assertTrue("attribute jwt should exist", actualResult.value().isDefined());

    }

    @Test
    public void tokenRealmJwtUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmJWTForm();
        table.bind(form);
        table.select(TKN_RLM_JWT_UPD);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, JWT, TAB));
        crud.update(tokenRealmAddress(TKN_RLM_JWT_UPD), form, f -> f.text(PUBLIC_KEY, ANY_STRING),
                ver -> ver.verifyAttribute(JWT + PROPERTY_DELMITER + PUBLIC_KEY, ANY_STRING));
    }

    @Test
    public void tokenRealmJwtRemove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmJWTForm();
        table.bind(form);
        table.select(TKN_RLM_JWT_DEL);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, JWT, TAB));
        crud.deleteSingleton(tokenRealmAddress(TKN_RLM_JWT_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(JWT));
    }

    @Test
    public void tokenRealmOauth2Add() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmOAuth2Form();
        table.bind(form);
        table.select(TKN_RLM_OAU_CRT);
        String url = "http://" + ANY_STRING;
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, OAUTH2_INTROSPECTION, TAB));
        ModelNode oauth = new ModelNode();
        oauth.get(CLIENT_ID).set(ANY_STRING);
        oauth.get(CLIENT_SECRET).set(ANY_STRING);
        oauth.get(INTROSPECTION_URL).set(url);
        crud.createSingleton(tokenRealmAddress(TKN_RLM_OAU_CRT), form, f -> {
                    f.text(CLIENT_ID, ANY_STRING);
                    f.text(CLIENT_SECRET, ANY_STRING);
                    f.text(INTROSPECTION_URL, url);
                },
                ver -> ver.verifyAttribute(OAUTH2_INTROSPECTION, oauth));
    }

    @Test
    public void tokenRealmOauth2Update() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmOAuth2Form();
        table.bind(form);
        table.select(TKN_RLM_OAU_UPD);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, OAUTH2_INTROSPECTION, TAB));
        String secret = Random.name();
        crud.update(tokenRealmAddress(TKN_RLM_OAU_UPD), form, f -> f.text(CLIENT_SECRET, secret),
                ver -> ver.verifyAttribute(OAUTH2_INTROSPECTION + PROPERTY_DELMITER + CLIENT_SECRET, secret));
    }

    @Test
    public void tokenRealmOauth2Remove() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, TOKEN_REALM_ITEM);
        TableFragment table = page.getTokenRealmTable();
        FormFragment form = page.getTokenRealmOAuth2Form();
        table.bind(form);
        table.select(TKN_RLM_OAU_DEL);
        page.getTokenRealmFormTabs().select(Ids.build(Ids.ELYTRON_TOKEN_REALM, OAUTH2_INTROSPECTION, TAB));
        crud.deleteSingleton(tokenRealmAddress(TKN_RLM_OAU_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(OAUTH2_INTROSPECTION));
    }

    // --------------- constant-realm-mapper

    @Test
    public void constantRealmMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, CONSTANT_REALM_MAPPER_ITEM);
        TableFragment table = page.getConstantRealmMapperTable();
        crud.create(constantRealmMapperAddress(CON_RM_CREATE), table, f -> {
            f.text(NAME, CON_RM_CREATE);
            f.text(REALM_NAME, ANY_STRING);
        }, ResourceVerifier::verifyExists);
    }

    @Test
    public void constantRealmMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, CONSTANT_REALM_MAPPER_ITEM);
        TableFragment table = page.getConstantRealmMapperTable();
        FormFragment form = page.getConstantRealmMapperForm();
        table.bind(form);
        table.select(CON_RM_UPDATE);
        crud.update(constantRealmMapperAddress(CON_RM_UPDATE), form, REALM_NAME, Random.name());
    }

    @Test
    public void constantRealmMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, CONSTANT_REALM_MAPPER_ITEM);
        TableFragment table = page.getConstantRealmMapperTable();
        crud.delete(constantRealmMapperAddress(CON_RM_DELETE), table, CON_RM_DELETE);
    }

    // --------------- mapped-regex-realm-mapper

    @Test
    public void mappedRegexRealmMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, MAPPED_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getMappedRegexRealmMapperTable();
        crud.create(mappedRegexRealmMapperAddress(MAPP_RM_CREATE), table, f -> {
            f.text(NAME, MAPP_RM_CREATE);
            f.text(PATTERN, REGEX_PATTERN);
            f.list(REALM_MAP).add("a", "b");
        }, ResourceVerifier::verifyExists);
    }

    @Test
    public void mappedRegexRealmMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, MAPPED_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getMappedRegexRealmMapperTable();
        FormFragment form = page.getMappedRegexRealmMapperForm();
        table.bind(form);
        table.select(MAPP_RM_UPDATE);
        crud.update(mappedRegexRealmMapperAddress(MAPP_RM_UPDATE), form, DELEGATE_REALM_MAPPER, CON_RM_UPDATE);
    }

    @Test
    public void mappedRegexRealmMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, MAPPED_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getMappedRegexRealmMapperTable();
        crud.delete(mappedRegexRealmMapperAddress(MAPP_RM_DELETE), table, MAPP_RM_DELETE);
    }

    // --------------- simple-regex-realm-mapper

    @Test
    public void simpleRegexRealmMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, SIMPLE_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getSimpleRegexRealmMapperTable();
        crud.create(simpleRegexRealmMapperAddress(SIMP_RM_CREATE), table, f -> {
            f.text(NAME, SIMP_RM_CREATE);
            f.text(PATTERN, REGEX_PATTERN);
        }, ResourceVerifier::verifyExists);
    }

    @Test
    public void simpleRegexRealmMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, SIMPLE_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getSimpleRegexRealmMapperTable();
        FormFragment form = page.getSimpleRegexRealmMapperForm();
        table.bind(form);
        table.select(SIMP_RM_UPDATE);
        crud.update(simpleRegexRealmMapperAddress(SIMP_RM_UPDATE), form, DELEGATE_REALM_MAPPER, CON_RM_UPDATE);
    }

    @Test
    public void simpleRegexRealmMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(REALM_MAPPER_ITEM, SIMPLE_REGEX_REALM_MAPPER_ITEM);
        TableFragment table = page.getSimpleRegexRealmMapperTable();
        crud.delete(simpleRegexRealmMapperAddress(SIMP_RM_DELETE), table, SIMP_RM_DELETE);
    }

}
