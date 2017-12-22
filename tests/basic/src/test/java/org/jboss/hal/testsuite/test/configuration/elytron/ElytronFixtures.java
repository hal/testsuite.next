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
    String ROLE_MAPPERS_ITEM = "mappers-decoders-role-mappers";
    String PERMISSION_MAPPER_ITEM = "mappers-decoders-permission-mapper-item";
    String PRINCIPAL_DECODER_ITEM = "mappers-decoders-principal-decoder-item";
    String ROLE_DECODER_ITEM = "mappers-decoders-role-decoder-item";
    String ADD_PREFIX_ROLE_MAPPER_ITEM = "mappers-decoders-add-prefix-role-mapper-item";
    String ADD_SUFFIX_ROLE_MAPPER_ITEM = "mappers-decoders-add-suffix-role-mapper-item";
    String AGGREGATE_ROLE_MAPPER_ITEM = "mappers-decoders-aggregate-role-mapper-item";
    String CONSTANT_ROLE_MAPPER_ITEM = "mappers-decoders-constant-role-mapper-item";
    String CUSTOM_ROLE_MAPPER_ITEM = "mappers-decoders-custom-role-mapper-item";
    String LOGICAL_ROLE_MAPPER_ITEM = "mappers-decoders-logical-role-mapper-item";
    String CUSTOM_PERMISSION_MAPPER_ITEM = "mappers-decoders-custom-permission-mapper-item";
    String LOGICAL_PERMISSION_MAPPER_ITEM = "mappers-decoders-logical-permission-mapper-item";
    String CONSTANT_PERMISSION_MAPPER_ITEM = "elytron-constant-permission-mapper-item";
    String SIMPLE_PERMISSION_MAPPER_ITEM = "elytron-simple-permission-mapper-item";
    String AGGREGATE_PRINCIPAL_DECODER_ITEM = "mappers-decoders-aggregate-principal-decoder-item";
    String CONCATENATING_PRINCIPAL_DECODER_ITEM = "mappers-decoders-concatenating-principal-decoder-item";
    String CONSTANT_PRINCIPAL_DECODER_ITEM = "mappers-decoders-constant-principal-decoder-item";
    String CUSTOM_PRINCIPAL_DECODER_ITEM = "mappers-decoders-custom-principal-decoder-item";
    String X500_PRINCIPAL_DECODER_ITEM = "mappers-decoders-x500-attribute-principal-decoder-item";
    String CUSTOM_ROLE_DECODER_ITEM = "mappers-decoders-custom-role-decoder-item";
    String SIMPLE_ROLE_DECODER_ITEM = "mappers-decoders-simple-role-decoder-item";
    String PREFIX = "prefix";
    String SUFFIX = "suffix";
    String ROLE_MAPPERS = "role-mappers";
    String LOGICAL_OPERATION = "logical-operation";
    String AND = "and";
    String OR = "or";
    String LEFT = "left";
    String RIGHT = "right";
    String MAPPING_MODE = "mapping-mode";
    String PRINCIPAL_DECODERS = "principal-decoders";
    String JOINER = "joiner";
    String OID = "oid";
    String ATTRIBUTE = "attribute";
    String JKS = "JKS";
    String ALIAS_FILTER = "alias-filter";
    String FILTER_ALIAS = "filter-alias";

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

    String STORES_ITEM = "stores-item";
    String CREDENTIAL_STORE_ITEM = "elytron-credential-store-item";
    String FILTERING_KEY_STORE_ITEM = "elytron-filtering-key-store-item";
    String KEY_STORE_ITEM = "elytron-key-store-item";
    String LDAP_KEY_STORE_ITEM = "elytron-ldap-key-store";
    String SSL_ITEM = "ssl-item";
    String AGGREGATE_PROVIDERS_ITEM = "elytron-aggregate-providers-item";
    String CLIENT_SSL_CONTEXT_ITEM = "elytron-client-ssl-context-item";
    String KEY_MANAGER_ITEM = "elytron-key-manager-item";
    String PROVIDER_LOADER_ITEM = "elytron-provider-loader-item";
    String SERVER_SSL_CONTEXT_ITEM = "elytron-server-ssl-context-item";
    String SECURITY_DOMAIN_ITEM = "elytron-security-domain-item";
    String TRUST_MANAGER_ITEM = "elytron-trust-manager-item";
    String AUTHENTICATION_ITEM = "authentication-item";
    String AUTHENTICATION_CONFIGURATION_ITEM = "elytron-authentication-configuration-item";
    String AUTHENTICATION_CONTEXT_ITEM = "elytron-authentication-context-item";
    String LOGS_ITEM = "logs-item";
    String FILE_AUDIT_LOG_ITEM = "elytron-file-audit-log-item";
    String PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM = "elytron-periodic-rotating-file-audit-log-item";
    String SIZE_ROTATING_FILE_AUDIT_LOG_ITEM = "elytron-size-rotating-file-audit-log-item";
    String SYSLOG_AUDIT_LOG_ITEM = "elytron-syslog-audit-log-item";
    String AGGREGATE_SECURITY_EVENT_LISTENER_ITEM = "elytron-aggregate-security-event-listener-item";
    String OTHER_ITEM = "other-item";
    String POLICY_ITEM = "elytron-policy";
    String DIR_CONTEXT_ITEM = "elytron-dir-context-item";

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
    // -------------- file-system realm

    String FILESYS_REALM_CREATE = Ids.build("filesys-realm", "create", Random.name());
    String FILESYS_REALM_UPDATE = Ids.build("filesys-realm", "update", Random.name());
    String FILESYS_REALM_DELETE = Ids.build("filesys-realm", "delete", Random.name());

    static Address filesystemRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILESYSTEM_REALM, name);
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

    // -------------- add-prefix-role-mapper

    String ADD_PRE_CREATE = Ids.build("add-pre", "create", Random.name());
    String ADD_PRE_UPDATE = Ids.build("add-pre", "update", Random.name());
    String ADD_PRE_DELETE = Ids.build("add-pre", "delete", Random.name());

    static Address addPrefixRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ADD_PREFIX_ROLE_MAPPER, name);
    }

    // -------------- add-suffix-role-mapper

    String ADD_SUF_CREATE = Ids.build("add-suf", "create", Random.name());
    String ADD_SUF_UPDATE = Ids.build("add-suf", "update", Random.name());
    String ADD_SUF_DELETE = Ids.build("add-suf", "delete", Random.name());

    static Address addSuffixRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ADD_SUFFIX_ROLE_MAPPER, name);
    }

    // -------------- aggregate-role-mapper

    String AGG_ROLE_CREATE = Ids.build("agg-role", "create", Random.name());
    String AGG_ROLE_UPDATE = Ids.build("agg-role", "update", Random.name());
    String AGG_ROLE_DELETE = Ids.build("agg-role", "delete", Random.name());

    static Address aggregateRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_ROLE_MAPPER, name);
    }

    // -------------- constant-role-mapper

    String CON_ROLE_CREATE = Ids.build("con-role", "create", Random.name());
    String CON_ROLE_UPDATE = Ids.build("con-role", "update", Random.name());
    String CON_ROLE_DELETE = Ids.build("con-role", "delete", Random.name());

    static Address constantRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_ROLE_MAPPER, name);
    }

    // -------------- logical-role-mapper

    String LOG_ROLE_CREATE = Ids.build("log-role", "create", Random.name());
    String LOG_ROLE_UPDATE = Ids.build("log-role", "update", Random.name());
    String LOG_ROLE_DELETE = Ids.build("log-role", "delete", Random.name());

    static Address logicalRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGICAL_ROLE_MAPPER, name);
    }

    // -------------- logical-permission-mapper

    String LOG_PERM_CREATE = Ids.build("log-perm", "create", Random.name());
    String LOG_PERM_UPDATE = Ids.build("log-perm", "update", Random.name());
    String LOG_PERM_DELETE = Ids.build("log-perm", "delete", Random.name());

    static Address logicalPermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGICAL_PERMISSION_MAPPER, name);
    }

    // -------------- constant-permission-mapper

    String CON_PERM_CREATE = Ids.build("con-perm", "create", Random.name());
    String CON_PERM_UPDATE = Ids.build("con-perm", "update", Random.name());
    String CON_PERM_UPDATE2 = Ids.build("con-perm2", "update", Random.name());
    String CON_PERM_DELETE = Ids.build("con-perm", "delete", Random.name());

    static Address constantPermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PERMISSION_MAPPER, name);
    }

    String PERM_CREATE = Ids.build("perm", "create", Random.name());
    String PERM_UPDATE = Ids.build("perm", "update", Random.name());
    String PERM_DELETE = Ids.build("perm", "delete", Random.name());

    // -------------- simple-permission-mapper

    String SIM_PERM_CREATE = Ids.build("sim-perm", "create", Random.name());
    String SIM_PERM_UPDATE = Ids.build("sim-perm", "update", Random.name());
    String SIM_PERM_DELETE = Ids.build("sim-perm", "delete", Random.name());

    static Address simplePermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIMPLE_PERMISSION_MAPPER, name);
    }

    String PERM_MAP_CREATE = Ids.build("perm-map", "create", Random.name());
    String PERM_MAP_UPDATE = Ids.build("perm-map", "update", Random.name());
    String PERM_MAP_DELETE = Ids.build("perm-map", "delete", Random.name());

    // -------------- aggregate-principal-decoder

    String AGG_PRI_CREATE = Ids.build("agg-pri", "create", Random.name());
    String AGG_PRI_UPDATE = Ids.build("agg-pri", "update", Random.name());
    String AGG_PRI_DELETE = Ids.build("agg-pri", "delete", Random.name());

    static Address aggregatePrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PRINCIPAL_DECODER, name);
    }

    // -------------- concatenating-principal-decoder

    String CONC_PRI_CREATE = Ids.build("conc-pri", "create", Random.name());
    String CONC_PRI_UPDATE = Ids.build("conc-pri", "update", Random.name());
    String CONC_PRI_DELETE = Ids.build("conc-pri", "delete", Random.name());

    static Address concatenatingPrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONCATENATING_PRINCIPAL_DECODER, name);
    }

    // -------------- constant-principal-decoder

    String CONS_PRI_CREATE = Ids.build("cons-pri", "create", Random.name());
    String CONS_PRI_UPDATE = Ids.build("cons-pri", "update", Random.name());
    String CONS_PRI_UPDATE2 = Ids.build("cons-pri2", "update", Random.name());
    String CONS_PRI_UPDATE3 = Ids.build("cons-pri3", "update", Random.name());
    String CONS_PRI_DELETE = Ids.build("cons-pri", "delete", Random.name());

    static Address constantPrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PRINCIPAL_DECODER, name);
    }

    // -------------- x500-attribute-principal-decoder

    String X500_PRI_CREATE = Ids.build("x500-pri", "create", Random.name());
    String X500_PRI_UPDATE = Ids.build("x500-pri", "update", Random.name());
    String X500_PRI_DELETE = Ids.build("x500-pri", "delete", Random.name());

    static Address x500PrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(X500_ATTRIBUTE_PRINCIPAL_DECODER, name);
    }

    // -------------- simple-role-decoder

    String SIMP_ROLE_CREATE = Ids.build("simp-role", "create", Random.name());
    String SIMP_ROLE_UPDATE = Ids.build("simp-role", "update", Random.name());
    String SIMP_ROLE_DELETE = Ids.build("simp-role", "delete", Random.name());

    static Address simpleRoleDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIMPLE_ROLE_DECODER, name);
    }

    // -------------- credential-store

    String CRED_ST_CREATE = Ids.build("cred-store", "create", Random.name());
    String CRED_ST_UPDATE = Ids.build("cred-store", "update", Random.name());
    String CRED_ST_DELETE = Ids.build("cred-store", "delete", Random.name());

    static Address credentialStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CREDENTIAL_STORE, name);
    }

    // -------------- filtering-key-store

    String FILT_ST_CREATE = Ids.build("filt-store", "create", Random.name());
    String FILT_ST_UPDATE = Ids.build("filt-store", "update", Random.name());
    String FILT_ST_DELETE = Ids.build("filt-store", "delete", Random.name());

    static Address filteringKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILTERING_KEY_STORE, name);
    }

    // -------------- key-store

    String KEY_ST_CREATE = Ids.build("ks", "create", Random.name());
    String KEY_ST_UPDATE = Ids.build("ks", "update", Random.name());
    String KEY_ST_DELETE = Ids.build("ks", "delete", Random.name());

    static Address keyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KEY_STORE, name);
    }

    // -------------- ldap-key-store

    String LDAPKEY_ST_CREATE = Ids.build("ldap-ks", "create", Random.name());
    String LDAPKEY_ST_UPDATE = Ids.build("ldap-ks", "update", Random.name());
    String LDAPKEY_ST_DELETE = Ids.build("ldap-ks", "delete", Random.name());

    static Address ldapKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LDAP_KEY_STORE, name);
    }

    // -------------- aggregate-providers

    String AGG_PRV_CREATE = Ids.build("agg-prv", "create", Random.name());
    String AGG_PRV_UPDATE = Ids.build("agg-prv", "update", Random.name());
    String AGG_PRV_DELETE = Ids.build("agg-prv", "delete", Random.name());

    static Address aggregateProvidersAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PROVIDERS, name);
    }

    // -------------- client-ssl-context

    String CLI_SSL_CREATE = Ids.build("cli-ssl", "create", Random.name());
    String CLI_SSL_UPDATE = Ids.build("cli-ssl", "update", Random.name());
    String CLI_SSL_DELETE = Ids.build("cli-ssl", "delete", Random.name());

    static Address clientSslContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CLIENT_SSL_CONTEXT, name);
    }

    // -------------- key-manager

    String KEY_MAN_CREATE = Ids.build("key-man", "create", Random.name());
    String KEY_MAN_UPDATE = Ids.build("key-man", "update", Random.name());
    String KEY_MAN_DELETE = Ids.build("key-man", "delete", Random.name());

    static Address keyManagertAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KEY_MANAGER, name);
    }

    // -------------- provider-loader

    String PROV_LOAD_CREATE = Ids.build("prov-load", "create", Random.name());
    String PROV_LOAD_UPDATE = Ids.build("prov-load", "update", Random.name());
    String PROV_LOAD_DELETE = Ids.build("prov-load", "delete", Random.name());

    static Address providerLoaderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_LOADER, name);
    }

    // -------------- server-ssl-context

    String SRV_SSL_CREATE = Ids.build("srv-ssl", "create", Random.name());
    String SRV_SSL_UPDATE = Ids.build("srv-ssl", "update", Random.name());
    String SRV_SSL_DELETE = Ids.build("srv-ssl", "delete", Random.name());

    static Address serverSslContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER_SSL_CONTEXT, name);
    }

    // -------------- security-domain

    String SEC_DOM_CREATE = Ids.build("sec-dom", "create", Random.name());
    String SEC_DOM_UPDATE = Ids.build("sec-dom", "update", Random.name());
    String SEC_DOM_DELETE = Ids.build("sec-dom", "delete", Random.name());

    static Address securityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SECURITY_DOMAIN, name);
    }


    // -------------- trust-manager

    String TRU_MAN_CREATE = Ids.build("tru-man", "create", Random.name());
    String TRU_MAN_UPDATE = Ids.build("tru-man", "update", Random.name());
    String TRU_MAN_DELETE = Ids.build("tru-man", "delete", Random.name());

    static Address trustManagertAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(TRUST_MANAGER, name);
    }

    // -------------- authentication-configuration

    String AUT_CF_CREATE = Ids.build("auth-conf", "create", Random.name());
    String AUT_CF_UPDATE = Ids.build("auth-conf", "update", Random.name());
    String AUT_CF_DELETE = Ids.build("auth-conf", "delete", Random.name());

    static Address authenticationConfigurationAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AUTHENTICATION_CONFIGURATION, name);
    }

    // -------------- authentication-context

    String AUT_CT_CREATE = Ids.build("auth-ct", "create", Random.name());
    String AUT_CT_UPDATE = Ids.build("auth-ct", "update", Random.name());
    String AUT_CT_DELETE = Ids.build("auth-ct", "delete", Random.name());

    static Address authenticationContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AUTHENTICATION_CONTEXT, name);
    }

    // -------------- file-audit-log

    String FILE_LOG_CREATE = Ids.build("file-log", "create", Random.name());
    String FILE_LOG_UPDATE = Ids.build("file-log", "update", Random.name());
    String FILE_LOG_DELETE = Ids.build("file-log", "delete", Random.name());

    static Address fileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILE_AUDIT_LOG, name);
    }

    // -------------- periodic-rotating-file-audit-log

    String PER_LOG_CREATE = Ids.build("per-log", "create", Random.name());
    String PER_LOG_UPDATE = Ids.build("per-log", "update", Random.name());
    String PER_LOG_DELETE = Ids.build("per-log", "delete", Random.name());

    static Address periodicRotatingfileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_AUDIT_LOG, name);
    }

    // -------------- size-rotating-file-audit-log

    String SIZ_LOG_CREATE = Ids.build("siz-log", "create", Random.name());
    String SIZ_LOG_UPDATE = Ids.build("siz-log", "update", Random.name());
    String SIZ_LOG_DELETE = Ids.build("siz-log", "delete", Random.name());

    static Address sizeRotatingfileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIZE_ROTATING_FILE_AUDIT_LOG, name);
    }

    // -------------- syslog-audit-log

    String SYS_LOG_CREATE = Ids.build("sys-log", "create", Random.name());
    String SYS_LOG_UPDATE = Ids.build("sys-log", "update", Random.name());
    String SYS_LOG_DELETE = Ids.build("sys-log", "delete", Random.name());

    static Address syslogAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SYSLOG_AUDIT_LOG, name);
    }

    // -------------- aggregate-security-event-listener

    String AGG_SEC_CREATE = Ids.build("agg-sec", "create", Random.name());
    String AGG_SEC_UPDATE = Ids.build("agg-sec", "update", Random.name());
    String AGG_SEC_DELETE = Ids.build("agg-sec", "delete", Random.name());

    static Address aggregateSecurityEventListenerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_SECURITY_EVENT_LISTENER, name);
    }

    // -------------- policy

    String POL_CREATE = Ids.build("pol", "create", Random.name());
    String POL_UPDATE = Ids.build("pol", "update", Random.name());
    String POL_DELETE = Ids.build("pol", "delete", Random.name());

    static Address policyAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(POLICY, name);
    }

    // -------------- dir-context

    String DIR_CREATE = Ids.build("dir", "create", Random.name());
    String DIR_UPDATE = Ids.build("dir", "update", Random.name());
    String DIR_DELETE = Ids.build("dir", "delete", Random.name());

    static Address dirContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(DIR_CONTEXT, name);
    }

}
