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
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.resources.Ids.ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY;
import static org.jboss.hal.resources.Ids.ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY;
import static org.jboss.hal.resources.Ids.ELYTRON_SASL_AUTHENTICATION_FACTORY;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class SaslFactoriesTest extends AbstractFactoriesTransformersTest {

    // --------------- aggregate-sasl-server-factory

    @Test
    public void aggregateSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        console.waitNoNotification();
        crud.create(aggregateSaslServerFactoryAddress(AGG_SASL_CREATE), table, f -> {
            f.text(NAME, AGG_SASL_CREATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE);
            f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE2);
        });
    }

    @Test
    public void aggregateSaslServerFactoryTryCreate() {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, AGGREGATE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getAggregateSaslServerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, AGG_SASL_CREATE, SASL_SERVER_FACTORIES);
    }

    @Test
    public void aggregateSaslServerFactoryTryCreateMin() {
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
        expected.add(PROV_SASL_UPDATE).add(PROV_SASL_UPDATE2).add(PROV_SASL_UPDATE4);
        table.select(AGG_SASL_UPDATE);
        crud.update(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE), form,
                f -> f.list(SASL_SERVER_FACTORIES).add(PROV_SASL_UPDATE4),
                changes -> changes.verifyAttribute(SASL_SERVER_FACTORIES, expected));
    }

    @Test
    public void aggregateSaslServerFactoryTryUpdate() {
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

        console.waitNoNotification();
        crud.create(configurableSaslServerFactoryAddress(CONF_SASL_CREATE), table, f -> {
            f.text(NAME, CONF_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void configurableSaslServerFactoryTryCreate() {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, CONF_SASL_CREATE, SASL_SERVER_FACTORY);
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
    public void configurableSaslServerFactoryTryUpdate() {
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

        try {
            console.waitNoNotification();
        crud.create(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersTable,
                    f -> f.text(PATTERN_FILTER, FILTERS_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PATTERN_FILTER, FILTERS_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY);
        }
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
        try {
            crud.update(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersForm,
                    f -> {
                        f.clear(PATTERN_FILTER);
                        f.select(PREDEFINED_FILTER, HASH_SHA);
                    },
                    vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PREDEFINED_FILTER, HASH_SHA));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY);
        }
    }

    @Test
    public void configurableSaslServerFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, CONFIGURABLE_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getConfigurableSaslServerTable();
        TableFragment filtersTable = page.getConfigurableSaslServerFiltersTable();

        table.action(CONF_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();
        try {
            crud.delete(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), filtersTable, FILTERS_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(FILTERS, PREDEFINED_FILTER, FILTERS_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY);
        }
    }


    // --------------- mechanism-provider-filtering-sasl-server-factory

    @Test
    public void mechanismProviderFilteringSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        console.waitNoNotification();
        crud.create(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_CREATE), table, f -> {
            f.text(NAME, MECH_SASL_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
        });
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryTryCreate() {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, MECH_SASL_CREATE, SASL_SERVER_FACTORY);
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
    public void mechanismProviderFilteringSaslServerFactoryTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        FormFragment form = page.getMechanismProviderFilteringSaslServerForm();
        table.bind(form);

        table.select(MECH_SASL_TRY_UPDATE);
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

        try {
            console.waitNoNotification();
        crud.create(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), filtersTable,
                    f -> f.text(PROVIDER_NAME, FILTERS_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, PROVIDER_NAME, FILTERS_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY);
        }
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

        try {
            crud.update(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), form,
                    f -> f.text(MECHANISM_NAME, ANY_STRING),
                    vc -> vc.verifyListAttributeContainsSingleValue(FILTERS, MECHANISM_NAME, ANY_STRING));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY);
        }
    }

    @Test
    public void mechanismProviderFilteringSaslServerFactoryFiltersDelete() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getMechanismProviderFilteringSaslServerTable();
        TableFragment filtersTable = page.getMechanismProviderFilteringSaslServerFiltersTable();

        table.action(MECH_SASL_UPDATE, Names.FILTERS);
        waitGui().until().element(filtersTable.getRoot()).is().visible();

        try {
            crud.delete(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), filtersTable, FILTERS_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(FILTERS, PROVIDER_NAME, FILTERS_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY);
        }
    }



    // --------------- provider-sasl-server-factory

    @Test
    public void providerSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, PROVIDER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getProviderSaslServerTable();

        console.waitNoNotification();
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

        console.waitNoNotification();
        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_CREATE), table, f -> {
            f.text(NAME, SASL_AUTH_CREATE);
            f.text(SASL_SERVER_FACTORY, PROV_SASL_UPDATE);
            f.text(SECURITY_DOMAIN, SEC_DOM_UPDATE);
        });
    }

    @Test
    public void saslAuthenticationFactoryTryCreate() {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, SASL_AUTH_CREATE, SASL_SERVER_FACTORY);
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
    public void saslAuthenticationFactoryTryUpdate() {
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

        try {
            console.waitNoNotification();
        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismConf,
                    f -> f.text(MECHANISM_NAME, MECH_CONF_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                    f -> f.text(PROTOCOL, ANY_STRING),
                    vc -> vc.verifyListAttributeContainsSingleValue(MECHANISM_CONFIGURATIONS, PROTOCOL, ANY_STRING));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
    }

    @Test
    public void saslAuthFacMechanismConfigurationsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SASL_FACTORIES_ITEM, SASL_AUTHENTICATION_FACTORY_ITEM);
        TableFragment table = page.getSaslAuthenticationFactoryTable();
        TableFragment mechanismConf = page.getSaslAuthFacMechanismConfigurationsTable();

        table.action(SASL_AUTH_UPDATE, Names.MECHANISM_CONFIGURATIONS);
        waitGui().until().element(mechanismConf.getRoot()).is().visible();

        try {
            crud.delete(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismConf,
                    MECH_CONF_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
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

        try {
            console.waitNoNotification();
        crud.create(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismRealmConf,
                    f -> f.text(REALM_NAME, MECH_RE_CONF_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_CREATE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.update(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), form,
                    f -> f.text(REALM_NAME, MECH_RE_CONF_UPDATE2),
                    vc -> vc.verifyListAttributeContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_UPDATE2));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
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

        try {
            crud.delete(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), mechanismRealmConf,
                    MECH_RE_CONF_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainsSingleValueOfList(MECHANISM_CONFIGURATIONS, MECHANISM_NAME,
                            MECH_CONF_UPDATE, MECHANISM_REALM_CONFIGURATIONS, REALM_NAME, MECH_RE_CONF_DELETE));
        } finally {
            // getting rid of action selection
            page.getBackToResourcePageViaBreadcrumb(ELYTRON_SASL_AUTHENTICATION_FACTORY);
        }
    }

    // --------------- service-loader-http-server-factory

    @Test
    public void serviceLoaderSaslServerFactoryCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(SASL_FACTORIES_ITEM, SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM);
        TableFragment table = page.getServiceLoaderSaslServerTable();

        console.waitNoNotification();
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
}
