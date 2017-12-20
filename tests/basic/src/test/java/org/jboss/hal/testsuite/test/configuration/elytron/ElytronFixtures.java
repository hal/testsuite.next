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

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.resources.Ids.*;

public interface ElytronFixtures {

    String INITIAL_PROVIDERS = "initial-providers";
    String HTTP_SERVER_MECH_FACTORIES = "http-server-mechanism-factories";
    String SASL_SERVER_FACTORIES = "sasl-server-factories";
    String SASL_SERVER_FACTORY = "sasl-server-factory";
    String HTTP_SERVER_MECH_FACTORY = "http-server-mechanism-factory";
    String HTTP_FACTORIES_ITEM = "http-factories-item";
    String SASL_FACTORIES_ITEM = "sasl-factories-item";
    String OTHER_FACTORIES_ITEM = "other-factories-item";
    String TRANSFORMERS_ITEM = "transformers-item";
    String PRINCIPAL_TRANSFORMERS = "principal-transformers";
    String PROVIDERS = "providers";
    String PRINCIPAL = "principal";
    String CONSTANT = "constant";
    String REPLACEMENT = "replacement";
    String ENABLING = "enabling";
    String HASH_SHA = "HASH_SHA";
    String PREDEFINED_FILTER = "predefined-filter";

    String AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    String CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    String HTTP_AUTHENTICATION_FACTORY_ITEM = Ids.build(ELYTRON_HTTP_AUTHENTICATION_FACTORY, ITEM);
    String PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    String SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    String AGGREGATE_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_AGGREGATE_SASL_SERVER_FACTORY, ITEM);
    String CONFIGURABLE_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY, ITEM);
    String MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY, ITEM);
    String PROVIDER_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_PROVIDER_SASL_SERVER_FACTORY, ITEM);
    String SASL_AUTHENTICATION_FACTORY_ITEM = Ids.build(ELYTRON_SASL_AUTHENTICATION_FACTORY, ITEM);
    String SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_SERVICE_LOADER_SASL_SERVER_FACTORY, ITEM);
    String KERBEROS_SECURITY_FACTORY_ITEM = Ids.build(ELYTRON_KERBEROS_SECURITY_FACTORY, ITEM);
    String AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_AGGREGATE_PRINCIPAL_TRANSFORMER, ITEM);
    String CHAINED_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CHAINED_PRINCIPAL_TRANSFORMER, ITEM);
    String CONSTANT_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CONSTANT_PRINCIPAL_TRANSFORMER, ITEM);
    String CUSTOM_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CUSTOM_PRINCIPAL_TRANSFORMER, ITEM);
    String REGEX_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_REGEX_PRINCIPAL_TRANSFORMER, ITEM);
    String REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_REGEX_VALIDATING_PRINCIPAL_TRANSFORMER, ITEM);

    Address SUBSYSTEM_ADDRESS = Address.subsystem(ELYTRON);

    // -------------- aggregate-http-server-mechanism-factory

    String AGG_HTTP_CREATE = Ids.build("agg-http", "create", Random.name());
    String AGG_HTTP_UPDATE = Ids.build("agg-http", "update", Random.name());
    String AGG_HTTP_DELETE = Ids.build("agg-http", "delete", Random.name());

    static Address aggregateHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- configurable-http-server-mechanism-factory

    String CONF_HTTP_CREATE = Ids.build("conf-http", "create", Random.name());
    String CONF_HTTP_UPDATE = Ids.build("conf-http", "update", Random.name());
    String CONF_HTTP_DELETE = Ids.build("conf-http", "delete", Random.name());

    static Address configurableHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- configurable-http-server-mechanism-factory / filters - complex attribute

    String FILTERS_CREATE = Ids.build("filt", "create", Random.name());
    String FILTERS_UPDATE = Ids.build("filt", "update", Random.name());
    String FILTERS_UPDATE2 = Ids.build("filt2", "update", Random.name());
    String FILTERS_DELETE = Ids.build("filt", "delete", Random.name());

    // -------------- http-authentication-factory

    String HTTP_AUTH_CREATE = Ids.build("http-auth", "create", Random.name());
    String HTTP_AUTH_UPDATE = Ids.build("http-auth", "update", Random.name());
    String HTTP_AUTH_DELETE = Ids.build("http-auth", "delete", Random.name());

    static Address httpAuthenticationFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(HTTP_AUTHENTICATION_FACTORY, name);
    }

    // -------------- http-authentication-factory - mechanism configurations

    String MECH_CONF_CREATE = Ids.build("mech-conf", "create", Random.name());
    String MECH_CONF_UPDATE = Ids.build("mech-conf", "update", Random.name());
    String MECH_CONF_UPDATE2 = Ids.build("mech-conf2", "update", Random.name());
    String MECH_CONF_DELETE = Ids.build("mech-conf", "delete", Random.name());

    // -------------- http-authentication-factory - mechanism configurations - mechanism realm configurations

    String MECH_RE_CONF_CREATE = Ids.build("mech-re-conf", "create", Random.name());
    String MECH_RE_CONF_UPDATE = Ids.build("mech-re-conf", "update", Random.name());
    String MECH_RE_CONF_UPDATE2 = Ids.build("mech-re-conf2", "update", Random.name());
    String MECH_RE_CONF_DELETE = Ids.build("mech-re-conf", "delete", Random.name());

    // -------------- provider-http-server-mechanism-factory

    String PROV_HTTP_CREATE = Ids.build("prov-http", "create", Random.name());
    String PROV_HTTP_UPDATE = Ids.build("prov-http", "update", Random.name());
    String PROV_HTTP_UPDATE2 = Ids.build("prov-http2", "update", Random.name());
    String PROV_HTTP_UPDATE3 = Ids.build("prov-http3", "update", Random.name());
    String PROV_HTTP_DELETE = Ids.build("prov-http", "delete", Random.name());

    static Address providerHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- service-loader-http-server-mechanism-factory

    String SVC_LOAD_HTTP_CREATE = Ids.build("svc-loa-http", "create", Random.name());
    String SVC_LOAD_HTTP_UPDATE = Ids.build("svc-loa-http", "update", Random.name());
    String SVC_LOAD_HTTP_DELETE = Ids.build("svc-loa-http", "delete", Random.name());

    static Address serviceLoaderHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- aggregate-sasl-server-factory

    String AGG_SASL_CREATE = Ids.build("agg-sasl", "create", Random.name());
    String AGG_SASL_UPDATE = Ids.build("agg-sasl", "update", Random.name());
    String AGG_SASL_DELETE = Ids.build("agg-sasl", "delete", Random.name());

    static Address aggregateSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_SASL_SERVER_FACTORY, name);
    }

    // -------------- configurable-sasl-server-factory

    String CONF_SASL_CREATE = Ids.build("conf-sasl", "create", Random.name());
    String CONF_SASL_UPDATE = Ids.build("conf-sasl", "update", Random.name());
    String CONF_SASL_DELETE = Ids.build("conf-sasl", "delete", Random.name());

    static Address configurableSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONFIGURABLE_SASL_SERVER_FACTORY, name);
    }

    // -------------- mechanism-provider-filtering-sasl-server-factory

    String MECH_SASL_CREATE = Ids.build("mech-sasl", "create", Random.name());
    String MECH_SASL_UPDATE = Ids.build("mech-sasl", "update", Random.name());
    String MECH_SASL_DELETE = Ids.build("mech-sasl", "delete", Random.name());

    static Address mechanismProviderFilteringSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY, name);
    }

    // -------------- sasl-authentication-factory

    String SASL_AUTH_CREATE = Ids.build("sasl-auth", "create", Random.name());
    String SASL_AUTH_UPDATE = Ids.build("sasl-auth", "update", Random.name());
    String SASL_AUTH_DELETE = Ids.build("sasl-auth", "delete", Random.name());

    static Address saslAuthenticationFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SASL_AUTHENTICATION_FACTORY, name);
    }

    // -------------- service-loader-sasl-server-factory

    String SVC_LOAD_SASL_CREATE = Ids.build("svc-loa-sasl", "create", Random.name());
    String SVC_LOAD_SASL_UPDATE = Ids.build("svc-loa-sasl", "update", Random.name());
    String SVC_LOAD_SASL_DELETE = Ids.build("svc-loa-sasl", "delete", Random.name());

    static Address serviceLoaderSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVICE_LOADER_SASL_SERVER_FACTORY, name);
    }


    // -------------- kerberos-security

    String KERB_CREATE = Ids.build("kerb", "create", Random.name());
    String KERB_UPDATE = Ids.build("kerb", "update", Random.name());
    String KERB_DELETE = Ids.build("kerb", "delete", Random.name());

    static Address kerberosSecurityFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KERBEROS_SECURITY_FACTORY, name);
    }

    // -------------- aggregate-principal-transformer

    String AGG_PRI_TRANS_CREATE = Ids.build("agg-pri-trans", "create", Random.name());
    String AGG_PRI_TRANS_UPDATE = Ids.build("agg-pri-trans", "update", Random.name());
    String AGG_PRI_TRANS_DELETE = Ids.build("agg-pri-trans", "delete", Random.name());

    static Address aggregatePrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- chained-principal-transformer

    String CHA_PRI_TRANS_CREATE = Ids.build("cha-pri-trans", "create", Random.name());
    String CHA_PRI_TRANS_UPDATE = Ids.build("cha-pri-trans", "update", Random.name());
    String CHA_PRI_TRANS_DELETE = Ids.build("cha-pri-trans", "delete", Random.name());

    static Address chainedPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CHAINED_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- constant-principal-transformer

    String CONS_PRI_TRANS_CREATE = Ids.build("cons-pri-trans", "create", Random.name());
    String CONS_PRI_TRANS_UPDATE = Ids.build("cons-pri-trans", "update", Random.name());
    String CONS_PRI_TRANS_UPDATE2 = Ids.build("cons-pri-trans2", "update", Random.name());
    String CONS_PRI_TRANS_UPDATE3 = Ids.build("cons-pri-trans3", "update", Random.name());
    String CONS_PRI_TRANS_DELETE = Ids.build("cons-pri-trans", "delete", Random.name());

    static Address constantPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- custom-principal-transformer

    String CUST_PRI_TRANS_CREATE = Ids.build("cust-pri-trans", "create", Random.name());
    String CUST_PRI_TRANS_UPDATE = Ids.build("cust-pri-trans", "update", Random.name());
    String CUST_PRI_TRANS_DELETE = Ids.build("cust-pri-trans", "delete", Random.name());

    static Address customPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- regex-principal-transformer

    String REG_PRI_TRANS_CREATE = Ids.build("reg-pri-trans", "create", Random.name());
    String REG_PRI_TRANS_UPDATE = Ids.build("reg-pri-trans", "update", Random.name());
    String REG_PRI_TRANS_DELETE = Ids.build("reg-pri-trans", "delete", Random.name());

    static Address regexPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(REGEX_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- regex-validating-principal-transformer

    String REGV_PRI_TRANS_CREATE = Ids.build("regv-pri-trans", "create", Random.name());
    String REGV_PRI_TRANS_UPDATE = Ids.build("regv-pri-trans", "update", Random.name());
    String REGV_PRI_TRANS_DELETE = Ids.build("regv-pri-trans", "delete", Random.name());

    static Address regexValidatingPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER, name);
    }



    // ===================================================================================
    // -------------- security-domain

    String SEC_DOM_CREATE = Ids.build("sec-dom", "create", Random.name());
    String SEC_DOM_UPDATE = Ids.build("sec-dom", "update", Random.name());
    String SEC_DOM_DELETE = Ids.build("sec-dom", "delete", Random.name());

    static Address securityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SECURITY_DOMAIN, name);
    }

    // -------------- file-system realm

    String FILESYS_REALM_CREATE = Ids.build("filesys-realm", "create", Random.name());
    String FILESYS_REALM_UPDATE = Ids.build("filesys-realm", "update", Random.name());
    String FILESYS_REALM_DELETE = Ids.build("filesys-realm", "delete", Random.name());

    static Address filesystemRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILESYSTEM_REALM, name);
    }

    // -------------- provider-loader

    String PROV_LOAD_CREATE = Ids.build("prov-load", "create", Random.name());
    String PROV_LOAD_UPDATE = Ids.build("prov-load", "update", Random.name());
    String PROV_LOAD_DELETE = Ids.build("prov-load", "delete", Random.name());

    static Address providerLoaderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_LOADER, name);
    }

    // -------------- provider-sasl-server-factory

    String PROV_SASL_CREATE = Ids.build("prov-sasl", "create", Random.name());
    String PROV_SASL_UPDATE = Ids.build("prov-sasl", "update", Random.name());
    String PROV_SASL_UPDATE2 = Ids.build("prov-sasl2", "update", Random.name());
    String PROV_SASL_UPDATE3 = Ids.build("prov-sasl3", "update", Random.name());
    String PROV_SASL_DELETE = Ids.build("prov-sasl", "delete", Random.name());

    static Address providerSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_SASL_SERVER_FACTORY, name);
    }

}
