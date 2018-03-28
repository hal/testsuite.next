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
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.resources.Ids.*;

public final class ElytronFixtures {

    private static final String ADD_PREFIX_ROLE_MAPPER_PREFIX = "add-pre";
    private static final String ADD_SUFFIX_ROLE_MAPPER_PREFIX = "add-suf";
    private static final String AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX = "agg-http";
    private static final String AGGREGATE_PRINCIPAL_DECODER_PREFIX = "agg-pri";
    private static final String AGGREGATE_PRINCIPAL_TRANSFORMER_PREFIX = "agg-pri-trans";
    private static final String AGGREGATE_PROVIDERS_PREFIX = "agg-prv";
    private static final String AGGREGATE_REALM_PREFIX = "agg-rlm";
    private static final String AGGREGATE_ROLE_MAPPER_PREFIX = "agg-role";
    private static final String AGGREGATE_SASL_SERVER_FACTORY_PREFIX = "agg-sasl";
    private static final String AGGREGATOR_SECURITY_EVENT_LISTENER_PREFIX = "agg-sec";
    private static final String AUTHENTICATION_CONFIGURATION_CR_PREFIX = "auth-conf-cr";
    private static final String AUTHENTICATION_CONFIGURATION_PREFIX = "auth-conf";
    private static final String AUTHENTICATION_CONTEXT_MATCH_RULE_PREFIX = "auth-ct-match-rule";
    private static final String AUTHENTICATION_CONTEXT_PREFIX = "auth-ct";
    private static final String CACHING_REALM_PREFIX = "cac-rlm";
    private static final String CHAINED_PRINCIPAL_TRANSFORMER_PREFIX = "cha-pri-trans";
    private static final String CLIENT_SSL_CONTEXT_PREFIX = "cli-ssl";
    private static final String CONCATENATING_PRINCIPAL_DECODER_PREFIX = "conc-pri";
    private static final String CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX = "conf-http";
    private static final String CONFIGURABLE_SASL_SERVER_FACTORY_PREFIX = "conf-sasl";
    private static final String CONSTANT_PERMISSION_MAPPER_PREFIX = "con-perm";
    private static final String CONSTANT_PRINCIPAL_DECODER_PREFIX = "cons-pri";
    private static final String CONSTANT_PRINCIPAL_TRANSFORMER_PREFIX = "cons-pri-trans";
    private static final String CONSTANT_REALM_MAPPER_PREFIX = "con-rm";
    private static final String CONSTANT_ROLE_MAPPER_PREFIX = "con-role";
    private static final String CREDENTIAL_STORE_PREFIX = "cred-store";
    public static final String CREDENTIAL_STORE_CREATE = "create";
    public static final String CREDENTIAL_REFERENCE = "credential-reference";
    public static final String CREDENTIAL_REFERENCE_ALIAS = "alias";
    public static final String CREDENTIAL_REFERENCE_CLEAR_TEXT = "clear-text";
    public static final String CREDENTIAL_REFERENCE_STORE = "store";
    public static final String CREDENTIAL_REFERENCE_TYPE = "type";
    private static final String CUSTOM_MODIFIABLE_REALM_PREFIX = "cmo-rlm";
    private static final String CUSTOM_PRINCIPAL_TRANSFORMER_PREFIX = "cust-pri-trans";
    private static final String CUSTOM_REALM_MAPPER_PREFIX = "cus-rm";
    private static final String CUSTOM_REALM_PREFIX = "cst-rlm";
    private static final String DIR_CONTEXT_CR_PREFIX = "dir-cr";
    private static final String DIR_CONTEXT_PREFIX = "dir";
    private static final String FILE_AUDIT_LOG_PREFIX = "file-log";
    private static final String FILESYSTEM_REALM_PREFIX = "filesys-rlm";
    private static final String FILTER_PREFIX = "filt";
    private static final String FILTERING_KEY_STORE_PREFIX = "filt-store";
    private static final String HTTP_AUTHENTICATION_FACTORY_PREFIX = "http-auth";
    private static final String IDENTITY_REALM_PREFIX = "iden-rlm";
    private static final String JDBC_REALM_PREFIX = "jdbc-rlm";
    private static final String KERBEROS_SECURITY_PREFIX = "kerb";
    private static final String KEY_MANANAGER_PREFIX = "key-man";
    private static final String KEY_STORE_PREFIX = "ks";
    private static final String KEY_STORE_REALM_PREFIX = "ks-rlm";
    private static final String LDAP_KEY_STORE_PREFIX = "ldap-ks";
    private static final String LDAP_REALM_AM_FROM_PREFIX = "ldap-rlm-am-from";
    private static final String LDAP_REALM_AM_PREFIX = "ldap-rlm-am";
    private static final String LDAP_REALM_OTP_MAP_PREFIX = "ldap-rlm-otp-map";
    private static final String LDAP_REALM_PREFIX = "ldap-rlm";
    private static final String LDAP_REALM_USER_MAP_PREFIX = "ldap-rlm-user-map";
    private static final String LDAP_REALM_X509_MAP_PREFIX = "ldap-rlm-x509-map";
    private static final String LOGICAL_PERMISSION_MAPPER_PREFIX = "log-perm";
    private static final String LOGICAL_ROLE_MAPPER_PREFIX = "log-role";
    private static final String MAPPED_REGEX_REALM_MAPPER_PREFIX = "mapp-rm";
    private static final String MECHANISM_CONFIGURATION_PREFIX = "mech-conf";
    private static final String MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_PREFIX = "mech-sasl";
    private static final String MECHANISM_REALM_CONFIGURATION_PREFIX = "mech-re-conf";
    private static final String PERIODIC_ROTATING_FILE_AUDIT_LOG_PREFIX = "per-log";
    private static final String PERMISSION_MAPPER_PREFIX = "perm";
    private static final String PERMISSION_MAPPINGS_PREFIX = "perm-map";
    private static final String PROPERTIES_REALM_GP_PREFIX = "prop-rlm-gp";
    private static final String PROPERTIES_REALM_PREFIX = "prop-rlm";
    private static final String PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX = "prov-http";
    private static final String PROVIDER_LOADER_PREFIX = "prov-load";
    private static final String PROVIDER_SASL_SERVER_FACTORY_PREFIX = "prov-sasl";
    private static final String REGEX_PRINCIPAL_TRANSFORMER_PREFIX = "reg-pri-trans";
    private static final String REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_PREFIX = "regv-pri-trans";
    private static final String SASL_AUTHENTICATION_FACTORY_PREFIX = "sasl-auth";
    private static final String SECURITY_DOMAIN_PREFIX = "sec-dom";
    private static final String SEREVR_SSL_CONTEXT_PREFIX = "srv-ssl";
    private static final String SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX = "svc-loa-http";
    private static final String SERVICE_LOADER_SASL_SERVER_FACTORY_PREFIX = "svc-loa-sasl";
    private static final String SIMPLE_PERMISSION_MAPPER_PREFIX = "sim-perm";
    private static final String SIMPLE_REGEX_REALM_MAPPER_PREFIX = "simp-rm";
    private static final String SIMPLE_ROLE_DECODER_PREFIX = "simp-role";
    private static final String SIZE_ROTATING_FILE_AUDIT_LOG_PREFIX = "siz-log";
    private static final String SQL_BCM_PREFIX = "sql-bcm";
    private static final String SQL_CPM_PREFIX = "sql-cpm";
    private static final String SQL_PREFIX = "sql";
    private static final String SQL_SDM_PREFIX = "sql-sdm";
    private static final String SQL_SM_PREFIX = "sql-sm";
    private static final String SQL_SSDM_PREFIX = "sql-ssdm";
    private static final String SYSLOG_AUDIT_LOG_PREFIX = "sys-log";
    private static final String TOKEN_REALM_JWT_PREFIX = "token-rlm-jwt";
    private static final String TOKEN_REALM_OAU_PREFIX = "token-rlm-oau";
    private static final String TOKEN_REALM_PREFIX = "token-rlm";
    private static final String TRUST_MANAGER_CRL_PREFIX = "tru-man-crl";
    private static final String TRUST_MANAGER_PREFIX = "tru-man";
    private static final String TRY_UPDATE = "try-update";
    private static final String X500_ATTRIBUTE_PRINCIPAL_DECODER_PREFIX = "x500-pri";

    static final String ADD_CUSTOM_POLICY = "Add Custom Policy";
    static final String ADD_JACC_POLICY = "Add JACC Policy";
    static final String ADD_PREFIX_ROLE_MAPPER_ITEM = "mappers-decoders-add-prefix-role-mapper-item";
    static final String ADD_SUFFIX_ROLE_MAPPER_ITEM = "mappers-decoders-add-suffix-role-mapper-item";
    static final String AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(
            ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    static final String AGGREGATE_PRINCIPAL_DECODER_ITEM = "mappers-decoders-aggregate-principal-decoder-item";
    static final String AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_AGGREGATE_PRINCIPAL_TRANSFORMER, ITEM);
    static final String AGGREGATE_PROVIDERS_ITEM = "elytron-aggregate-providers-item";
    static final String AGGREGATE_REALM_ITEM = "elytron-aggregate-realm-item";
    static final String AGGREGATE_ROLE_MAPPER_ITEM = "mappers-decoders-aggregate-role-mapper-item";
    static final String AGGREGATE_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_AGGREGATE_SASL_SERVER_FACTORY, ITEM);
    static final String AGGREGATE_SECURITY_EVENT_LISTENER_ITEM = "elytron-aggregate-security-event-listener-item";
    static final String ALGORITHM_FROM = "algorithm-from";
    static final String ALIAS_FILTER = "alias-filter";
    static final String AND = "and";
    static final String ANY_STRING = Random.name();
    static final String APP_ROLES_PROPS = "application-roles.properties";
    static final String APP_USERS_PROPS = "application-users.properties";
    static final String ATTRIBUTE = "attribute";
    static final String ATTRIBUTE_NAME = "attribute-name";
    static final String AUDIENCE = "audience";
    static final String AUTHENTICATION_CONFIGURATION_ITEM = "elytron-authentication-configuration-item";
    static final String AUTHENTICATION_CONTEXT_ITEM = "elytron-authentication-context-item";
    static final String AUTHENTICATION_ITEM = "authentication-item";
    static final String AUTHENTICATION_NAME = "authentication-name";
    static final String AUTHENTICATION_REALM = "authentication-realm";
    static final String AUTHORIZATION_REALM = "authorization-realm";
    static final String BCRYPT_MAPPER = "bcrypt-mapper";
    static final String BCRYPT_MAPPER_TAB = "elytron-jdbc-realm-principal-query-bcrypt-mapper-tab";
    static final String CACHING_REALM_ITEM = "elytron-caching-realm-item";
    static final String CERTIFICATE_FROM = "certificate-from";
    static final String CERTIFICATE_REVOCATION_LIST = "certificate-revocation-list";
    static final String CHAINED_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CHAINED_PRINCIPAL_TRANSFORMER, ITEM);
    static final String CLEAR_PASSWORD_MAPPER = "clear-password-mapper";
    static final String CLEAR_PASSWORD_MAPPER_TAB = "elytron-jdbc-realm-principal-query-clear-password-mapper-tab";
    static final String CLIENT_ID = "client-id";
    static final String CLIENT_SECRET = "client-secret";
    static final String CLIENT_SSL_CONTEXT_ITEM = "elytron-client-ssl-context-item";
    static final String CONCATENATING_PRINCIPAL_DECODER_ITEM = "mappers-decoders-concatenating-principal-decoder-item";
    static final String CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(
            ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    static final String CONFIGURABLE_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_CONFIGURABLE_SASL_SERVER_FACTORY,
            ITEM);
    static final String CONSTANT = "constant";
    static final String CONSTANT_PERMISSION_MAPPER_ITEM = "elytron-constant-permission-mapper-item";
    static final String CONSTANT_PRINCIPAL_DECODER_ITEM = "mappers-decoders-constant-principal-decoder-item";
    static final String CONSTANT_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CONSTANT_PRINCIPAL_TRANSFORMER, ITEM);
    static final String CONSTANT_REALM_MAPPER_ITEM = "elytron-constant-realm-mapper-item";
    static final String CONSTANT_ROLE_MAPPER_ITEM = "mappers-decoders-constant-role-mapper-item";
    static final String CREDENTIAL_STORE_ITEM = "elytron-credential-store-item";
    static final String CUSTOM_MODIFIABLE_REALM_ITEM = "elytron-custom-modifiable-realm-item";
    static final String CUSTOM_PERMISSION_MAPPER_ITEM = "mappers-decoders-custom-permission-mapper-item";
    static final String CUSTOM_PRINCIPAL_DECODER_ITEM = "mappers-decoders-custom-principal-decoder-item";
    static final String CUSTOM_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_CUSTOM_PRINCIPAL_TRANSFORMER, ITEM);
    static final String CUSTOM_REALM_ITEM = "elytron-custom-realm-item";
    static final String CUSTOM_REALM_MAPPER_ITEM = "elytron-custom-realm-mapper-item";
    static final String CUSTOM_ROLE_DECODER_ITEM = "mappers-decoders-custom-role-decoder-item";
    static final String CUSTOM_ROLE_MAPPER_ITEM = "mappers-decoders-custom-role-mapper-item";
    static final String DELEGATE_REALM_MAPPER = "delegate-realm-mapper";
    static final String DIGEST_REALM_NAME = "digest-realm-name";
    static final String DIR_CONTEXT_ITEM = "elytron-dir-context-item";
    static final String ENABLING = "enabling";
    static final String EXTENDS = "extends";
    static final String FILE_AUDIT_LOG_ITEM = "elytron-file-audit-log-item";
    static final String FILESYSTEM_REALM_ITEM = "elytron-filesystem-realm-item";
    static final String FILTER_ALIAS = "filter-alias";
    static final String FILTER_NAME = "filter-name";
    static final String FILTERING_KEY_STORE_ITEM = "elytron-filtering-key-store-item";
    static final String GROUPS_PROPERTIES = "groups-properties";
    static final String HASH_FROM = "hash-from";
    static final String HASH_SHA = "HASH_SHA";
    static final String HOSTNAME = "host-name";
    static final String HTTP_AUTHENTICATION_FACTORY_ITEM = Ids.build(ELYTRON_HTTP_AUTHENTICATION_FACTORY, ITEM);
    static final String HTTP_FACTORIES_ITEM = "http-factories-item";
    static final String HTTP_SERVER_MECH_FACTORIES = "http-server-mechanism-factories";
    static final String HTTP_SERVER_MECH_FACTORY = "http-server-mechanism-factory";
    static final String IDENTITY = "identity";
    static final String IDENTITY_ATTRIBUTES_MAPPING_LB = "Identity Attribute Mapping";
    static final String IDENTITY_REALM_ITEM = "elytron-identity-realm-item";
    static final String INITIAL_PROVIDERS = "initial-providers";
    static final String INTROSPECTION_URL = "introspection-url";
    static final String ITERATION_COUNT_INDEX = "iteration-count-index";
    static final String JBOSS_SRV_CONFIG_DIR = "jboss.server.config.dir";
    static final String JDBC_REALM_ITEM = "elytron-jdbc-realm-item";
    public static final String JKS = "JKS";
    static final String JOINER = "joiner";
    static final String JWT = "jwt";
    static final String KERBEROS_SECURITY_FACTORY_ITEM = Ids.build(ELYTRON_KERBEROS_SECURITY_FACTORY, ITEM);
    static final String KEY_MANAGER_ITEM = "elytron-key-manager-item";
    static final String KEY_STORE_ITEM = "elytron-key-store-item";
    static final String KEYSTORE_REALM_ITEM = "elytron-key-store-realm-item";
    static final String LDAP_KEY_STORE_ITEM = "elytron-ldap-key-store";
    static final String LDAP_REALM_ITEM = "elytron-ldap-realm-item";
    static final String LEFT = "left";
    static final String LEVELS = "levels";
    static final String LOCALHOST = "127.0.0.1";
    static final String LOGICAL_OPERATION = "logical-operation";
    static final String LOGICAL_PERMISSION_MAPPER_ITEM = "mappers-decoders-logical-permission-mapper-item";
    static final String LOGICAL_ROLE_MAPPER_ITEM = "mappers-decoders-logical-role-mapper-item";
    static final String LOGS_ITEM = "logs-item";
    static final String MAPPED_REGEX_REALM_MAPPER_ITEM = "elytron-mapped-regex-realm-mapper-item";
    static final String MAPPING_MODE = "mapping-mode";
    static final String MATCH_ABSTRACT_TYPE = "match-abstract-type";
    static final String MATCH_HOST = "match-host";
    static final String MATCH_RULES = "match-rules";
    static final String MATCH_RULES_TITLE = "Match Rules";
    static final String MAXIMUM_AGE = "maximum-age";
    static final String MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_ITEM = Ids.build(
            ELYTRON_MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY, ITEM);
    static final String OAUTH2_INTROSPECTION = "oauth2-introspection";
    static final String OID = "oid";
    static final String OR = "or";
    static final String OTHER_FACTORIES_ITEM = "other-factories-item";
    static final String OTHER_ITEM = "other-item";
    static final String OUTFLOW_SECURITY_DOMAINS = "outflow-security-domains";
    static final String PASSWORD_INDEX = "password-index";
    static final String PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM = "elytron-periodic-rotating-file-audit-log-item";
    static final String PERMISSION_MAPPER_ITEM = "mappers-decoders-permission-mapper-item";
    static final String POLICY_ITEM = "elytron-policy";
    static final String PQ_LABEL = "Principal Query";
    static final String PREDEFINED_FILTER = "predefined-filter";
    static final String PREFIX = "prefix";
    static final String PRINCIPAL = "principal";
    static final String PRINCIPAL_CLAIM = "principal-claim";
    static final String PRINCIPAL_DECODER_ITEM = "mappers-decoders-principal-decoder-item";
    static final String PRINCIPAL_DECODERS = "principal-decoders";
    static final String PRINCIPAL_TRANSFORMER = "principal-transformer";
    static final String PRINCIPAL_TRANSFORMERS = "principal-transformers";
    static final String PROPERTIES_REALM_ITEM = "elytron-properties-realm-item";
    static final String PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(
            ELYTRON_PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    static final String PROVIDER_LOADER_ITEM = "elytron-provider-loader-item";
    static final String PROVIDER_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_PROVIDER_SASL_SERVER_FACTORY, ITEM);
    static final String PROVIDERS = "providers";
    static final String PUBLIC_KEY = "public-key";
    static final String REALM_MAP = "realm-map";
    static final String REALM_MAPPER_ITEM = "realm-mapper-item";
    static final String REALMS = "Realms";
    static final String REGEX_PATTERN = "[a-z]@([a-z])";
    static final String REGEX_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(ELYTRON_REGEX_PRINCIPAL_TRANSFORMER, ITEM);
    static final String REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM = Ids.build(
            ELYTRON_REGEX_VALIDATING_PRINCIPAL_TRANSFORMER, ITEM);
    static final String REPLACEMENT = "replacement";
    static final String RIGHT = "right";
    static final String ROLE_DECODER_ITEM = "mappers-decoders-role-decoder-item";
    static final String ROLE_MAPPERS = "role-mappers";
    static final String ROLE_MAPPERS_ITEM = "mappers-decoders-role-mappers";
    static final String SALT_INDEX = "salt-index";
    static final String SALTED_SIMPLE_DIGEST_MAPPER = "salted-simple-digest-mapper";
    static final String SALTED_SIMPLE_DIGEST_MAPPER_TAB = "elytron-jdbc-realm-principal-query-salted-simple-digest-mapper-tab";
    static final String SASL_AUTHENTICATION_FACTORY_ITEM = Ids.build(ELYTRON_SASL_AUTHENTICATION_FACTORY, ITEM);
    static final String SASL_FACTORIES_ITEM = "sasl-factories-item";
    static final String SASL_SERVER_FACTORIES = "sasl-server-factories";
    static final String SASL_SERVER_FACTORY = "sasl-server-factory";
    static final String SCRAM_MAPPER = "scram-mapper";
    static final String SCRAM_MAPPER_TAB = "elytron-jdbc-realm-principal-query-scram-mapper-tab";
    static final String SECURITY_DOMAIN_ITEM = "elytron-security-domain-item";
    static final String SECURITY_EVENT_LISTENERS = "security-event-listeners";
    static final String SECURITY_REALM_ITEM = "security-realm-item";
    static final String SEED_FROM = "seed-from";
    static final String SEQUENCE_FROM = "sequence-from";
    static final String SERVER_ADDRESS = "server-address";
    static final String SERVER_SSL_CONTEXT_ITEM = "elytron-server-ssl-context-item";
    static final String SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(
            ELYTRON_SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    static final String SERVICE_LOADER_SASL_SERVER_FACTORY_ITEM = Ids.build(ELYTRON_SERVICE_LOADER_SASL_SERVER_FACTORY,
            ITEM);
    static final String SIMPLE_DIGEST_MAPPER = "simple-digest-mapper";
    static final String SIMPLE_DIGEST_MAPPER_TAB = "elytron-jdbc-realm-principal-query-simple-digest-mapper-tab";
    static final String SIMPLE_PERMISSION_MAPPER_ITEM = "elytron-simple-permission-mapper-item";
    static final String SIMPLE_REGEX_REALM_MAPPER_ITEM = "elytron-simple-regex-realm-mapper-item";
    static final String SIMPLE_ROLE_DECODER_ITEM = "mappers-decoders-simple-role-decoder-item";
    static final String SIZE_ROTATING_FILE_AUDIT_LOG_ITEM = "elytron-size-rotating-file-audit-log-item";
    static final String SSL_ITEM = "ssl-item";
    static final String STORES_ITEM = "stores-item";
    static final String SUFFIX = "suffix";
    static final String SUFFIX_LOG = "yy-mm";
    static final String SYSLOG_AUDIT_LOG_ITEM = "elytron-syslog-audit-log-item";
    static final String TOKEN_REALM_ITEM = "elytron-token-realm-item";
    static final String TRANSFORMERS_ITEM = "transformers-item";
    static final String TRUST_MANAGER_ITEM = "elytron-trust-manager-item";
    static final String X500_PRINCIPAL_DECODER_ITEM = "mappers-decoders-x500-attribute-principal-decoder-item";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(ELYTRON);

    // -------------- aggregate-http-server-mechanism-factory

    static final String AGG_HTTP_CREATE = Ids.build(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String AGG_HTTP_UPDATE = Ids.build(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String AGG_HTTP_TRY_UPDATE = Ids.build(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX, TRY_UPDATE,
            Random.name());
    static final String AGG_HTTP_DELETE = Ids.build(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address aggregateHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- configurable-http-server-mechanism-factory

    static final String CONF_HTTP_CREATE = Ids.build(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String CONF_HTTP_UPDATE = Ids.build(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String CONF_HTTP_TRY_UPDATE = Ids.build(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX, TRY_UPDATE,
            Random.name());
    static final String CONF_HTTP_DELETE = Ids.build(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address configurableHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- configurable-http-server-mechanism-factory / filters - complex attribute

    static final String FILTERS_CREATE = Ids.build(FILTER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String FILTERS_UPDATE = Ids.build(FILTER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String FILTERS_UPDATE2 = Ids.build("filt2", CrudConstants.UPDATE, Random.name());
    static final String FILTERS_DELETE = Ids.build(FILTER_PREFIX, CrudConstants.DELETE, Random.name());

    // -------------- http-authentication-factory

    static final String HTTP_AUTH_CREATE = Ids.build(HTTP_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String HTTP_AUTH_UPDATE = Ids.build(HTTP_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String HTTP_AUTH_TRY_UPDATE = Ids.build(HTTP_AUTHENTICATION_FACTORY_PREFIX, TRY_UPDATE, Random.name());
    static final String HTTP_AUTH_DELETE = Ids.build(HTTP_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address httpAuthenticationFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(HTTP_AUTHENTICATION_FACTORY, name);
    }

    // -------------- http-authentication-factory - mechanism configurations

    static final String MECH_CONF_CREATE = Ids.build(MECHANISM_CONFIGURATION_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String MECH_CONF_UPDATE = Ids.build(MECHANISM_CONFIGURATION_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String MECH_CONF_UPDATE2 = Ids.build("mech-conf2", CrudConstants.UPDATE, Random.name());
    static final String MECH_CONF_DELETE = Ids.build(MECHANISM_CONFIGURATION_PREFIX, CrudConstants.DELETE,
            Random.name());

    // -------------- http-authentication-factory - mechanism configurations - mechanism realm configurations

    static final String MECH_RE_CONF_CREATE = Ids.build(MECHANISM_REALM_CONFIGURATION_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String MECH_RE_CONF_UPDATE = Ids.build(MECHANISM_REALM_CONFIGURATION_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String MECH_RE_CONF_UPDATE2 = Ids.build("mech-re-conf2", CrudConstants.UPDATE, Random.name());
    static final String MECH_RE_CONF_DELETE = Ids.build(MECHANISM_REALM_CONFIGURATION_PREFIX, CrudConstants.DELETE,
            Random.name());

    // -------------- provider-http-server-mechanism-factory

    static final String PROV_HTTP_CREATE = Ids.build(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String PROV_HTTP_UPDATE = Ids.build(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String PROV_HTTP_UPDATE2 = Ids.build("prov-http2", CrudConstants.UPDATE, Random.name());
    static final String PROV_HTTP_UPDATE3 = Ids.build("prov-http3", CrudConstants.UPDATE, Random.name());
    static final String PROV_HTTP_UPDATE4 = Ids.build("prov-http4", CrudConstants.UPDATE, Random.name());
    static final String PROV_HTTP_DELETE = Ids.build(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address providerHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- service-loader-http-server-mechanism-factory

    static final String SVC_LOAD_HTTP_CREATE = Ids.build(SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String SVC_LOAD_HTTP_UPDATE = Ids.build(SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String SVC_LOAD_HTTP_DELETE = Ids.build(SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address serviceLoaderHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVICE_LOADER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- aggregate-sasl-server-factory

    static final String AGG_SASL_CREATE = Ids.build(AGGREGATE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AGG_SASL_UPDATE = Ids.build(AGGREGATE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AGG_SASL_DELETE = Ids.build(AGGREGATE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address aggregateSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_SASL_SERVER_FACTORY, name);
    }

    // -------------- configurable-sasl-server-factory

    static final String CONF_SASL_CREATE = Ids.build(CONFIGURABLE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CONF_SASL_UPDATE = Ids.build(CONFIGURABLE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CONF_SASL_DELETE = Ids.build(CONFIGURABLE_SASL_SERVER_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address configurableSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONFIGURABLE_SASL_SERVER_FACTORY, name);
    }

    // -------------- mechanism-provider-filtering-sasl-server-factory

    static final String MECH_SASL_CREATE = Ids.build(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String MECH_SASL_UPDATE = Ids.build(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String MECH_SASL_TRY_UPDATE = Ids.build(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_PREFIX,
            TRY_UPDATE, Random.name());
    static final String MECH_SASL_DELETE = Ids.build(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address mechanismProviderFilteringSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MECHANISM_PROVIDER_FILTERING_SASL_SERVER_FACTORY, name);
    }

    // -------------- sasl-authentication-factory

    static final String SASL_AUTH_CREATE = Ids.build(SASL_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String SASL_AUTH_UPDATE = Ids.build(SASL_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String SASL_AUTH_DELETE = Ids.build(SASL_AUTHENTICATION_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address saslAuthenticationFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SASL_AUTHENTICATION_FACTORY, name);
    }

    // -------------- service-loader-sasl-server-factory

    static final String SVC_LOAD_SASL_CREATE = Ids.build(SERVICE_LOADER_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String SVC_LOAD_SASL_UPDATE = Ids.build(SERVICE_LOADER_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String SVC_LOAD_SASL_DELETE = Ids.build(SERVICE_LOADER_SASL_SERVER_FACTORY_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address serviceLoaderSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVICE_LOADER_SASL_SERVER_FACTORY, name);
    }


    // -------------- kerberos-security

    static final String KERB_CREATE = Ids.build(KERBEROS_SECURITY_PREFIX, CrudConstants.CREATE, Random.name());
    static final String KERB_UPDATE = Ids.build(KERBEROS_SECURITY_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String KERB_TRY_UPDATE = Ids.build(KERBEROS_SECURITY_PREFIX, TRY_UPDATE, Random.name());
    static final String KERB_DELETE = Ids.build(KERBEROS_SECURITY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address kerberosSecurityFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KERBEROS_SECURITY_FACTORY, name);
    }

    // -------------- aggregate-principal-transformer

    static final String AGG_PRI_TRANS_CREATE = Ids.build(AGGREGATE_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AGG_PRI_TRANS_UPDATE = Ids.build(AGGREGATE_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AGG_PRI_TRANS_TRY_UPDATE = Ids.build(AGGREGATE_PRINCIPAL_TRANSFORMER_PREFIX, TRY_UPDATE,
            Random.name());
    static final String AGG_PRI_TRANS_DELETE = Ids.build(AGGREGATE_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address aggregatePrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- chained-principal-transformer

    static final String CHA_PRI_TRANS_CREATE = Ids.build(CHAINED_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CHA_PRI_TRANS_UPDATE = Ids.build(CHAINED_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CHA_PRI_TRANS_DELETE = Ids.build(CHAINED_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address chainedPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CHAINED_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- constant-principal-transformer

    static final String CONS_PRI_TRANS_CREATE = Ids.build(CONSTANT_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CONS_PRI_TRANS_UPDATE = Ids.build(CONSTANT_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CONS_PRI_TRANS_UPDATE2 = Ids.build("cons-pri-trans2", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE3 = Ids.build("cons-pri-trans3", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE4 = Ids.build("cons-pri-trans4", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE5 = Ids.build("cons-pri-trans5", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE6 = Ids.build("cons-pri-trans6", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE7 = Ids.build("cons-pri-trans7", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_UPDATE8 = Ids.build("cons-pri-trans8", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_TRANS_DELETE = Ids.build(CONSTANT_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address constantPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- custom-principal-transformer

    static final String CUST_PRI_TRANS_CREATE = Ids.build(CUSTOM_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CUST_PRI_TRANS_UPDATE = Ids.build(CUSTOM_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CUST_PRI_TRANS_DELETE = Ids.build(CUSTOM_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address customPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- regex-principal-transformer

    static final String REG_PRI_TRANS_CREATE = Ids.build(REGEX_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String REG_PRI_TRANS_UPDATE = Ids.build(REGEX_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String REG_PRI_TRANS_TRY_UPDATE = Ids.build(REGEX_PRINCIPAL_TRANSFORMER_PREFIX, TRY_UPDATE,
            Random.name());
    static final String REG_PRI_TRANS_DELETE = Ids.build(REGEX_PRINCIPAL_TRANSFORMER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address regexPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(REGEX_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- regex-validating-principal-transformer

    static final String REGV_PRI_TRANS_CREATE = Ids.build(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_PREFIX,
            CrudConstants.CREATE, Random.name());
    static final String REGV_PRI_TRANS_UPDATE = Ids.build(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_PREFIX,
            CrudConstants.UPDATE, Random.name());
    static final String REGV_PRI_TRANS_TRY_UPDATE = Ids.build(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_PREFIX, TRY_UPDATE,
            Random.name());
    static final String REGV_PRI_TRANS_DELETE = Ids.build(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_PREFIX,
            CrudConstants.DELETE, Random.name());

    static Address regexValidatingPrincipalTransformerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(REGEX_VALIDATING_PRINCIPAL_TRANSFORMER, name);
    }

    // -------------- provider-sasl-server-factory

    static final String PROV_SASL_CREATE = Ids.build(PROVIDER_SASL_SERVER_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String PROV_SASL_UPDATE = Ids.build(PROVIDER_SASL_SERVER_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String PROV_SASL_UPDATE2 = Ids.build("prov-sasl2", CrudConstants.UPDATE, Random.name());
    static final String PROV_SASL_UPDATE3 = Ids.build("prov-sasl3", CrudConstants.UPDATE, Random.name());
    static final String PROV_SASL_UPDATE4 = Ids.build("prov-sasl4", CrudConstants.UPDATE, Random.name());
    static final String PROV_SASL_DELETE = Ids.build(PROVIDER_SASL_SERVER_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address providerSaslServerFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_SASL_SERVER_FACTORY, name);
    }

    // -------------- add-prefix-role-mapper

    static final String ADD_PRE_CREATE = Ids.build(ADD_PREFIX_ROLE_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ADD_PRE_UPDATE = Ids.build(ADD_PREFIX_ROLE_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ADD_PRE_DELETE = Ids.build(ADD_PREFIX_ROLE_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address addPrefixRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ADD_PREFIX_ROLE_MAPPER, name);
    }

    // -------------- add-suffix-role-mapper

    static final String ADD_SUF_CREATE = Ids.build(ADD_SUFFIX_ROLE_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ADD_SUF_UPDATE = Ids.build(ADD_SUFFIX_ROLE_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ADD_SUF_DELETE = Ids.build(ADD_SUFFIX_ROLE_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address addSuffixRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ADD_SUFFIX_ROLE_MAPPER, name);
    }

    // -------------- aggregate-role-mapper

    static final String AGG_ROLE_CREATE = Ids.build(AGGREGATE_ROLE_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String AGG_ROLE_UPDATE = Ids.build(AGGREGATE_ROLE_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String AGG_ROLE_DELETE = Ids.build(AGGREGATE_ROLE_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address aggregateRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_ROLE_MAPPER, name);
    }

    // -------------- constant-role-mapper

    static final String CON_ROLE_CREATE = Ids.build(CONSTANT_ROLE_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CON_ROLE_UPDATE = Ids.build(CONSTANT_ROLE_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CON_ROLE_DELETE = Ids.build(CONSTANT_ROLE_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address constantRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_ROLE_MAPPER, name);
    }

    // -------------- logical-role-mapper

    static final String LOG_ROLE_CREATE = Ids.build(LOGICAL_ROLE_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String LOG_ROLE_UPDATE = Ids.build(LOGICAL_ROLE_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String LOG_ROLE_DELETE = Ids.build(LOGICAL_ROLE_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address logicalRoleMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGICAL_ROLE_MAPPER, name);
    }

    // -------------- logical-permission-mapper

    static final String LOG_PERM_CREATE = Ids.build(LOGICAL_PERMISSION_MAPPER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String LOG_PERM_UPDATE = Ids.build(LOGICAL_PERMISSION_MAPPER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String LOG_PERM_DELETE = Ids.build(LOGICAL_PERMISSION_MAPPER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address logicalPermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGICAL_PERMISSION_MAPPER, name);
    }

    // -------------- constant-permission-mapper

    static final String CON_PERM_CREATE = Ids.build(CONSTANT_PERMISSION_MAPPER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CON_PERM_UPDATE = Ids.build(CONSTANT_PERMISSION_MAPPER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CON_PERM_UPDATE2 = Ids.build("con-perm2", CrudConstants.UPDATE, Random.name());
    static final String CON_PERM_DELETE = Ids.build(CONSTANT_PERMISSION_MAPPER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address constantPermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PERMISSION_MAPPER, name);
    }

    static final String PERM_CREATE = Ids.build(PERMISSION_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String PERM_UPDATE = Ids.build(PERMISSION_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String PERM_DELETE = Ids.build(PERMISSION_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    // -------------- simple-permission-mapper

    static final String SIM_PERM_CREATE = Ids.build(SIMPLE_PERMISSION_MAPPER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String SIM_PERM_UPDATE = Ids.build(SIMPLE_PERMISSION_MAPPER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String SIM_PERM_DELETE = Ids.build(SIMPLE_PERMISSION_MAPPER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address simplePermissionMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIMPLE_PERMISSION_MAPPER, name);
    }

    static final String PERM_MAP_CREATE = Ids.build(PERMISSION_MAPPINGS_PREFIX, CrudConstants.CREATE, Random.name());
    static final String PERM_MAP_UPDATE = Ids.build(PERMISSION_MAPPINGS_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String PERM_MAP_DELETE = Ids.build(PERMISSION_MAPPINGS_PREFIX, CrudConstants.DELETE, Random.name());

    // -------------- aggregate-principal-decoder

    static final String AGG_PRI_CREATE = Ids.build(AGGREGATE_PRINCIPAL_DECODER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AGG_PRI_UPDATE = Ids.build(AGGREGATE_PRINCIPAL_DECODER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AGG_PRI_DELETE = Ids.build(AGGREGATE_PRINCIPAL_DECODER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address aggregatePrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PRINCIPAL_DECODER, name);
    }

    // -------------- concatenating-principal-decoder

    static final String CONC_PRI_CREATE = Ids.build(CONCATENATING_PRINCIPAL_DECODER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CONC_PRI_UPDATE = Ids.build(CONCATENATING_PRINCIPAL_DECODER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CONC_PRI_DELETE = Ids.build(CONCATENATING_PRINCIPAL_DECODER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address concatenatingPrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONCATENATING_PRINCIPAL_DECODER, name);
    }

    // -------------- constant-principal-decoder

    static final String CONS_PRI_CREATE = Ids.build(CONSTANT_PRINCIPAL_DECODER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String CONS_PRI_UPDATE = Ids.build(CONSTANT_PRINCIPAL_DECODER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String CONS_PRI_UPDATE2 = Ids.build("cons-pri2", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_UPDATE3 = Ids.build("cons-pri3", CrudConstants.UPDATE, Random.name());
    static final String CONS_PRI_DELETE = Ids.build(CONSTANT_PRINCIPAL_DECODER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address constantPrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_PRINCIPAL_DECODER, name);
    }

    // -------------- x500-attribute-principal-decoder

    static final String X500_PRI_CREATE = Ids.build(X500_ATTRIBUTE_PRINCIPAL_DECODER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String X500_PRI_UPDATE = Ids.build(X500_ATTRIBUTE_PRINCIPAL_DECODER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String X500_PRI_DELETE = Ids.build(X500_ATTRIBUTE_PRINCIPAL_DECODER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address x500PrincipalDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(X500_ATTRIBUTE_PRINCIPAL_DECODER, name);
    }

    // -------------- simple-role-decoder

    static final String SIMP_ROLE_CREATE = Ids.build(SIMPLE_ROLE_DECODER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SIMP_ROLE_UPDATE = Ids.build(SIMPLE_ROLE_DECODER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SIMP_ROLE_DELETE = Ids.build(SIMPLE_ROLE_DECODER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address simpleRoleDecoderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIMPLE_ROLE_DECODER, name);
    }

    // -------------- credential-store

    static final String CRED_ST_CREATE = Ids.build(CREDENTIAL_STORE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CRED_ST_UPDATE = Ids.build(CREDENTIAL_STORE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CRED_ST_DELETE = Ids.build(CREDENTIAL_STORE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address credentialStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CREDENTIAL_STORE, name);
    }

    // -------------- filtering-key-store

    static final String FILT_ST_CREATE = Ids.build(FILTERING_KEY_STORE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String FILT_ST_UPDATE = Ids.build(FILTERING_KEY_STORE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String FILT_ST_DELETE = Ids.build(FILTERING_KEY_STORE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address filteringKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILTERING_KEY_STORE, name);
    }

    // -------------- key-store

    public static final String KEY_ST_CREATE = Ids.build(KEY_STORE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String KEY_ST_UPDATE = Ids.build(KEY_STORE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String KEY_ST_CR_UPDATE = Ids.build(KEY_STORE_PREFIX, "cr-update", Random.name());
    public static final String KEY_ST_DELETE = Ids.build(KEY_STORE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address keyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KEY_STORE, name);
    }

    // -------------- ldap-key-store

    static final String LDAPKEY_ST_CREATE = Ids.build(LDAP_KEY_STORE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String LDAPKEY_ST_UPDATE = Ids.build(LDAP_KEY_STORE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String LDAPKEY_ST_TEMP1_UPDATE = Ids.build("ldap-ks-1", CrudConstants.UPDATE, Random.name());
    static final String LDAPKEY_ST_TEMP2_DELETE = Ids.build("ldap-ks-2", CrudConstants.UPDATE, Random.name());
    static final String LDAPKEY_ST_TEMP3_ADD = Ids.build("ldap-ks-3", "add", Random.name());
    static final String LDAPKEY_ST_TEMP4_TRY_ADD = Ids.build("ldap-ks-4", "try-add", Random.name());
    static final String LDAPKEY_ST_DELETE = Ids.build(LDAP_KEY_STORE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address ldapKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LDAP_KEY_STORE, name);
    }

    // -------------- aggregate-providers

    static final String AGG_PRV_CREATE = Ids.build(AGGREGATE_PROVIDERS_PREFIX, CrudConstants.CREATE, Random.name());
    static final String AGG_PRV_UPDATE = Ids.build(AGGREGATE_PROVIDERS_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String AGG_PRV_DELETE = Ids.build(AGGREGATE_PROVIDERS_PREFIX, CrudConstants.DELETE, Random.name());

    static Address aggregateProvidersAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_PROVIDERS, name);
    }

    // -------------- client-ssl-context

    static final String CLI_SSL_CREATE = Ids.build(CLIENT_SSL_CONTEXT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CLI_SSL_UPDATE = Ids.build(CLIENT_SSL_CONTEXT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CLI_SSL_DELETE = Ids.build(CLIENT_SSL_CONTEXT_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address clientSslContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CLIENT_SSL_CONTEXT, name);
    }

    // -------------- key-manager

    static final String KEY_MAN_CREATE = Ids.build(KEY_MANANAGER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String KEY_MAN_UPDATE = Ids.build(KEY_MANANAGER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String KEY_MAN_TRY_UPDATE = Ids.build(KEY_MANANAGER_PREFIX, TRY_UPDATE, Random.name());
    static final String KEY_MAN_DELETE = Ids.build(KEY_MANANAGER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address keyManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KEY_MANAGER, name);
    }

    // -------------- provider-loader

    static final String PROV_LOAD_CREATE = Ids.build(PROVIDER_LOADER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String PROV_LOAD_UPDATE = Ids.build(PROVIDER_LOADER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String PROV_LOAD_UPDATE2 = Ids.build("prov-load-2", CrudConstants.UPDATE, Random.name());
    static final String PROV_LOAD_UPDATE3 = Ids.build("prov-load-3", CrudConstants.UPDATE, Random.name());
    static final String PROV_LOAD_DELETE = Ids.build(PROVIDER_LOADER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address providerLoaderAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_LOADER, name);
    }

    // -------------- server-ssl-context

    static final String SRV_SSL_CREATE = Ids.build(SEREVR_SSL_CONTEXT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SRV_SSL_UPDATE = Ids.build(SEREVR_SSL_CONTEXT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SRV_SSL_DELETE = Ids.build(SEREVR_SSL_CONTEXT_PREFIX, CrudConstants.DELETE, Random.name());

    static Address serverSslContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER_SSL_CONTEXT, name);
    }

    // -------------- security-domain

    static final String SEC_DOM_CREATE = Ids.build(SECURITY_DOMAIN_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SEC_DOM_UPDATE = Ids.build(SECURITY_DOMAIN_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SEC_DOM_UPDATE2 = Ids.build("sec-dom-2", CrudConstants.UPDATE, Random.name());
    static final String SEC_DOM_UPDATE3 = Ids.build("sec-dom-3", CrudConstants.UPDATE, Random.name());
    static final String SEC_DOM_DELETE = Ids.build(SECURITY_DOMAIN_PREFIX, CrudConstants.DELETE, Random.name());

    static Address securityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SECURITY_DOMAIN, name);
    }

    // -------------- trust-manager

    static final String TRU_MAN_CREATE = Ids.build(TRUST_MANAGER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String TRU_MAN_UPDATE = Ids.build(TRUST_MANAGER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String TRU_MAN_DELETE = Ids.build(TRUST_MANAGER_PREFIX, CrudConstants.DELETE, Random.name());

    static final String TRU_MAN_CRL_CRT = Ids.build(TRUST_MANAGER_CRL_PREFIX, CrudConstants.CREATE, Random.name());
    static final String TRU_MAN_CRL_UPD = Ids.build(TRUST_MANAGER_CRL_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String TRU_MAN_CRL_DEL = Ids.build(TRUST_MANAGER_CRL_PREFIX, CrudConstants.DELETE, Random.name());

    static Address trustManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(TRUST_MANAGER, name);
    }

    // -------------- authentication-configuration

    static final String AUT_CF_CREATE = Ids.build(AUTHENTICATION_CONFIGURATION_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AUT_CF_UPDATE = Ids.build(AUTHENTICATION_CONFIGURATION_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AUT_CF_DELETE = Ids.build(AUTHENTICATION_CONFIGURATION_PREFIX, CrudConstants.DELETE,
            Random.name());

    static final String AUT_CF_CR_CRT = Ids.build(AUTHENTICATION_CONFIGURATION_CR_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AUT_CF_CR_UPD = Ids.build(AUTHENTICATION_CONFIGURATION_CR_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AUT_CF_CR_DEL = Ids.build(AUTHENTICATION_CONFIGURATION_CR_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address authenticationConfigurationAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AUTHENTICATION_CONFIGURATION, name);
    }

    // -------------- authentication-context

    static final String AUT_CT_CREATE = Ids.build(AUTHENTICATION_CONTEXT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String AUT_CT_UPDATE = Ids.build(AUTHENTICATION_CONTEXT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String AUT_CT_UPDATE2 = Ids.build("auth-ct-2", CrudConstants.UPDATE, Random.name());
    static final String AUT_CT_DELETE = Ids.build(AUTHENTICATION_CONTEXT_PREFIX, CrudConstants.DELETE, Random.name());

    // authentication-context match-rules
    static final String AUT_CT_MR_CREATE = Ids.build(AUTHENTICATION_CONTEXT_MATCH_RULE_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AUT_CT_MR_UPDATE = Ids.build(AUTHENTICATION_CONTEXT_MATCH_RULE_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AUT_CT_MR_DELETE = Ids.build(AUTHENTICATION_CONTEXT_MATCH_RULE_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address authenticationContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AUTHENTICATION_CONTEXT, name);
    }

    // -------------- file-audit-log

    static final String FILE_LOG_CREATE = Ids.build(FILE_AUDIT_LOG_PREFIX, CrudConstants.CREATE, Random.name());
    static final String FILE_LOG_UPDATE = Ids.build(FILE_AUDIT_LOG_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String FILE_LOG_TRY_UPDATE = Ids.build(FILE_AUDIT_LOG_PREFIX, TRY_UPDATE, Random.name());
    static final String FILE_LOG_DELETE = Ids.build(FILE_AUDIT_LOG_PREFIX, CrudConstants.DELETE, Random.name());

    static Address fileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILE_AUDIT_LOG, name);
    }

    // -------------- periodic-rotating-file-audit-log

    static final String PER_LOG_CREATE = Ids.build(PERIODIC_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String PER_LOG_UPDATE = Ids.build(PERIODIC_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String PER_LOG_TRY_UPDATE = Ids.build(PERIODIC_ROTATING_FILE_AUDIT_LOG_PREFIX, TRY_UPDATE,
            Random.name());
    static final String PER_LOG_DELETE = Ids.build(PERIODIC_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address periodicRotatingFileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_AUDIT_LOG, name);
    }

    // -------------- size-rotating-file-audit-log

    static final String SIZ_LOG_CREATE = Ids.build(SIZE_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String SIZ_LOG_UPDATE = Ids.build(SIZE_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String SIZ_LOG_DELETE = Ids.build(SIZE_ROTATING_FILE_AUDIT_LOG_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address sizeRotatingFileAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIZE_ROTATING_FILE_AUDIT_LOG, name);
    }

    // -------------- syslog-audit-log

    static final String SYS_LOG_CREATE = Ids.build(SYSLOG_AUDIT_LOG_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SYS_LOG_UPDATE = Ids.build(SYSLOG_AUDIT_LOG_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SYS_LOG_TRY_UPDATE = Ids.build(SYSLOG_AUDIT_LOG_PREFIX, TRY_UPDATE, Random.name());
    static final String SYS_LOG_DELETE = Ids.build(SYSLOG_AUDIT_LOG_PREFIX, CrudConstants.DELETE, Random.name());

    static Address syslogAuditLogAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SYSLOG_AUDIT_LOG, name);
    }

    // -------------- aggregate-security-event-listener

    static final String AGG_SEC_CREATE = Ids.build(AGGREGATOR_SECURITY_EVENT_LISTENER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String AGG_SEC_UPDATE = Ids.build(AGGREGATOR_SECURITY_EVENT_LISTENER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String AGG_SEC_DELETE = Ids.build(AGGREGATOR_SECURITY_EVENT_LISTENER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address aggregateSecurityEventListenerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_SECURITY_EVENT_LISTENER, name);
    }

    // -------------- policy

    static final String POL_CREATE = Ids.build("pol", CrudConstants.CREATE, Random.name());

    static Address policyAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(POLICY, name);
    }

    // -------------- dir-context

    static final String DIR_CREATE = Ids.build(DIR_CONTEXT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String DIR_UPDATE = Ids.build(DIR_CONTEXT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String DIR_DELETE = Ids.build(DIR_CONTEXT_PREFIX, CrudConstants.DELETE, Random.name());

    static final String DIR_CR_CRT = Ids.build(DIR_CONTEXT_CR_PREFIX, CrudConstants.CREATE, Random.name());
    static final String DIR_CR_UPD = Ids.build(DIR_CONTEXT_CR_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String DIR_CR_DEL = Ids.build(DIR_CONTEXT_CR_PREFIX, CrudConstants.DELETE, Random.name());

    static Address dirContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(DIR_CONTEXT, name);
    }

    // -------------- aggregate-realm

    static final String AGG_RLM_CREATE = Ids.build(AGGREGATE_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String AGG_RLM_UPDATE = Ids.build(AGGREGATE_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String AGG_RLM_DELETE = Ids.build(AGGREGATE_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address aggregateRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_REALM, name);
    }

    // -------------- caching-realm

    static final String CAC_RLM_CREATE = Ids.build(CACHING_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CAC_RLM_UPDATE = Ids.build(CACHING_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CAC_RLM_DELETE = Ids.build(CACHING_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address cachingRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CACHING_REALM, name);
    }

    // -------------- custom-modifiable-realm

    static final String CMO_RLM_CREATE = Ids.build(CUSTOM_MODIFIABLE_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CMO_RLM_UPDATE = Ids.build(CUSTOM_MODIFIABLE_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CMO_RLM_DELETE = Ids.build(CUSTOM_MODIFIABLE_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address customModifiableRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_MODIFIABLE_REALM, name);
    }

    // -------------- custom-realm

    static final String CST_RLM_CREATE = Ids.build(CUSTOM_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CST_RLM_UPDATE = Ids.build(CUSTOM_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CST_RLM_DELETE = Ids.build(CUSTOM_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address customRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_REALM, name);
    }

    // -------------- filesystem realm

    static final String FILESYS_RLM_CREATE = Ids.build(FILESYSTEM_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String FILESYS_RLM_UPDATE = Ids.build(FILESYSTEM_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String FILESYS_RLM_UPDATE2 = Ids.build("filesys-rlm-2", CrudConstants.UPDATE, Random.name());
    static final String FILESYS_RLM_UPDATE3 = Ids.build("filesys-rlm-3", CrudConstants.UPDATE, Random.name());
    static final String FILESYS_RLM_DELETE = Ids.build(FILESYSTEM_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address filesystemRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILESYSTEM_REALM, name);
    }

    // -------------- identity realm

    static final String IDEN_RLM_CREATE = Ids.build(IDENTITY_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String IDEN_RLM_UPDATE = Ids.build(IDENTITY_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String IDEN_RLM_DELETE = Ids.build(IDENTITY_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address identityRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(IDENTITY_REALM, name);
    }

    // -------------- jdbc realm

    static final String JDBC_RLM_CREATE = Ids.build(JDBC_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String JDBC_RLM_UPDATE = Ids.build(JDBC_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String JDBC_RLM_DELETE = Ids.build(JDBC_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_CREATE = Ids.build(SQL_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_UPDATE = Ids.build(SQL_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_DELETE = Ids.build(SQL_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_UPDATE2 = Ids.build("sql-2", CrudConstants.UPDATE, Random.name());

    static final String SQL_CPM_CRT = Ids.build(SQL_CPM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_CPM_UPD = Ids.build(SQL_CPM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_CPM_DEL = Ids.build(SQL_CPM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_BCM_CRT = Ids.build(SQL_BCM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_BCM_UPD = Ids.build(SQL_BCM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_BCM_DEL = Ids.build(SQL_BCM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_SSDM_CRT = Ids.build(SQL_SSDM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_SSDM_UPD = Ids.build(SQL_SSDM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_SSDM_DEL = Ids.build(SQL_SSDM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_SDM_CRT = Ids.build(SQL_SDM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_SDM_UPD = Ids.build(SQL_SDM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_SDM_DEL = Ids.build(SQL_SDM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String SQL_SM_CRT = Ids.build(SQL_SM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SQL_SM_UPD = Ids.build(SQL_SM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SQL_SM_DEL = Ids.build(SQL_SM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address jdbcRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(JDBC_REALM, name);
    }

    //  datasource-address
    static final String JDBC_PQ_DS = Ids.build("ds", Random.name());

    static Address datasourceAddress(String name) {
        return Address.subsystem(DATASOURCES).and(DATA_SOURCE, name);
    }

    // -------------- keystore realm

    static final String KS_RLM_CREATE = Ids.build(KEY_STORE_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String KS_RLM_UPDATE = Ids.build(KEY_STORE_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String KS_RLM_DELETE = Ids.build(KEY_STORE_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address keystoreRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(KEY_STORE_REALM, name);
    }

    // -------------- ldap realm

    static final String LDAP_RLM_CREATE = Ids.build(LDAP_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String LDAP_RLM_UPDATE = Ids.build(LDAP_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String LDAP_RLM_DELETE = Ids.build(LDAP_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String LDAP_RLM_USER_MAPPER_CRT = Ids.build(LDAP_REALM_USER_MAP_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String LDAP_RLM_USER_MAPPER_UPD = Ids.build(LDAP_REALM_USER_MAP_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String LDAP_RLM_USER_MAPPER_DEL = Ids.build(LDAP_REALM_USER_MAP_PREFIX, CrudConstants.DELETE,
            Random.name());
    static final String LDAP_RLM_OTP_MAPPER_CRT = Ids.build(LDAP_REALM_OTP_MAP_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String LDAP_RLM_OTP_MAPPER_UPD = Ids.build(LDAP_REALM_OTP_MAP_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String LDAP_RLM_OTP_MAPPER_DEL = Ids.build(LDAP_REALM_OTP_MAP_PREFIX, CrudConstants.DELETE,
            Random.name());
    static final String LDAP_RLM_X509_MAPPER_CRT = Ids.build(LDAP_REALM_X509_MAP_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String LDAP_RLM_X509_MAPPER_UPD = Ids.build(LDAP_REALM_X509_MAP_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String LDAP_RLM_X509_MAPPER_DEL = Ids.build(LDAP_REALM_X509_MAP_PREFIX, CrudConstants.DELETE,
            Random.name());

    static final String LDAP_RLM_AM_CRT = Ids.build(LDAP_REALM_AM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String LDAP_RLM_AM_UPD = Ids.build(LDAP_REALM_AM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String LDAP_RLM_AM_DEL = Ids.build(LDAP_REALM_AM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String LDAP_RLM_AM_FROM_UPD = Ids.build(LDAP_REALM_AM_FROM_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String LDAP_RLM_AM_FROM_DEL = Ids.build(LDAP_REALM_AM_FROM_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address ldapRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LDAP_REALM, name);
    }

    // -------------- properties realm

    static final String PROP_RLM_CREATE = Ids.build(PROPERTIES_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String PROP_RLM_UPDATE = Ids.build(PROPERTIES_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String PROP_RLM_DELETE = Ids.build(PROPERTIES_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String PROP_RLM_GP_ADD = Ids.build(PROPERTIES_REALM_GP_PREFIX, "add", Random.name());
    static final String PROP_RLM_GP_UPD = Ids.build(PROPERTIES_REALM_GP_PREFIX, "upd", Random.name());
    static final String PROP_RLM_GP_DEL = Ids.build(PROPERTIES_REALM_GP_PREFIX, "del", Random.name());

    static Address propertiesRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROPERTIES_REALM, name);
    }

    // -------------- token realm

    static final String TKN_RLM_CREATE = Ids.build(TOKEN_REALM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String TKN_RLM_UPDATE = Ids.build(TOKEN_REALM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String TKN_RLM_DELETE = Ids.build(TOKEN_REALM_PREFIX, CrudConstants.DELETE, Random.name());

    static final String TKN_RLM_JWT_CRT = Ids.build(TOKEN_REALM_JWT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String TKN_RLM_JWT_UPD = Ids.build(TOKEN_REALM_JWT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String TKN_RLM_JWT_DEL = Ids.build(TOKEN_REALM_JWT_PREFIX, CrudConstants.DELETE, Random.name());

    static final String TKN_RLM_OAU_CRT = Ids.build(TOKEN_REALM_OAU_PREFIX, CrudConstants.CREATE, Random.name());
    static final String TKN_RLM_OAU_UPD = Ids.build(TOKEN_REALM_OAU_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String TKN_RLM_OAU_DEL = Ids.build(TOKEN_REALM_OAU_PREFIX, CrudConstants.DELETE, Random.name());

    static Address tokenRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(TOKEN_REALM, name);
    }

    // -------------- constant realm-mapper

    static final String CON_RM_CREATE = Ids.build(CONSTANT_REALM_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CON_RM_UPDATE = Ids.build(CONSTANT_REALM_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CON_RM_DELETE = Ids.build(CONSTANT_REALM_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address constantRealmMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSTANT_REALM_MAPPER, name);
    }

    // -------------- custom realm-mapper

    static final String CUS_RM_CREATE = Ids.build(CUSTOM_REALM_MAPPER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CUS_RM_UPDATE = Ids.build(CUSTOM_REALM_MAPPER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CUS_RM_DELETE = Ids.build(CUSTOM_REALM_MAPPER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address customRealmMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_REALM_MAPPER, name);
    }

    // -------------- mapped-regex-realm-mapper

    static final String MAPP_RM_CREATE = Ids.build(MAPPED_REGEX_REALM_MAPPER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String MAPP_RM_UPDATE = Ids.build(MAPPED_REGEX_REALM_MAPPER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String MAPP_RM_DELETE = Ids.build(MAPPED_REGEX_REALM_MAPPER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address mappedRegexRealmMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MAPPED_REGEX_REALM_MAPPER, name);
    }

    // -------------- simple-regex-realm-mapper

    static final String SIMP_RM_CREATE = Ids.build(SIMPLE_REGEX_REALM_MAPPER_PREFIX, CrudConstants.CREATE,
            Random.name());
    static final String SIMP_RM_UPDATE = Ids.build(SIMPLE_REGEX_REALM_MAPPER_PREFIX, CrudConstants.UPDATE,
            Random.name());
    static final String SIMP_RM_DELETE = Ids.build(SIMPLE_REGEX_REALM_MAPPER_PREFIX, CrudConstants.DELETE,
            Random.name());

    static Address simpleRegexRealmMapperAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIMPLE_REGEX_REALM_MAPPER, name);
    }

    private ElytronFixtures() {
    }

}
