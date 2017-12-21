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

package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.FILTERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_CONFIGURATIONS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_REALM_CONFIGURATIONS;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_FACTORIES_TRANSFORMERS)
public class ElytronFactoriesTransformersPage extends BasePage {

    // HTTP Factories
    @FindBy(id = ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment aggregateHttpServerMechanismTable;
    @FindBy(id = ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY + "-" + FORM) private FormFragment aggregateHttpServerMechanismForm;
    @FindBy(id = ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment configurableHttpServerMechanismTable;
    @FindBy(id = ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY + "-" + FORM) private FormFragment configurableHttpServerMechanismForm;
    @FindBy(id = ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY + "-" + FILTERS + "-" + TABLE +  WRAPPER) private TableFragment configurableHttpServerMechanismFiltersTable;
    @FindBy(id = ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY + "-" + FILTERS + "-" + FORM) private FormFragment configurableHttpServerMechanismFiltersForm;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment httpAuthenticationFactoryTable;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + FORM) private FormFragment httpAuthenticationFactoryForm;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + MECHANISM_CONFIGURATIONS + "-" + TABLE +  WRAPPER) private TableFragment httpAuthFacMechanismConfigurationsTable;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + MECHANISM_CONFIGURATIONS + "-" + FORM) private FormFragment httpAuthFacMechanismConfigurationsForm;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + MECHANISM_REALM_CONFIGURATIONS + "-" + TABLE +  WRAPPER) private TableFragment httpAuthFacMechanismRealmConfigurationsTable;
    @FindBy(id = ELYTRON_HTTP_AUTHENTICATION_FACTORY + "-" + MECHANISM_REALM_CONFIGURATIONS + "-" + FORM) private FormFragment httpAuthFacMechanismRealmConfigurationsForm;
    @FindBy(id = ELYTRON_PROVIDER_HTTP_SERVER_MECHANISM_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment providerHttpServerMechanismTable;
    @FindBy(id = ELYTRON_PROVIDER_HTTP_SERVER_MECHANISM_FACTORY + "-" + FORM) private FormFragment providerHttpServerMechanismForm;
    @FindBy(id = ELYTRON_SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment serviceLoaderHttpServerMechanismTable;
    @FindBy(id = ELYTRON_SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY + "-" + FORM) private FormFragment serviceLoaderHttpServerMechanismForm;

    // SASL Factories
    @FindBy(id = ELYTRON_AGGREGATE_SASL_SERVER_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment aggregateSaslServerTable;
    @FindBy(id = ELYTRON_AGGREGATE_SASL_SERVER_FACTORY + "-" + FORM) private FormFragment aggregateSaslServerForm;
    @FindBy(id = ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment configurableSaslServerTable;
    @FindBy(id = ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY + "-" + FORM) private FormFragment configurableSaslServerForm;
    @FindBy(id = ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY + "-" + FILTERS + "-" + TABLE +  WRAPPER) private TableFragment configurableSaslServerFiltersTable;
    @FindBy(id = ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY + "-" + FILTERS + "-" + FORM) private FormFragment configurableSaslServerFiltersForm;
    @FindBy(id = ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment mechanismProviderFilteringSaslServerTable;
    @FindBy(id = ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY + "-" + FORM) private FormFragment mechanismProviderFilteringSaslServerForm;
    @FindBy(id = ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY + "-" + FILTERS + "-" + TABLE +  WRAPPER) private TableFragment mechanismProviderFilteringSaslServerFiltersTable;
    @FindBy(id = ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY + "-" + FILTERS + "-" + FORM) private FormFragment mechanismProviderFilteringSaslServerFiltersForm;
    @FindBy(id = ELYTRON_PROVIDER_SASL_SERVER_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment providerSaslServerTable;
    @FindBy(id = ELYTRON_PROVIDER_SASL_SERVER_FACTORY + "-" + FORM) private FormFragment providerSaslServerForm;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment saslAuthenticationFactoryTable;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + FORM) private FormFragment saslAuthenticationFactoryForm;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + MECHANISM_CONFIGURATIONS + "-" + TABLE +  WRAPPER) private TableFragment saslAutFacMechanismConfigurationsTable;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + MECHANISM_CONFIGURATIONS + "-" + FORM) private FormFragment saslAutFacMechanismConfigurationsForm;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + MECHANISM_REALM_CONFIGURATIONS + "-" + TABLE +  WRAPPER) private TableFragment saslAuthFacMechanismRealmConfigurationsTable;
    @FindBy(id = ELYTRON_SASL_AUTHENTICATION_FACTORY + "-" + MECHANISM_REALM_CONFIGURATIONS + "-" + FORM) private FormFragment saslAuthFacMechanismRealmConfigurationsForm;
    @FindBy(id = ELYTRON_SERVICE_LOADER_SASL_SERVER_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment serviceLoaderSaslServerTable;
    @FindBy(id = ELYTRON_SERVICE_LOADER_SASL_SERVER_FACTORY + "-" + FORM) private FormFragment serviceLoaderSaslServerForm;

    // Other Factories
    @FindBy(id = ELYTRON_KERBEROS_SECURITY_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment kerberosSecurityTable;
    @FindBy(id = ELYTRON_KERBEROS_SECURITY_FACTORY + "-" + FORM) private FormFragment kerberosSecurityForm;
    @FindBy(id = ELYTRON_CUSTOM_CREDENTIAL_SECURITY_FACTORY + "-" + TABLE +  WRAPPER) private TableFragment customCredentialSecurityTable;
    @FindBy(id = ELYTRON_CUSTOM_CREDENTIAL_SECURITY_FACTORY + "-" + FORM) private FormFragment customCredentialSecurityForm;

    // Principal Transformers
    @FindBy(id = ELYTRON_AGGREGATE_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment aggregatePrincipalTransformerTable;
    @FindBy(id = ELYTRON_AGGREGATE_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment aggregatePrincipalTransformerForm;
    @FindBy(id = ELYTRON_CHAINED_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment chainedPrincipalTransformerTable;
    @FindBy(id = ELYTRON_CHAINED_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment chainedPrincipalTransformerForm;
    @FindBy(id = ELYTRON_CONSTANT_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment constantPrincipalTransformerTable;
    @FindBy(id = ELYTRON_CONSTANT_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment constantPrincipalTransformerForm;
    @FindBy(id = ELYTRON_CUSTOM_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment customPrincipalTransformerTable;
    @FindBy(id = ELYTRON_CUSTOM_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment customPrincipalTransformerForm;
    @FindBy(id = ELYTRON_REGEX_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment regexPrincipalTransformerTable;
    @FindBy(id = ELYTRON_REGEX_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment regexPrincipalTransformerForm;
    @FindBy(id = ELYTRON_REGEX_VALIDATING_PRINCIPAL_TRANSFORMER + "-" + TABLE +  WRAPPER) private TableFragment regexValidatingPrincipalTransformerTable;
    @FindBy(id = ELYTRON_REGEX_VALIDATING_PRINCIPAL_TRANSFORMER + "-" + FORM) private FormFragment regexValidatingPrincipalTransformerForm;

    public TableFragment getAggregateHttpServerMechanismTable() {
        return aggregateHttpServerMechanismTable;
    }

    public FormFragment getAggregateHttpServerMechanismForm() {
        return aggregateHttpServerMechanismForm;
    }

    public TableFragment getConfigurableHttpServerMechanismTable() {
        return configurableHttpServerMechanismTable;
    }

    public FormFragment getConfigurableHttpServerMechanismForm() {
        return configurableHttpServerMechanismForm;
    }

    public TableFragment getConfigurableHttpServerMechanismFiltersTable() {
        return configurableHttpServerMechanismFiltersTable;
    }

    public FormFragment getConfigurableHttpServerMechanismFiltersForm() {
        return configurableHttpServerMechanismFiltersForm;
    }

    public TableFragment getHttpAuthenticationFactoryTable() {
        return httpAuthenticationFactoryTable;
    }

    public FormFragment getHttpAuthenticationFactoryForm() {
        return httpAuthenticationFactoryForm;
    }

    public TableFragment getHttpAuthFacMechanismConfigurationsTable() {
        return httpAuthFacMechanismConfigurationsTable;
    }

    public FormFragment getHttpAuthFacMechanismConfigurationsForm() {
        return httpAuthFacMechanismConfigurationsForm;
    }

    public TableFragment getHttpAuthFacMechanismRealmConfigurationsTable() {
        return httpAuthFacMechanismRealmConfigurationsTable;
    }

    public FormFragment getHttpAuthFacMechanismRealmConfigurationsForm() {
        return httpAuthFacMechanismRealmConfigurationsForm;
    }

    public TableFragment getProviderHttpServerMechanismTable() {
        return providerHttpServerMechanismTable;
    }

    public FormFragment getProviderHttpServerMechanismForm() {
        return providerHttpServerMechanismForm;
    }

    public TableFragment getServiceLoaderHttpServerMechanismTable() {
        return serviceLoaderHttpServerMechanismTable;
    }

    public FormFragment getServiceLoaderHttpServerMechanismForm() {
        return serviceLoaderHttpServerMechanismForm;
    }

    public TableFragment getAggregateSaslServerTable() {
        return aggregateSaslServerTable;
    }

    public FormFragment getAggregateSaslServerForm() {
        return aggregateSaslServerForm;
    }

    public TableFragment getConfigurableSaslServerTable() {
        return configurableSaslServerTable;
    }

    public FormFragment getConfigurableSaslServerForm() {
        return configurableSaslServerForm;
    }

    public TableFragment getConfigurableSaslServerFiltersTable() {
        return configurableSaslServerFiltersTable;
    }

    public FormFragment getConfigurableSaslServerFiltersForm() {
        return configurableSaslServerFiltersForm;
    }

    public TableFragment getMechanismProviderFilteringSaslServerTable() {
        return mechanismProviderFilteringSaslServerTable;
    }

    public FormFragment getMechanismProviderFilteringSaslServerForm() {
        return mechanismProviderFilteringSaslServerForm;
    }

    public TableFragment getMechanismProviderFilteringSaslServerFiltersTable() {
        return mechanismProviderFilteringSaslServerFiltersTable;
    }

    public FormFragment getMechanismProviderFilteringSaslServerFiltersForm() {
        return mechanismProviderFilteringSaslServerFiltersForm;
    }

    public TableFragment getProviderSaslServerTable() {
        return providerSaslServerTable;
    }

    public FormFragment getProviderSaslServerForm() {
        return providerSaslServerForm;
    }

    public TableFragment getSaslAuthenticationFactoryTable() {
        return saslAuthenticationFactoryTable;
    }

    public FormFragment getSaslAuthenticationFactoryForm() {
        return saslAuthenticationFactoryForm;
    }

    public TableFragment getSaslAuthFacMechanismConfigurationsTable() {
        return saslAutFacMechanismConfigurationsTable;
    }

    public FormFragment getSaslAuthFacMechanismConfigurationsForm() {
        return saslAutFacMechanismConfigurationsForm;
    }

    public TableFragment getSaslAuthFacMechanismRealmConfigurationsTable() {
        return saslAuthFacMechanismRealmConfigurationsTable;
    }

    public FormFragment getSaslAuthFacMechanismRealmConfigurationsForm() {
        return saslAuthFacMechanismRealmConfigurationsForm;
    }

    public TableFragment getServiceLoaderSaslServerTable() {
        return serviceLoaderSaslServerTable;
    }

    public FormFragment getServiceLoaderSaslServerForm() {
        return serviceLoaderSaslServerForm;
    }

    public TableFragment getKerberosSecurityTable() {
        return kerberosSecurityTable;
    }

    public FormFragment getKerberosSecurityForm() {
        return kerberosSecurityForm;
    }

    public TableFragment getCustomCredentialSecurityTable() {
        return customCredentialSecurityTable;
    }

    public FormFragment getCustomCredentialSecurityForm() {
        return customCredentialSecurityForm;
    }

    public TableFragment getAggregatePrincipalTransformerTable() {
        return aggregatePrincipalTransformerTable;
    }

    public FormFragment getAggregatePrincipalTransformerForm() {
        return aggregatePrincipalTransformerForm;
    }

    public TableFragment getChainedPrincipalTransformerTable() {
        return chainedPrincipalTransformerTable;
    }

    public FormFragment getChainedPrincipalTransformerForm() {
        return chainedPrincipalTransformerForm;
    }

    public TableFragment getConstantPrincipalTransformerTable() {
        return constantPrincipalTransformerTable;
    }

    public FormFragment getConstantPrincipalTransformerForm() {
        return constantPrincipalTransformerForm;
    }

    public TableFragment getCustomPrincipalTransformerTable() {
        return customPrincipalTransformerTable;
    }

    public FormFragment getCustomPrincipalTransformerForm() {
        return customPrincipalTransformerForm;
    }

    public TableFragment getRegexPrincipalTransformerTable() {
        return regexPrincipalTransformerTable;
    }

    public FormFragment getRegexPrincipalTransformerForm() {
        return regexPrincipalTransformerForm;
    }

    public TableFragment getRegexValidatingPrincipalTransformerTable() {
        return regexValidatingPrincipalTransformerTable;
    }

    public FormFragment getRegexValidatingPrincipalTransformerForm() {
        return regexValidatingPrincipalTransformerForm;
    }
}
