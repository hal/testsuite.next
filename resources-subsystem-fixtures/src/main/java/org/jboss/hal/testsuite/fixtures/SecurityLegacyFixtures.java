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
package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ACL_MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CONFIGURATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LOGIN_MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MAPPING_MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.POLICY_MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRUST_MODULE;

public interface SecurityLegacyFixtures {

    String ANY_STRING = Random.name();
    String ACL = "acl";
    String AUDIT = "audit";
    String AUTHENTICATION = "authentication";
    String AUTHORIZATION = "authorization";
    String CACHE_TYPE = "cache-type";
    String CLASSIC = "classic";
    String CLIENT_AUTH = "client-auth";
    String CREATE = "create";
    String CODE = "code";
    String DELETE = "delete";
    String ELYTRON_REALM = "elytron-realm";
    String ELYTRON_TRUST_STORE = "elytron-trust-store";
    String FLAG = "flag";
    String IDENTITY_TRUST = "identity-trust";
    String INITIALIZE_JACC = "initialize-jacc";
    String JBOSS_SERVER_CONFIG_DIR = "jboss.server.config.dir";
    String KEYSTORE = "keystore";
    String LEGACY_JAAS_CONFIG = "legacy-jaas-config";
    String LEGACY_JSSE_CONFIG = "legacy-jsse-config";
    String MAPPING = "mapping";
    String SAMPLE_KEYSTORE_FILENAME = "keystore-" + ANY_STRING + ".jks";
    String SAMPLE_PASSWORD = "admin123";
    String SAMPLE_TRUSTSTORE_FILENAME = "truststore-" + ANY_STRING + ".jks";
    String TRUSTSTORE = "truststore";
    String UPDATE = "update";
    String VAULT = "vault";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(SECURITY);

    String CONFIGURATION_ITEM = Ids.build(SECURITY, CONFIGURATION, Ids.ITEM);
    String KEY_MANAGER_ITEM = Ids.build(SECURITY, Ids.ELYTRON_KEY_MANAGER, Ids.ITEM);
    String KEY_STORE_ITEM = Ids.build(SECURITY, Ids.ELYTRON_KEY_STORE, Ids.ITEM);
    String REALM_ITEM = Ids.build(SECURITY, ELYTRON_REALM, Ids.ITEM);
    String TRUST_MANAGER_ITEM = Ids.build(SECURITY, Ids.ELYTRON_TRUST_MANAGER, Ids.ITEM);
    String TRUST_STORE_ITEM = Ids.build(SECURITY, ELYTRON_TRUST_STORE, Ids.ITEM);
    String VAULT_ITEM = Ids.build(SECURITY, VAULT, Ids.ITEM);

    String SEC_DOMAIN_CONFIGURATION_ITEM = "security-domain-configuration-item";
    String SEC_DOMAIN_AUTHENTICATION_ITEM = "security-domain-authentication-item";
    String SEC_DOMAIN_AUTHORIZATION_ITEM = "security-domain-authorization-item";
    String SEC_DOMAIN_AUDIT_ITEM = "security-domain-audit-item";
    String SEC_DOMAIN_ACL_ITEM = "security-domain-acl-item";
    String SEC_DOMAIN_MAPPING_ITEM = "security-domain-mapping-item";
    String SEC_DOMAIN_IDENTITY_TRUST_ITEM = "security-domain-trust-item";


    // -------------- elytron-key-manager

    String EKM_CREATE = Ids.build("ekm", CREATE, Random.name());
    String EKM_UPDATE = Ids.build("ekm", "update", Random.name());
    String EKM_DELETE = Ids.build("ekm", "delete", Random.name());

    static Address elytronKeyManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(Ids.ELYTRON_KEY_MANAGER, name);
    }

    // -------------- elytron-key-store

    String EKS_CREATE = Ids.build("eks", CREATE, Random.name());
    String EKS_UPDATE = Ids.build("eks", UPDATE, Random.name());
    String EKS_DELETE = Ids.build("eks", DELETE, Random.name());

    static Address elytronKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(Ids.ELYTRON_KEY_STORE, name);
    }

    // -------------- elytron-realm

    String REALM_CREATE = Ids.build("realm", CREATE, Random.name());
    String REALM_UPDATE = Ids.build("realm", UPDATE, Random.name());
    String REALM_DELETE = Ids.build("realm", DELETE, Random.name());

    static Address elytronRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_REALM, name);
    }

    // -------------- elytron-trust-manager

    String ETM_CREATE = Ids.build("etm", CREATE, Random.name());
    String ETM_UPDATE = Ids.build("etm", UPDATE, Random.name());
    String ETM_DELETE = Ids.build("etm", DELETE, Random.name());

    static Address elytronTrustManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(Ids.ELYTRON_TRUST_MANAGER, name);
    }

    // -------------- elytron-trust-store

    String ETS_CREATE = Ids.build("ets", CREATE, Random.name());
    String ETS_UPDATE = Ids.build("ets", UPDATE, Random.name());
    String ETS_DELETE = Ids.build("ets", DELETE, Random.name());

    static Address elytronTrustStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_TRUST_STORE, name);
    }

    // -------------- security-domain

    static Address vaultAddress() {
        return SUBSYSTEM_ADDRESS.and(VAULT, CLASSIC);
    }

    // -------------- security-domain

    String SEC_DOM_CREATE = Ids.build(SECURITY_DOMAIN, CREATE, Random.name());
    String SEC_DOM_READ = Ids.build(SECURITY_DOMAIN, "read", Random.name());
    String SEC_DOM_UPDATE = Ids.build(SECURITY_DOMAIN, UPDATE, Random.name());
    String SEC_DOM_DELETE = Ids.build(SECURITY_DOMAIN, DELETE, Random.name());

    static Address securityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SECURITY_DOMAIN, name);
    }

    // -------------- security-domain / jsse

    static Address securityDomainJSSEAddress(String name) {
        return securityDomainAddress(name).and("jsse", "classic");
    }

    // -------------- security-domain / authentication

    String AUTHENT_CREATE = Ids.build("authent", CREATE, Random.name());
    String AUTHENT_UPDATE = Ids.build("authent", UPDATE, Random.name());
    String AUTHENT_DELETE = Ids.build("authent", DELETE, Random.name());

    static Address authenticationLoginModuleAddress(String securityDomain, String loginModule) {
        return securityDomainAddress(securityDomain).and(AUTHENTICATION, CLASSIC).and(LOGIN_MODULE, loginModule);
    }

    // -------------- security-domain / authorization

    String AUTHOR_CREATE = Ids.build("author", CREATE, Random.name());
    String AUTHOR_UPDATE = Ids.build("author", UPDATE, Random.name());
    String AUTHOR_DELETE = Ids.build("author", DELETE, Random.name());

    static Address authorizationPolicyModuleAddress(String securityDomain, String policyModule) {
        return securityDomainAddress(securityDomain).and(AUTHORIZATION, CLASSIC).and(POLICY_MODULE, policyModule);
    }

    // -------------- security-domain / acl

    String ACL_CREATE = Ids.build(ACL, CREATE, Random.name());
    String ACL_UPDATE = Ids.build(ACL, UPDATE, Random.name());
    String ACL_DELETE = Ids.build(ACL, DELETE, Random.name());

    static Address aclModuleAddress(String securityDomain, String aclModule) {
        return securityDomainAddress(securityDomain).and(ACL, CLASSIC).and(ACL_MODULE, aclModule);
    }

    // -------------- security-domain / audit

    String AUDIT_CREATE = Ids.build(AUDIT, CREATE, Random.name());
    String AUDIT_UPDATE = Ids.build(AUDIT, UPDATE, Random.name());
    String AUDIT_DELETE = Ids.build(AUDIT, DELETE, Random.name());

    static Address auditProviderModuleAddress(String securityDomain, String providerModule) {
        return securityDomainAddress(securityDomain).and(AUDIT, CLASSIC).and(PROVIDER_MODULE, providerModule);
    }

    // -------------- security-domain / identity-trust

    String IDENT_CREATE = Ids.build("iden", CREATE, Random.name());
    String IDENT_UPDATE = Ids.build("iden", UPDATE, Random.name());
    String IDENT_DELETE = Ids.build("iden", DELETE, Random.name());

    static Address identityTrustModuleAddress(String securityDomain, String trustModule) {
        return securityDomainAddress(securityDomain).and(IDENTITY_TRUST, CLASSIC).and(TRUST_MODULE, trustModule);
    }

    // -------------- security-domain / mapping

    String MAPPING_CREATE = Ids.build(MAPPING, CREATE, Random.name());
    String MAPPING_UPDATE = Ids.build(MAPPING, UPDATE, Random.name());
    String MAPPING_DELETE = Ids.build(MAPPING, DELETE, Random.name());

    static Address mappingAddress(String securityDomain, String mappingModule) {
        return securityDomainAddress(securityDomain).and(MAPPING, CLASSIC).and(MAPPING_MODULE, mappingModule);
    }
}
