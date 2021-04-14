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
package org.jboss.hal.testsuite.test.configuration.elytron.factoriestransformers;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.FILTERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_CONFIGURATIONS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_REALM_CONFIGURATIONS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN_FILTER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROTOCOL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.resources.Ids.ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.resources.Ids.ELYTRON_HTTP_AUTHENTICATION_FACTORY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_HTTP_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_HTTP_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_HTTP_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AGG_HTTP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONF_HTTP_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONF_HTTP_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONF_HTTP_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CONF_HTTP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTERS_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTERS_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTERS_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTERS_UPDATE2;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTHENTICATION_FACTORY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_AUTH_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_FACTORIES_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_SERVER_MECH_FACTORIES;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.HTTP_SERVER_MECH_FACTORY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_CONF_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_CONF_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_CONF_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_RE_CONF_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_RE_CONF_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_RE_CONF_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MECH_RE_CONF_UPDATE2;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROVIDERS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_UPDATE2;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_UPDATE3;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_HTTP_UPDATE4;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PROV_LOAD_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SEC_DOM_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SVC_LOAD_HTTP_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SVC_LOAD_HTTP_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SVC_LOAD_HTTP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.aggregateHttpServerMechanismFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.configurableHttpServerMechanismFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.httpAuthenticationFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.providerHttpServerMechanismFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.serviceLoaderHttpServerMechanismFactoryAddress;

@RunWith(Arquillian.class)
public class HttpFactoriesTest extends AbstractFactoriesTransformersTest {

    // --------------- aggregate-http-server-mechanism-factory

    @Test
    public void aggregateHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        console.waitNoNotification();
        crud.create(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE), table, f -> {
            f.text(NAME, AGG_HTTP_CREATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE2);
        });
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreate() {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, AGG_HTTP_CREATE, HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreateMin() {
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
        expected.add(PROV_HTTP_UPDATE).add(PROV_HTTP_UPDATE2).add(PROV_HTTP_UPDATE4);
        table.select(AGG_HTTP_UPDATE);
        crud.update(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), form,
                f -> f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE4),
                changes -> changes.verifyAttribute(HTTP_SERVER_MECH_FACTORIES, expected));
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryUpdate() {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        FormFragment form = page.getAggregateHttpServerMechanismForm();
        table.bind(form);

        table.select(AGG_HTTP_TRY_UPDATE);
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

        console.waitNoNotification();
        crud.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE), table, f -> {
            f.text(NAME, CONF_HTTP_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
        });
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryCreate() {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, CONF_HTTP_CREATE, HTTP_SERVER_MECH_FACTORY);
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
    public void configurableHttpServerMechanismFactoryTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_TRY_UPDATE);
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
        try {
            console.waitNoNotification();
        crud.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), filtersTable,
                    f -> f.text(PATTERN_FILTER, FILTERS_CREATE),
                    vc -> vc.verifyListAttributeContainsValue(FILTERS, FILTER_CREATE_MODEL));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY);
        }
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
        try {
            crud.update(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), form,
                    f -> f.text(PATTERN_FILTER, FILTERS_UPDATE2),
                    vc -> vc.verifyListAttributeContainsValue(FILTERS, FILTER_UPDATE2_MODEL));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY);
        }
    }

    @Test
    public void configurableHttpServerMechanismFactoryFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(HTTP_FACTORIES_ITEM, CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        TableFragment filtersTable = page.getConfigurableHttpServerMechanismFiltersTable();

        table.action(CONF_HTTP_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        try {
            crud.delete(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), filtersTable,
                    FILTERS_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainValue(FILTERS, FILTER_DELETE_MODEL));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY);
        }
    }

    // --------------- http-authentication-factory

    @Test
    public void httpAuthenticationFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        console.waitNoNotification();
        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE), table, f -> {
            f.text(NAME, HTTP_AUTH_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void httpAuthenticationFactoryTryCreate() {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, HTTP_AUTH_CREATE, HTTP_SERVER_MECH_FACTORY);
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
    public void httpAuthenticationFactoryTryUpdate() {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        FormFragment form = page.getHttpAuthenticationFactoryForm();
        table.bind(form);

        table.select(HTTP_AUTH_TRY_UPDATE);
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

        try {
            console.waitNoNotification();
        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismConf,
                    f -> f.text(MECHANISM_NAME, MECH_CONF_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                    f -> f.text(PROTOCOL, ANY_STRING),
                    vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, PROTOCOL, ANY_STRING));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
    }

    @Test
    public void httpAuthFacMechanismConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, HTTP_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getHttpAuthenticationFactoryTable();
        TableFragment mechanismConfTable = page.getHttpAuthFacMechanismConfigurationsTable();

        table.action(HTTP_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConfTable.getRoot()).is().visible();

        try {
            crud.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismConfTable,
                    MECH_CONF_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
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

        try {
            console.waitNoNotification();
        crud.create(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismRealmConf,
                    f -> f.text(REALM_NAME, MECH_RE_CONF_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.update(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), form,
                    f -> f.text(REALM_NAME, MECH_RE_CONF_UPDATE2),
                    vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_UPDATE2));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.delete(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), mechanismRealmConf,
                    MECH_RE_CONF_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_HTTP_AUTHENTICATION_FACTORY);
        }
    }

    // --------------- provider-http-server-mechanism-factory

    @Test
    public void providerHttpServerMechanismFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getProviderHttpServerMechanismTable();

        console.waitNoNotification();
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

        console.waitNoNotification();
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
}
