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
package org.jboss.hal.testsuite.test.configuration.elytron;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronFactoriesTransformersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.PREDEFINED_FILTER;

@RunWith(Arquillian.class)
public class FactoriesTransformersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String ANY_STRING = Random.name();
    private static final ModelNode FILTER_CREATE_MODEL = new ModelNode();
    private static final ModelNode FILTER_UPDATE2_MODEL = new ModelNode();
    private static final ModelNode FILTER_DELETE_MODEL = new ModelNode();

    @BeforeClass
    public static void beforeTests() throws Exception {

        ModelNode httpServerFactoriesModel = new ModelNode();
        httpServerFactoriesModel.add(PROV_HTTP_UPDATE);
        httpServerFactoriesModel.add(PROV_HTTP_UPDATE2);
        Values HTTP_PARAMS = Values.of(HTTP_SERVER_MECH_FACTORIES, httpServerFactoriesModel);

        ModelNode saslServerFactoriesModel = new ModelNode();
        saslServerFactoriesModel.add(PROV_SASL_UPDATE);
        saslServerFactoriesModel.add(PROV_SASL_UPDATE2);
        Values SASL_PARAMS = Values.of(SASL_SERVER_FACTORIES, saslServerFactoriesModel);

        ModelNode principalTransformersModel = new ModelNode();
        principalTransformersModel.add(CONS_PRI_TRANS_UPDATE);
        principalTransformersModel.add(CONS_PRI_TRANS_UPDATE2);
        Values AGG_PRI_PARAMS = Values.of(PRINCIPAL_TRANSFORMERS, principalTransformersModel);

        // order is important as there are resources that should are used elsewhere
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), HTTP_PARAMS);
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), HTTP_PARAMS);
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));

        ModelNode FILTER_UPDATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_CREATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_UPDATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_UPDATE2_MODEL = new ModelNode();
        ModelNode MECH_CONF_DELETE_MODEL = new ModelNode();

        FILTER_CREATE_MODEL.get(PATTERN_FILTER).set(FILTERS_CREATE);
        FILTER_CREATE_MODEL.get(ENABLING).set(true);
        FILTER_UPDATE_MODEL.get(PATTERN_FILTER).set(FILTERS_UPDATE);
        FILTER_UPDATE_MODEL.get(ENABLING).set(true);
        FILTER_UPDATE2_MODEL.get(PATTERN_FILTER).set(FILTERS_UPDATE2);
        FILTER_UPDATE2_MODEL.get(ENABLING).set(true);
        FILTER_DELETE_MODEL.get(PATTERN_FILTER).set(FILTERS_DELETE);
        FILTER_DELETE_MODEL.get(ENABLING).set(true);

        operations.writeListAttribute(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), FILTERS,
                FILTER_UPDATE_MODEL, FILTER_DELETE_MODEL);

        operations.add(securityDomainAddress(SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

        ModelNode realmNameUpdate = new ModelNode();
        realmNameUpdate.get(REALM_NAME).set(MECH_RE_CONF_UPDATE);
        ModelNode realmNameDelete = new ModelNode();
        realmNameDelete.get(REALM_NAME).set(MECH_RE_CONF_DELETE);
        MECH_CONF_CREATE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_CREATE);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_UPDATE);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_REALM_CONFIGURATIONS).add(realmNameUpdate);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_REALM_CONFIGURATIONS).add(realmNameDelete);
        MECH_CONF_UPDATE2_MODEL.get(MECHANISM_NAME).set(MECH_CONF_UPDATE2);
        MECH_CONF_DELETE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_DELETE);

        operations.writeListAttribute(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), MECHANISM_CONFIGURATIONS,
                MECH_CONF_UPDATE_MODEL, MECH_CONF_DELETE_MODEL);

        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.add(providerLoaderAddress(PROV_LOAD_DELETE));

        operations.add(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE));
        operations.add(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE));

        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE2));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE3));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_DELETE));

        operations.add(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE), SASL_PARAMS);
        operations.add(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE), SASL_PARAMS);

        operations.add(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(configurableSaslServerFactoryAddress(CONF_SASL_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));

        operations.writeListAttribute(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), FILTERS,
                FILTER_UPDATE_MODEL, FILTER_DELETE_MODEL);

        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));

        ModelNode mechProvfilterUpdate = new ModelNode();
        mechProvfilterUpdate.get(PROVIDER_NAME).set(FILTERS_UPDATE);
        ModelNode mechProvfilterDelete = new ModelNode();
        mechProvfilterDelete.get(PROVIDER_NAME).set(FILTERS_DELETE);

        operations.writeListAttribute(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), FILTERS,
                mechProvfilterUpdate, mechProvfilterDelete);

        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

        operations.writeListAttribute(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), MECHANISM_CONFIGURATIONS,
                MECH_CONF_UPDATE_MODEL, MECH_CONF_DELETE_MODEL);

        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE));
        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE));

        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE2), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE3), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), Values.of(CONSTANT, ANY_STRING));

        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE),
                Values.of(PATTERN, ANY_STRING).and(REPLACEMENT, ANY_STRING));
        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE),
                Values.of(PATTERN, ANY_STRING).and(REPLACEMENT, ANY_STRING));

        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE),
                Values.of(PATTERN, ANY_STRING));
        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE),
                Values.of(PATTERN, ANY_STRING));

        operations.add(kerberosSecurityFactoryAddress(KERB_UPDATE),
                Values.of(PATH, ANY_STRING).and(PRINCIPAL, ANY_STRING));
        operations.add(kerberosSecurityFactoryAddress(KERB_DELETE),
                Values.of(PATH, ANY_STRING).and(PRINCIPAL, ANY_STRING));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE));
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE));
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE));
        operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE));
        operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE));
        operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE));
        operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE));
        operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE));
        operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_CREATE));

        operations.remove(filesystemRealmAddress(FILESYS_REALM_CREATE));

        operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE));
        operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE));
        operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_CREATE));

        operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_CREATE));
        operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE));
        operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE));

        operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE));
        operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_DELETE));
        operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_CREATE));

        operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_CREATE));
        operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE));
        operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE));

        operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE));
        operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE));
        operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_CREATE));

        operations.remove(kerberosSecurityFactoryAddress(KERB_UPDATE));
        operations.remove(kerberosSecurityFactoryAddress(KERB_DELETE));
        operations.remove(kerberosSecurityFactoryAddress(KERB_CREATE));

        operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE));
        operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE));
        operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_CREATE));

        operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE));
        operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE));
        operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_CREATE));

        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_CREATE));
        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE));
        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE2));
        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE3));
        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE));

        operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_CREATE));
        operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE));
        operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE));

        operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_CREATE));
        operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE));
        operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE));

        operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_CREATE));
        operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE));
        operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE));

        operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE));
        operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE2));
        operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE3));
        operations.remove(providerSaslServerFactoryAddress(PROV_SASL_DELETE));

        operations.remove(providerLoaderAddress(PROV_LOAD_CREATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_DELETE));
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE));

    }

    @Page private ElytronFactoriesTransformersPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- aggregate-http-server-mechanism-factory

    @Test
    public void aggregateHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crud.create(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE), table, f -> {
            f.text(NAME, AGG_HTTP_CREATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE2);
        });
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crud.createWithError(table, AGG_HTTP_CREATE, HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreateMin() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, AGG_HTTP_CREATE);
        dialog.getForm().list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE3);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void aggregateHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        FormFragment form = page.getAggregateHttpServerMechanismForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(PROV_HTTP_UPDATE).add(PROV_HTTP_UPDATE2).add(PROV_HTTP_UPDATE3);
        table.select(AGG_HTTP_UPDATE);
        crud.update(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), form,
                f -> f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE3),
                changes -> changes.verifyAttribute(HTTP_SERVER_MECH_FACTORIES, expected));
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        FormFragment form = page.getAggregateHttpServerMechanismForm();
        table.bind(form);

        table.select(AGG_HTTP_UPDATE);
        crud.updateWithError(form, f -> f.list(HTTP_SERVER_MECH_FACTORIES).removeTags(),
                HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        crud.delete(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), table, AGG_HTTP_DELETE);
    }

    // --------------- configurable-http-server-mechanism-factory

    @Test
    public void configurableHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crud.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE), table, f -> {
            f.text(NAME, CONF_HTTP_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
        });
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crud.createWithError(table, CONF_HTTP_CREATE, HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_UPDATE);
        crud.update(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), form,
                HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE2);
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_UPDATE);
        crud.updateWithError(form, f -> f.clear(HTTP_SERVER_MECH_FACTORY),
                HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        crud.delete(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE), table, CONF_HTTP_DELETE);
    }

    @Test
    public void configurableHttpServerMechanismFactoryFiltersCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        TableFragment filtersTable = page.getConfigurableHttpServerMechanismFiltersTable();

        table.action(CONF_HTTP_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        crud.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), filtersTable,
                f -> f.text(PATTERN_FILTER, FILTERS_CREATE),
                vc -> vc.verifyListAttributeContainsValue(FILTERS, FILTER_CREATE_MODEL));
    }

    @Test
    public void configurableHttpServerMechanismFactoryFiltersUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        TableFragment filtersTable = page.getConfigurableHttpServerMechanismFiltersTable();

        table.action(CONF_HTTP_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        FormFragment form = page.getConfigurableHttpServerMechanismFiltersForm();
        filtersTable.bind(form);
        filtersTable.select(FILTERS_UPDATE);
        crud.update(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), form,
                f -> f.text(PATTERN_FILTER, FILTERS_UPDATE2),
                vc -> vc.verifyListAttributeContainsValue(FILTERS, FILTER_UPDATE2_MODEL));
    }

    @Test
    public void configurableHttpServerMechanismFactoryFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        TableFragment filtersTable = page.getConfigurableHttpServerMechanismFiltersTable();

        table.action(CONF_HTTP_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        crud.delete(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), filtersTable,
                FILTERS_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainValue(FILTERS, FILTER_DELETE_MODEL));
    }

    // --------------- http-authentication-factory

    @Test
    public void httpAuthenticationFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE), table, f -> {
            f.text(NAME, HTTP_AUTH_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void httpAuthenticationFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        crud.createWithError(table, HTTP_AUTH_CREATE, HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void httpAuthenticationFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        FormFragment form = page.getHttpAuthenticationFactoryForm();
        table.bind(form);

        table.select(HTTP_AUTH_UPDATE);
        crud.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE2);
    }

    @Test
    public void httpAuthenticationFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        FormFragment form = page.getHttpAuthenticationFactoryForm();
        table.bind(form);

        table.select(HTTP_AUTH_UPDATE);
        crud.updateWithError(form, f -> f.clear(HTTP_SERVER_MECH_FACTORY),
                HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void httpAuthenticationFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        crud.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE), table, HTTP_AUTH_DELETE);
    }

    @Test
    public void httpAuthFacMechanismConfigurationsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getHttpAuthFacMechanismConfigurationsTable();

        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismConf,
                f -> f.text(MECHANISM_NAME, MECH_CONF_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_CREATE));
    }

    @Test
    public void httpAuthFacMechanismConfigurationsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        TableFragment mechanismConfTable = page.getHttpAuthFacMechanismConfigurationsTable();

        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConfTable.getRoot()).is().visible();

        FormFragment form = page.getHttpAuthFacMechanismConfigurationsForm();
        mechanismConfTable.bind(form);
        mechanismConfTable.select(MECH_CONF_UPDATE);

        crud.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                f -> f.text(PROTOCOL, ANY_STRING),
                vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, PROTOCOL, ANY_STRING));
    }

    @Test
    public void httpAuthFacMechanismConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        TableFragment mechanismConfTable = page.getHttpAuthFacMechanismConfigurationsTable();

        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConfTable.getRoot()).is().visible();

        crud.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismConfTable,
                MECH_CONF_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_DELETE));
    }

    @Test
    public void httpAuthFacMechanismRealmConfigurationsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        TableFragment mechanismConf = page.getHttpAuthFacMechanismConfigurationsTable();
        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getHttpAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismRealmConf,
                f -> f.text(REALM_NAME, MECH_RE_CONF_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_CREATE));
    }

    @Test
    public void httpAuthFacMechanismRealmConfigurationsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        TableFragment mechanismConf = page.getHttpAuthFacMechanismConfigurationsTable();
        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getHttpAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        FormFragment form = page.getHttpAuthFacMechanismRealmConfigurationsForm();
        mechanismRealmConf.bind(form);
        mechanismRealmConf.select(MECH_RE_CONF_UPDATE);

        crud.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                f -> f.text(REALM_NAME, MECH_RE_CONF_UPDATE2),
                vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_UPDATE2));
    }

    @Test
    public void httpAuthFacMechanismRealmConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        TableFragment mechanismConf = page.getHttpAuthFacMechanismConfigurationsTable();
        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getHttpAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        crud.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismRealmConf,
                MECH_RE_CONF_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_DELETE));
    }

    // --------------- provider-http-server-mechanism-factory

    @Test
    public void providerHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();

        crud.create(providerHttpServerMechanismFactoryAddress(PROV_HTTP_CREATE), table, PROV_HTTP_CREATE);
    }

    @Test
    public void providerHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();
        FormFragment form = page.getProviderHttpServerMechanismForm();
        table.bind(form);

        table.select(PROV_HTTP_UPDATE);
        crud.update(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE), form,
                PROVIDERS, PROV_LOAD_UPDATE);
    }

    @Test
    public void providerHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();
        crud.delete(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE), table, PROV_HTTP_DELETE);
    }

    // --------------- service-loader-http-server-mechanism-factory

    @Test
    public void serviceLoaderHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderHttpServerMechanismTable();

        crud.create(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_CREATE), table,
                SVC_LOAD_HTTP_CREATE);
    }

    @Test
    public void serviceLoaderHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderHttpServerMechanismTable();
        FormFragment form = page.getServiceLoaderHttpServerMechanismForm();
        table.bind(form);

        table.select(SVC_LOAD_HTTP_UPDATE);
        crud.update(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE), form,
                MODULE, ANY_STRING);
    }

    @Test
    public void serviceLoaderHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderHttpServerMechanismTable();
        crud.delete(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE), table,
                SVC_LOAD_HTTP_DELETE);
    }

    // --------------- aggregate-sasl-server-factory

    @Test
    public void aggregateSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        crud.create(aggregateSaslServerFactoryAddress(AGG_SASL_CREATE), table, f -> {
            f.text(NAME, AGG_SASL_CREATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE2);
        });
    }

    @Test
    public void aggregateSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        crud.createWithError(table, AGG_SASL_CREATE, SASL_SERVER_FACTORIES);
    }

    @Test
    public void aggregateSaslServerFactoryTryCreateMin() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, AGG_SASL_CREATE);
        dialog.getForm().list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE3);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void aggregateSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();
        FormFragment form = page.getAggregateSaslServerForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(PROV_SASL_UPDATE).add(PROV_SASL_UPDATE2).add(PROV_SASL_UPDATE3);
        table.select(AGG_SASL_UPDATE);
        crud.update(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE), form,
                f -> f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE3),
                changes -> changes.verifyAttribute(SASL_SERVER_FACTORIES, expected));
    }

    @Test
    public void aggregateSaslServerFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();
        FormFragment form = page.getAggregateSaslServerForm();
        table.bind(form);

        table.select(AGG_SASL_UPDATE);
        crud.updateWithError(form, f -> f.list(SASL_SERVER_FACTORIES).removeTags(),
                SASL_SERVER_FACTORIES);
    }

    @Test
    public void aggregateSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();
        crud.delete(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE), table, AGG_SASL_DELETE);
    }

    // --------------- configurable-sasl-server-factory

    @Test
    public void configurableSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();

        crud.create(configurableSaslServerFactoryAddress(CONF_SASL_CREATE), table, f -> {
            f.text(NAME, CONF_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void configurableSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();

        crud.createWithError(table, CONF_SASL_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void configurableSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        FormFragment form = page.getConfigurableSaslServerForm();
        table.bind(form);

        table.select(CONF_SASL_UPDATE);
        crud.update(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), form,
                SASL_SERVER_FACTORY, PROV_SASL_UPDATE2);
    }

    @Test
    public void configurableSaslServerFactoryTryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        FormFragment form = page.getConfigurableSaslServerForm();
        table.bind(form);

        table.select(CONF_SASL_UPDATE);
        crud.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void configurableSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        crud.delete(configurableSaslServerFactoryAddress(CONF_SASL_DELETE), table, CONF_SASL_DELETE);
    }

    @Test
    public void configurableSaslServerFiltersCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        TableFragment filtersTable = page.getConfigurableSaslServerFiltersTable();

        table.action(CONF_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        crud.create(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersTable,
                f -> f.text(PATTERN_FILTER, FILTERS_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PATTERN_FILTER, FILTERS_CREATE));
    }

    @Test
    public void configurableSaslServerFiltersUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        TableFragment filtersTable = page.getConfigurableSaslServerFiltersTable();

        table.action(CONF_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();
        FormFragment filtersForm = page.getConfigurableSaslServerFiltersForm();
        filtersTable.bind(filtersForm);
        filtersTable.select(FILTERS_UPDATE);
        crud.update(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersForm,
                f -> {
                    f.clear(PATTERN_FILTER);
                    f.select(PREDEFINED_FILTER, HASH_SHA);
                },
                vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PREDEFINED_FILTER, HASH_SHA));
    }

    @Test
    public void configurableSaslServerFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        TableFragment filtersTable = page.getConfigurableSaslServerFiltersTable();

        table.action(CONF_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();
        crud.delete(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersTable, FILTERS_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainSingleValue(FILTERS, PREDEFINED_FILTER, FILTERS_DELETE));
    }


    // --------------- mechanism-provider-filtering-sasl-server-factory

    @Test
    public void mechanismProviderFilteringSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        crud.create(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_CREATE), table, f -> {
            f.text(NAME, MECH_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        crud.createWithError(table, MECH_SASL_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        FormFragment form = page.getMechanismProviderFilteringSaslServerForm();
        table.bind(form);

        table.select(MECH_SASL_UPDATE);
        crud.update(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), form,
                SASL_SERVER_FACTORY, PROV_SASL_UPDATE2);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryTryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        FormFragment form = page.getMechanismProviderFilteringSaslServerForm();
        table.bind(form);

        table.select(MECH_SASL_UPDATE);
        crud.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        crud.delete(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE), table,
                MECH_SASL_DELETE);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryFiltersCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        TableFragment filtersTable = page.getMechanismProviderFilteringSaslServerFiltersTable();

        table.action(MECH_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        crud.create(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), filtersTable,
                f -> f.text(PROVIDER_NAME, FILTERS_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PROVIDER_NAME, FILTERS_CREATE));
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryFiltersUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        TableFragment filtersTable = page.getMechanismProviderFilteringSaslServerFiltersTable();

        table.action(MECH_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        FormFragment form = page.getMechanismProviderFilteringSaslServerFiltersForm();
        filtersTable.bind(form);
        filtersTable.select(FILTERS_UPDATE);

        crud.update(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), form,
                f -> f.text(MECHANISM_NAME, ANY_STRING),
                vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, MECHANISM_NAME, ANY_STRING));
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        TableFragment filtersTable = page.getMechanismProviderFilteringSaslServerFiltersTable();

        table.action(MECH_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        crud.delete(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), filtersTable, FILTERS_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainSingleValue(FILTERS, PROVIDER_NAME, FILTERS_DELETE));
    }



    // --------------- provider-sasl-server-factory

    @Test
    public void providerSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();

        crud.create(providerSaslServerFactoryAddress(PROV_SASL_CREATE), table, PROV_SASL_CREATE);
    }

    @Test
    public void providerSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();
        FormFragment form = page.getProviderSaslServerForm();
        table.bind(form);

        table.select(PROV_SASL_UPDATE);
        crud.update(providerSaslServerFactoryAddress(PROV_SASL_UPDATE), form,
                PROVIDERS, PROV_LOAD_UPDATE);
    }

    @Test
    public void providerSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();
        crud.delete(providerSaslServerFactoryAddress(PROV_SASL_DELETE), table, PROV_SASL_DELETE);
    }

    // --------------- sasl-authentication-factory

    @Test
    public void saslAuthenticationFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_CREATE), table, f -> {
            f.text(NAME, SASL_AUTH_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void saslAuthenticationFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        crud.createWithError(table, SASL_AUTH_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void saslAuthenticationFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        FormFragment form = page.getSaslAuthenticationFactoryForm();
        table.bind(form);

        table.select(SASL_AUTH_UPDATE);
        crud.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                SASL_SERVER_FACTORY, PROV_SASL_UPDATE2);
    }

    @Test
    public void saslAuthenticationFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        FormFragment form = page.getSaslAuthenticationFactoryForm();
        table.bind(form);

        table.select(SASL_AUTH_UPDATE);
        crud.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void saslAuthenticationFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        crud.delete(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE), table, SASL_AUTH_DELETE);
    }

    @Test
    public void saslAuthFacMechanismConfigurationsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();

        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismConf,
                f -> f.text(MECHANISM_NAME, MECH_CONF_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_CREATE));
    }

    @Test
    public void saslAuthFacMechanismConfigurationsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();

        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        FormFragment form = page.getSaslAuthFacMechanismConfigurationsForm();
        mechanismConf.bind(form);
        mechanismConf.select(MECH_CONF_UPDATE);

        crud.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                f -> f.text(PROTOCOL, ANY_STRING),
                vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, PROTOCOL, ANY_STRING));
    }

    @Test
    public void saslAuthFacMechanismConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();

        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        crud.delete(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismConf,
                MECH_CONF_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_DELETE));
    }

    @Test
    public void saslAuthFacMechanismRealmConfigurationsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();

        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getSaslAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismRealmConf,
                f -> f.text(REALM_NAME, MECH_RE_CONF_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_CREATE));
    }

    @Test
    public void saslAuthFacMechanismRealmConfigurationsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();
        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getSaslAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        FormFragment form = page.getSaslAuthFacMechanismRealmConfigurationsForm();
        mechanismRealmConf.bind(form);
        mechanismRealmConf.select(MECH_RE_CONF_UPDATE);

        crud.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                f -> f.text(REALM_NAME, MECH_RE_CONF_UPDATE2),
                vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_UPDATE2));
    }

    @Test
    public void saslAuthFacMechanismRealmConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();
        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        TableFragment mechanismRealmConf = page.getSaslAuthFacMechanismRealmConfigurationsTable();
        mechanismConf.action(MECH_CONF_UPDATE, Names.MECHANISM_REALM_CONFIGURATIONS);
        waitGui().until().element(mechanismRealmConf.getRoot()).is().visible();

        crud.delete(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismRealmConf,
                MECH_RE_CONF_DELETE,
                vc -> vc.verifyListAttributeDoesNotContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                        MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_DELETE));
    }

    // --------------- service-loader-http-server-factory

    @Test
    public void serviceLoaderSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();

        crud.create(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_CREATE), table,
                SVC_LOAD_SASL_CREATE);
    }

    @Test
    public void serviceLoaderSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();
        FormFragment form = page.getServiceLoaderSaslServerForm();
        table.bind(form);

        table.select(SVC_LOAD_SASL_UPDATE);
        crud.update(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE), form,
                MODULE, ANY_STRING);
    }

    @Test
    public void serviceLoaderSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();
        crud.delete(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE), table,
                SVC_LOAD_SASL_DELETE);
    }

    // --------------- kerberos-security-factory

    @Test
    public void kerberosSecurityFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        crud.create(kerberosSecurityFactoryAddress(KERB_CREATE), table, f -> {
            f.text(NAME, KERB_CREATE);
            f.text(PATH, ANY_STRING);
            f.text(PRINCIPAL, ANY_STRING);
        });
    }

    @Test
    public void kerberosSecurityFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        crud.createWithError(table, KERB_CREATE, PATH);
    }

    @Test
    public void kerberosSecurityFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_UPDATE);
        crud.update(kerberosSecurityFactoryAddress(KERB_UPDATE), form, REQUIRED, true);
    }

    @Test
    public void kerberosSecurityFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void kerberosSecurityFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        crud.delete(kerberosSecurityFactoryAddress(KERB_DELETE), table, KERB_DELETE);
    }

    // --------------- aggregate-principal-transformer

    @Test
    public void aggregatePrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        crud.create(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, AGG_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2);
        });
    }

    @Test
    public void aggregatePrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        crud.createWithError(table, AGG_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void aggregatePrincipalTransformerTryCreateMin() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, AGG_PRI_TRANS_CREATE);
        dialog.getForm().list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void aggregatePrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        FormFragment form = page.getAggregatePrincipalTransformerForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2).add(CONS_PRI_TRANS_UPDATE3);
        table.select(AGG_PRI_TRANS_UPDATE);
        crud.update(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), form,
                f -> f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE3),
                changes -> changes.verifyAttribute(PRINCIPAL_TRANSFORMERS, expected));
    }

    @Test
    public void aggregatePrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        FormFragment form = page.getAggregatePrincipalTransformerForm();
        table.bind(form);

        table.select(AGG_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void aggregatePrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        crud.delete(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), table, AGG_PRI_TRANS_DELETE);
    }

    // --------------- chained-principal-transformer

    @Test
    public void chainedPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        crud.create(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CHA_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2);
        });
    }

    @Test
    public void chainedPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        crud.createWithError(table, CHA_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void chainedPrincipalTransformerTryCreateMin() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, CHA_PRI_TRANS_CREATE);
        dialog.getForm().list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void chainedPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        FormFragment form = page.getChainedPrincipalTransformerForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2).add(CONS_PRI_TRANS_UPDATE3);
        table.select(CHA_PRI_TRANS_UPDATE);
        crud.update(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), form,
                f -> f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE3),
                changes -> changes.verifyAttribute(PRINCIPAL_TRANSFORMERS, expected));
    }

    @Test
    public void chainedPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        FormFragment form = page.getChainedPrincipalTransformerForm();
        table.bind(form);

        table.select(CHA_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void chainedPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        crud.delete(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), table, CHA_PRI_TRANS_DELETE);
    }

    // --------------- constant-principal-transformer

    @Test
    public void constantPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        crud.create(constantPrincipalTransformerAddress(CONS_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CONS_PRI_TRANS_CREATE);
            f.text(CONSTANT, ANY_STRING);
        });
    }

    @Test
    public void constantPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        crud.createWithError(table, CONS_PRI_TRANS_CREATE, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crud.update(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), form, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.clear(CONSTANT), CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        crud.delete(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), table, CONS_PRI_TRANS_DELETE);
    }


    // --------------- regex-principal-transformer

    @Test
    public void regexPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        crud.create(regexPrincipalTransformerAddress(REG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REG_PRI_TRANS_CREATE);
            f.text(PATTERN, ANY_STRING);
            f.text(REPLACEMENT, ANY_STRING);
        });
    }

    @Test
    public void regexPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        crud.createWithError(table, REG_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_UPDATE);
        crud.update(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        crud.delete(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE), table, REG_PRI_TRANS_DELETE);
    }

    // --------------- regex-validating-principal-transformer

    @Test
    public void regexValidatingPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        crud.create(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REGV_PRI_TRANS_CREATE);
            f.text(PATTERN, ANY_STRING);
        });
    }

    @Test
    public void regexValidatingPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        crud.createWithError(table, REGV_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_UPDATE);
        crud.update(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        crud.delete(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE), table,
                REGV_PRI_TRANS_DELETE);
    }


}
