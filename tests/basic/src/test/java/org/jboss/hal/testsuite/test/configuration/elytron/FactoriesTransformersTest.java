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

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class FactoriesTransformersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String anyString = Random.name();

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

        operations.add(securityDomainAddress(SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

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

        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));

        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE));
        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE));

        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), Values.of(CONSTANT, anyString));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE2), Values.of(CONSTANT, anyString));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE3), Values.of(CONSTANT, anyString));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), Values.of(CONSTANT, anyString));

        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE),
                Values.of(PATTERN, anyString).and(REPLACEMENT, anyString));
        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE),
                Values.of(PATTERN, anyString).and(REPLACEMENT, anyString));

        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE),
                Values.of(PATTERN, anyString));
        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE),
                Values.of(PATTERN, anyString));

        operations.add(kerberosSecurityFactoryAddress(KERB_UPDATE),
                Values.of(PATH, anyString).and(PRINCIPAL, anyString));
        operations.add(kerberosSecurityFactoryAddress(KERB_DELETE),
                Values.of(PATH, anyString).and(PRINCIPAL, anyString));
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
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- aggregate-http-server-mechanism-factory

    @Test
    public void aggregateHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crudOperations.create(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE), table, f -> {
            f.text(NAME, AGG_HTTP_CREATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE2);
        });
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crudOperations.createWithError(table, AGG_HTTP_CREATE, HTTP_SERVER_MECH_FACTORIES);
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
        crudOperations.update(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.list(HTTP_SERVER_MECH_FACTORIES).removeTags(),
                HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        crudOperations.delete(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), table, AGG_HTTP_DELETE);
    }

    // --------------- configurable-http-server-mechanism-factory

    @Test
    public void configurableHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crudOperations.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE), table, f -> {
            f.text(NAME, CONF_HTTP_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
        });
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crudOperations.createWithError(table, CONF_HTTP_CREATE, HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_UPDATE);
        crudOperations.update(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.clear(HTTP_SERVER_MECH_FACTORY),
                HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        crudOperations.delete(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE), table, CONF_HTTP_DELETE);
    }

    // --------------- http-authentication-factory

    @Test
    public void httpAuthenticationFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        crudOperations.create(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE), table, f -> {
            f.text(NAME, HTTP_AUTH_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void httpAuthenticationFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        crudOperations.createWithError(table, HTTP_AUTH_CREATE, HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void httpAuthenticationFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        FormFragment form = page.getHttpAuthenticationFactoryForm();
        table.bind(form);

        table.select(HTTP_AUTH_UPDATE);
        crudOperations.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE2);
    }

    @Test
    public void httpAuthenticationFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        FormFragment form = page.getHttpAuthenticationFactoryForm();
        table.bind(form);

        table.select(HTTP_AUTH_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(HTTP_SERVER_MECH_FACTORY),
                HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void httpAuthenticationFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        crudOperations.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE), table, HTTP_AUTH_DELETE);
    }

    // --------------- provider-http-server-mechanism-factory

    @Test
    public void providerHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();

        crudOperations.create(providerHttpServerMechanismFactoryAddress(PROV_HTTP_CREATE), table, PROV_HTTP_CREATE);
    }

    @Test
    public void providerHttpServerMechanismFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();
        FormFragment form = page.getProviderHttpServerMechanismForm();
        table.bind(form);

        table.select(PROV_HTTP_UPDATE);
        crudOperations.update(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE), form,
                PROVIDERS, PROV_LOAD_UPDATE);
    }

    @Test
    public void providerHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();
        crudOperations.delete(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE), table, PROV_HTTP_DELETE);
    }

    // --------------- service-loader-http-server-mechanism-factory

    @Test
    public void serviceLoaderHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderHttpServerMechanismTable();

        crudOperations.create(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_CREATE), table,
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
        crudOperations.update(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE), form,
                MODULE, anyString);
    }

    @Test
    public void serviceLoaderHttpServerMechanismFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderHttpServerMechanismTable();
        crudOperations.delete(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE), table,
                SVC_LOAD_HTTP_DELETE);
    }

    // --------------- aggregate-sasl-server-factory

    @Test
    public void aggregateSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        crudOperations.create(aggregateSaslServerFactoryAddress(AGG_SASL_CREATE), table, f -> {
            f.text(NAME, AGG_SASL_CREATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE2);
        });
    }

    @Test
    public void aggregateSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        crudOperations.createWithError(table, AGG_SASL_CREATE, SASL_SERVER_FACTORIES);
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
        crudOperations.update(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.list(SASL_SERVER_FACTORIES).removeTags(),
                SASL_SERVER_FACTORIES);
    }

    @Test
    public void aggregateSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();
        crudOperations.delete(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE), table, AGG_SASL_DELETE);
    }

    // --------------- configurable-sasl-server-factory

    @Test
    public void configurableSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();

        crudOperations.create(configurableSaslServerFactoryAddress(CONF_SASL_CREATE), table, f -> {
            f.text(NAME, CONF_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void configurableSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();

        crudOperations.createWithError(table, CONF_SASL_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void configurableSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        FormFragment form = page.getConfigurableSaslServerForm();
        table.bind(form);

        table.select(CONF_SASL_UPDATE);
        crudOperations.update(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void configurableSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        crudOperations.delete(configurableSaslServerFactoryAddress(CONF_SASL_DELETE), table, CONF_SASL_DELETE);
    }

    // --------------- mechanism-provider-filtering-sasl-server-factory

    @Test
    public void mechanismProviderFilteringSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        crudOperations.create(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_CREATE), table, f -> {
            f.text(NAME, MECH_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryTryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        crudOperations.createWithError(table, MECH_SASL_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        FormFragment form = page.getMechanismProviderFilteringSaslServerForm();
        table.bind(form);

        table.select(MECH_SASL_UPDATE);
        crudOperations.update(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        crudOperations.delete(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE), table,
                MECH_SASL_DELETE);
    }

    // --------------- provider-sasl-server-factory

    @Test
    public void providerSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();

        crudOperations.create(providerSaslServerFactoryAddress(PROV_SASL_CREATE), table, PROV_SASL_CREATE);
    }

    @Test
    public void providerSaslServerFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();
        FormFragment form = page.getProviderSaslServerForm();
        table.bind(form);

        table.select(PROV_SASL_UPDATE);
        crudOperations.update(providerSaslServerFactoryAddress(PROV_SASL_UPDATE), form,
                PROVIDERS, PROV_LOAD_UPDATE);
    }

    @Test
    public void providerSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();
        crudOperations.delete(providerSaslServerFactoryAddress(PROV_SASL_DELETE), table, PROV_SASL_DELETE);
    }

    // --------------- sasl-authentication-factory

    @Test
    public void saslAuthenticationFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        crudOperations.create(saslAuthenticationFactoryAddress(SASL_AUTH_CREATE), table, f -> {
            f.text(NAME, SASL_AUTH_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void saslAuthenticationFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        crudOperations.createWithError(table, SASL_AUTH_CREATE, SASL_SERVER_FACTORY);
    }

    @Test
    public void saslAuthenticationFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        FormFragment form = page.getSaslAuthenticationFactoryForm();
        table.bind(form);

        table.select(SASL_AUTH_UPDATE);
        crudOperations.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                SASL_SERVER_FACTORY, PROV_SASL_UPDATE2);
    }

    @Test
    public void saslAuthenticationFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        FormFragment form = page.getSaslAuthenticationFactoryForm();
        table.bind(form);

        table.select(SASL_AUTH_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SASL_SERVER_FACTORY),
                SASL_SERVER_FACTORY);
    }

    @Test
    public void saslAuthenticationFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        crudOperations.delete(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE), table, SASL_AUTH_DELETE);
    }

    // --------------- service-loader-http-server-factory

    @Test
    public void serviceLoaderSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();

        crudOperations.create(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_CREATE), table,
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
        crudOperations.update(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE), form,
                MODULE, anyString);
    }

    @Test
    public void serviceLoaderSaslServerFactoryDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();
        crudOperations.delete(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE), table,
                SVC_LOAD_SASL_DELETE);
    }

    // --------------- kerberos-security-factory

    @Test
    public void kerberosSecurityFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        crudOperations.create(kerberosSecurityFactoryAddress(KERB_CREATE), table, f -> {
            f.text(NAME, KERB_CREATE);
            f.text(PATH, anyString);
            f.text(PRINCIPAL, anyString);
        });
    }

    @Test
    public void kerberosSecurityFactoryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        crudOperations.createWithError(table, KERB_CREATE, PATH);
    }

    @Test
    public void kerberosSecurityFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_UPDATE);
        crudOperations.update(kerberosSecurityFactoryAddress(KERB_UPDATE), form, REQUIRED, true);
    }

    @Test
    public void kerberosSecurityFactoryTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void kerberosSecurityFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        crudOperations.delete(kerberosSecurityFactoryAddress(KERB_DELETE), table, KERB_DELETE);
    }

    // --------------- aggregate-principal-transformer

    @Test
    public void aggregatePrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        crudOperations.create(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, AGG_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2);
        });
    }

    @Test
    public void aggregatePrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        crudOperations.createWithError(table, AGG_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
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
        crudOperations.update(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void aggregatePrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        crudOperations.delete(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), table, AGG_PRI_TRANS_DELETE);
    }

    // --------------- chained-principal-transformer

    @Test
    public void chainedPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        crudOperations.create(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CHA_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2);
        });
    }

    @Test
    public void chainedPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        crudOperations.createWithError(table, CHA_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
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
        crudOperations.update(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), form,
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
        crudOperations.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void chainedPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        crudOperations.delete(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), table, CHA_PRI_TRANS_DELETE);
    }

    // --------------- constant-principal-transformer

    @Test
    public void constantPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        crudOperations.create(constantPrincipalTransformerAddress(CONS_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CONS_PRI_TRANS_CREATE);
            f.text(CONSTANT, anyString);
        });
    }

    @Test
    public void constantPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        crudOperations.createWithError(table, CONS_PRI_TRANS_CREATE, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crudOperations.update(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), form, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(CONSTANT), CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        crudOperations.delete(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), table, CONS_PRI_TRANS_DELETE);
    }


    // --------------- regex-principal-transformer

    @Test
    public void regexPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        crudOperations.create(regexPrincipalTransformerAddress(REG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REG_PRI_TRANS_CREATE);
            f.text(PATTERN, anyString);
            f.text(REPLACEMENT, anyString);
        });
    }

    @Test
    public void regexPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        crudOperations.createWithError(table, REG_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_UPDATE);
        crudOperations.update(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        crudOperations.delete(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE), table, REG_PRI_TRANS_DELETE);
    }

    // --------------- regex-validating-principal-transformer

    @Test
    public void regexValidatingPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        crudOperations.create(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REGV_PRI_TRANS_CREATE);
            f.text(PATTERN, anyString);
        });
    }

    @Test
    public void regexValidatingPrincipalTransformerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        crudOperations.createWithError(table, REGV_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_UPDATE);
        crudOperations.update(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        crudOperations.delete(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE), table, REGV_PRI_TRANS_DELETE);
    }


}
