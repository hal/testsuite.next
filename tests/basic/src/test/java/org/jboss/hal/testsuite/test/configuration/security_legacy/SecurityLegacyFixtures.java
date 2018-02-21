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
package org.jboss.hal.testsuite.test.configuration.security_legacy;

import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CONFIGURATION;
import static org.jboss.hal.resources.Ids.*;

public interface SecurityLegacyFixtures {

    String ANY_STRING = Random.name();
    String SAMPLE_PASSWORD = "admin123";
    String SAMPLE_KEYSTORE_FILENAME = "keystore-" + ANY_STRING + ".jks";
    String SAMPLE_TRUSTSTORE_FILENAME = "truststore-" + ANY_STRING + ".jks";
    String JBOSS_SERVER_CONFIG_DIR = "jboss.server.config.dir";

    String ELYTRON_REALM = "elytron-realm";
    String ELYTRON_TRUST_STORE = "elytron-trust-store";
    String LEGACY_JSSE_CONFIG = "legacy-jsse-config";
    String LEGACY_JAAS_CONFIG = "legacy-jaas-config";
    String CLIENT_AUTH = "client-auth";
    String TRUSTSTORE = "truststore";
    String KEYSTORE = "keystore";
    String INITIALIZE_JACC = "initialize-jacc";
    String VAULT = "vault";
    String CLASSIC = "classic";
    String AUTHENTICATION = "authentication";
    String AUTHORIZATION = "authorization";
    String ACL = "acl";
    String AUDIT = "audit";
    String IDENTITY_TRUST = "identity-trust";
    String MAPPING = "mapping";
    String CODE = "code";
    String CACHE_TYPE = "cache-type";
    String FLAG = "flag";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(SECURITY);

    String CONFIGURATION_ITEM = SECURITY + "-" + CONFIGURATION + "-" + ITEM;
    String KEY_MANAGER_ITEM = SECURITY + "-" + ELYTRON_KEY_MANAGER + "-" + ITEM;
    String KEY_STORE_ITEM = SECURITY + "-" + ELYTRON_KEY_STORE + "-" + ITEM;
    String REALM_ITEM = SECURITY + "-elytron-realm-" + ITEM;
    String TRUST_MANAGER_ITEM = SECURITY + "-" + ELYTRON_TRUST_MANAGER + "-" + ITEM;
    String TRUST_STORE_ITEM = SECURITY + "-" + ELYTRON_TRUST_STORE + "-" + ITEM;
    String VAULT_ITEM = SECURITY + "-vault-" + ITEM;

    String SEC_DOMAIN_CONFIGURATION_ITEM = "security-domain-configuration-item";
    String SEC_DOMAIN_AUTHENTICATION_ITEM = "security-domain-authentication-item";
    String SEC_DOMAIN_AUTHORIZATION_ITEM = "security-domain-authorization-item";
    String SEC_DOMAIN_AUDIT_ITEM = "security-domain-audit-item";
    String SEC_DOMAIN_ACL_ITEM = "security-domain-acl-item";
    String SEC_DOMAIN_MAPPING_ITEM = "security-domain-mapping-item";
    String SEC_DOMAIN_IDENTITY_TRUST_ITEM = "security-domain-trust-item";


    // -------------- elytron-key-manager

    String EKM_CREATE = Ids.build("ekm", "create", Random.name());
    String EKM_UPDATE = Ids.build("ekm", "update", Random.name());
    String EKM_DELETE = Ids.build("ekm", "delete", Random.name());

    static Address elytronKeyManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_KEY_MANAGER, name);
    }

    // -------------- elytron-key-store

    String EKS_CREATE = Ids.build("eks", "create", Random.name());
    String EKS_UPDATE = Ids.build("eks", "update", Random.name());
    String EKS_DELETE = Ids.build("eks", "delete", Random.name());

    static Address elytronKeyStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_KEY_STORE, name);
    }

    // -------------- elytron-realm

    String REALM_CREATE = Ids.build("realm", "create", Random.name());
    String REALM_UPDATE = Ids.build("realm", "update", Random.name());
    String REALM_DELETE = Ids.build("realm", "delete", Random.name());

    static Address elytronRealmAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_REALM, name);
    }

    // -------------- elytron-trust-manager

    String ETM_CREATE = Ids.build("etm", "create", Random.name());
    String ETM_UPDATE = Ids.build("etm", "update", Random.name());
    String ETM_DELETE = Ids.build("etm", "delete", Random.name());

    static Address elytronTrustManagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_TRUST_MANAGER, name);
    }

    // -------------- elytron-trust-store

    String ETS_CREATE = Ids.build("ets", "create", Random.name());
    String ETS_UPDATE = Ids.build("ets", "update", Random.name());
    String ETS_DELETE = Ids.build("ets", "delete", Random.name());

    static Address elytronTrustStoreAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ELYTRON_TRUST_STORE, name);
    }

    // -------------- security-domain

    static Address vaultAddress() {
        return SUBSYSTEM_ADDRESS.and(VAULT, CLASSIC);
    }

    // -------------- security-domain

    String SEC_DOM_CREATE = Ids.build("sec-domain", "create", Random.name());
    String SEC_DOM_READ = Ids.build("sec-domain", "read", Random.name());
    String SEC_DOM_UPDATE = Ids.build("sec-domain", "update", Random.name());
    String SEC_DOM_DELETE = Ids.build("sec-domain", "delete", Random.name());

    static Address securityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ModelDescriptionConstants.SECURITY_DOMAIN, name);
    }

    // -------------- security-domain / jsse

    static Address securityDomainJSSEAddress(String name) {
        return securityDomainAddress(name).and("jsse", "classic");
    }

    // -------------- security-domain / authentication

    String AUTHENT_CREATE = Ids.build("authent", "create", Random.name());
    String AUTHENT_UPDATE = Ids.build("authent", "update", Random.name());
    String AUTHENT_DELETE = Ids.build("authent", "delete", Random.name());

    static Address authenticationLoginModuleAddress(String securityDomain, String loginModule) {
        return securityDomainAddress(securityDomain).and(AUTHENTICATION, CLASSIC).and(LOGIN_MODULE, loginModule);
    }

    // -------------- security-domain / authorization

    String AUTHOR_CREATE = Ids.build("author", "create", Random.name());
    String AUTHOR_UPDATE = Ids.build("author", "update", Random.name());
    String AUTHOR_DELETE = Ids.build("author", "delete", Random.name());

    static Address authorizationPolicyModuleAddress(String securityDomain, String policyModule) {
        return securityDomainAddress(securityDomain).and(AUTHORIZATION, CLASSIC).and(POLICY_MODULE, policyModule);
    }

    // -------------- security-domain / acl

    String ACL_CREATE = Ids.build("acl", "create", Random.name());
    String ACL_UPDATE = Ids.build("acl", "update", Random.name());
    String ACL_DELETE = Ids.build("acl", "delete", Random.name());

    static Address aclModuleAddress(String securityDomain, String aclModule) {
        return securityDomainAddress(securityDomain).and(ACL, CLASSIC).and(ACL_MODULE, aclModule);
    }

    // -------------- security-domain / audit

    String AUDIT_CREATE = Ids.build("audit", "create", Random.name());
    String AUDIT_UPDATE = Ids.build("audit", "update", Random.name());
    String AUDIT_DELETE = Ids.build("audit", "delete", Random.name());

    static Address auditProviderModuleAddress(String securityDomain, String providerModule) {
        return securityDomainAddress(securityDomain).and(AUDIT, CLASSIC).and(PROVIDER_MODULE, providerModule);
    }

    // -------------- security-domain / identity-trust

    String IDENT_CREATE = Ids.build("iden", "create", Random.name());
    String IDENT_UPDATE = Ids.build("iden", "update", Random.name());
    String IDENT_DELETE = Ids.build("iden", "delete", Random.name());

    static Address identityTrustModuleAddress(String securityDomain, String trustModule) {
        return securityDomainAddress(securityDomain).and(IDENTITY_TRUST, CLASSIC).and(TRUST_MODULE, trustModule);
    }

    // -------------- security-domain / mapping

    String MAPPING_CREATE = Ids.build("mapping", "create", Random.name());
    String MAPPING_UPDATE = Ids.build("mapping", "update", Random.name());
    String MAPPING_DELETE = Ids.build("mapping", "delete", Random.name());

    static Address mappingAddress(String securityDomain, String mappingModule) {
        return securityDomainAddress(securityDomain).and(MAPPING, CLASSIC).and(MAPPING_MODULE, mappingModule);
    }


}
