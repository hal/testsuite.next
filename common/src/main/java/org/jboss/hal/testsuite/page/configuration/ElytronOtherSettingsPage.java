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
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_TEMPLATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALMS;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_OTHER)
public class ElytronOtherSettingsPage extends BasePage {

    // Credential Store
    @FindBy(id = ELYTRON_CREDENTIAL_STORE + "-" + TABLE + WRAPPER) private TableFragment credentialStoreTable;
    @FindBy(id = ELYTRON_CREDENTIAL_STORE + "-" + FORM) private FormFragment credentialStoreForm;
    @FindBy(id = ELYTRON_CREDENTIAL_STORE + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment credentialStoreCredentialReferenceForm;
    @FindBy(id = ELYTRON_CREDENTIAL_STORE + "-" + TAB_CONTAINER) private TabsFragment credentialStoreTab;

    // Filtering Key Store
    @FindBy(id = ELYTRON_FILTERING_KEY_STORE + "-" + TABLE + WRAPPER) private TableFragment filteringKeyStoreTable;
    @FindBy(id = ELYTRON_FILTERING_KEY_STORE + "-" + FORM) private FormFragment filteringKeyStoreForm;

    // Key Store
    @FindBy(id = ELYTRON_KEY_STORE + "-" + TABLE + WRAPPER) private TableFragment keyStoreTable;
    @FindBy(id = ELYTRON_KEY_STORE + "-" + FORM) private FormFragment keyStoreForm;
    @FindBy(id = ELYTRON_KEY_STORE + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment keyStoreCredentialReferenceForm;
    @FindBy(id = ELYTRON_KEY_STORE + "-" + TAB_CONTAINER) private TabsFragment keyStoreTab;

    // LDAP Key Store
    @FindBy(id = ELYTRON_LDAP_KEY_STORE + "-" + TABLE + WRAPPER) private TableFragment ldapKeyStoreTable;
    @FindBy(id = ELYTRON_LDAP_KEY_STORE + "-" + FORM) private FormFragment ldapKeyStoreForm;
    @FindBy(id = ELYTRON_LDAP_KEY_STORE + "-" + NEW_ITEM_TEMPLATE + "-" + FORM) private FormFragment ldapKeyStoreNewItemTemplateForm;
    @FindBy(id = ELYTRON_LDAP_KEY_STORE + "-" + TAB_CONTAINER) private TabsFragment ldapKeyStoreTab;

    // Aggregate Providers
    @FindBy(id = ELYTRON_AGGREGATE_PROVIDERS + "-" + TABLE + WRAPPER) private TableFragment aggregateProvidersTable;
    @FindBy(id = ELYTRON_AGGREGATE_PROVIDERS + "-" + FORM) private FormFragment aggregateProvidersForm;

    // Client SSL Context
    @FindBy(id = ELYTRON_CLIENT_SSL_CONTEXT + "-" + TABLE + WRAPPER) private TableFragment clientSslContextTable;
    @FindBy(id = ELYTRON_CLIENT_SSL_CONTEXT + "-" + FORM) private FormFragment clientSslContextForm;

    // Key Manager
    @FindBy(id = ELYTRON_KEY_MANAGER + "-" + TABLE + WRAPPER) private TableFragment keyManagerTable;
    @FindBy(id = ELYTRON_KEY_MANAGER + "-" + FORM) private FormFragment keyManagerForm;
    @FindBy(id = ELYTRON_KEY_MANAGER + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment keyManagerCredentialReferenceForm;
    @FindBy(id = ELYTRON_KEY_MANAGER + "-" + TAB_CONTAINER) private TabsFragment keyManagerTab;

    // Provider Loader
    @FindBy(id = ELYTRON_PROVIDER_LOADER + "-" + TABLE + WRAPPER) private TableFragment providerLoaderTable;
    @FindBy(id = ELYTRON_PROVIDER_LOADER + "-" + FORM) private FormFragment providerLoaderForm;

    // Server SSL Context
    @FindBy(id = ELYTRON_SERVER_SSL_CONTEXT + "-" + TABLE + WRAPPER) private TableFragment serverSslContextTable;
    @FindBy(id = ELYTRON_SERVER_SSL_CONTEXT + "-" + FORM) private FormFragment serverSslContextForm;

    // Security Domain
    @FindBy(id = ELYTRON_SECURITY_DOMAIN + "-" + TABLE + WRAPPER) private TableFragment securityDomainTable;
    @FindBy(id = ELYTRON_SECURITY_DOMAIN + "-" + FORM) private FormFragment securityDomainForm;

    // Security Domain / Realms
    @FindBy(id = ELYTRON_SECURITY_DOMAIN + "-" + REALMS + "-" + TABLE + WRAPPER) private TableFragment securityDomainRealmsTable;
    @FindBy(id = ELYTRON_SECURITY_DOMAIN + "-" + REALMS + "-" + FORM) private FormFragment securityDomainRealmsForm;

    // Trust Manager
    @FindBy(id = ELYTRON_TRUST_MANAGER + "-" + TABLE + WRAPPER) private TableFragment trustManagerTable;
    @FindBy(id = ELYTRON_TRUST_MANAGER + "-" + FORM) private FormFragment trustManagerForm;
    @FindBy(id = ELYTRON_TRUST_MANAGER + "-certificate-revocation-list-" + FORM) private FormFragment trustManagerCertificateRevocationListForm;
    @FindBy(id = ELYTRON_TRUST_MANAGER + "-" + TAB_CONTAINER) private TabsFragment trustManagerTab;

    // Authentication Configuration
    @FindBy(id = ELYTRON_AUTHENTICATION_CONFIGURATION + "-" + TABLE + WRAPPER) private TableFragment authenticationConfigurationTable;
    @FindBy(id = ELYTRON_AUTHENTICATION_CONFIGURATION + "-" + TAB_CONTAINER) private TabsFragment authenticationConfigurationTabs;
    @FindBy(id = ELYTRON_AUTHENTICATION_CONFIGURATION + "-" + FORM) private FormFragment authenticationConfigurationForm;
    @FindBy(id = ELYTRON_AUTHENTICATION_CONFIGURATION + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment authConfigCredentialReferenceForm;

    // Authentication Context
    @FindBy(id = ELYTRON_AUTHENTICATION_CONTEXT + "-" + TABLE + WRAPPER) private TableFragment authenticationContextTable;
    @FindBy(id = ELYTRON_AUTHENTICATION_CONTEXT + "-" + FORM) private FormFragment authenticationContextForm;

    // Authentication Context / Match Rules
    @FindBy(id = ELYTRON_AUTHENTICATION_CONTEXT + "-match-rules-" + TABLE + WRAPPER) private TableFragment authenticationContextMatchRulesTable;
    @FindBy(id = ELYTRON_AUTHENTICATION_CONTEXT + "-match-rules-" + FORM) private FormFragment authenticationContextMatchRulesForm;

    // File Audit Log
    @FindBy(id = ELYTRON_FILE_AUDIT_LOG + "-" + TABLE + WRAPPER) private TableFragment fileAuditLogTable;
    @FindBy(id = ELYTRON_FILE_AUDIT_LOG + "-" + FORM) private FormFragment fileAuditLogForm;

    // Periodic Rotating File Audit Log
    @FindBy(id = ELYTRON_PERIODIC_ROTATING_FILE_AUDIT_LOG + "-" + TABLE + WRAPPER) private TableFragment periodicRotatingFileAuditLogTable;
    @FindBy(id = ELYTRON_PERIODIC_ROTATING_FILE_AUDIT_LOG + "-" + FORM) private FormFragment periodicRotatingFileAuditLogForm;

    // Size Rotating File Audit Log
    @FindBy(id = ELYTRON_SIZE_ROTATING_FILE_AUDIT_LOG + "-" + TABLE + WRAPPER) private TableFragment sizeRotatingFileAuditLogTable;
    @FindBy(id = ELYTRON_SIZE_ROTATING_FILE_AUDIT_LOG + "-" + FORM) private FormFragment sizeRotatingFileAuditLogForm;

    // Syslog Audit Log
    @FindBy(id = ELYTRON_SYSLOG_AUDIT_LOG + "-" + TABLE + WRAPPER) private TableFragment syslogAuditLogTable;
    @FindBy(id = ELYTRON_SYSLOG_AUDIT_LOG + "-" + FORM) private FormFragment syslogAuditLogForm;

    // Aggregate Security Event Listener
    @FindBy(id = ELYTRON_AGGREGATE_SECURITY_EVENT_LISTENER + "-" + TABLE + WRAPPER) private TableFragment aggregateSecurityEventListenerTable;
    @FindBy(id = ELYTRON_AGGREGATE_SECURITY_EVENT_LISTENER + "-" + FORM) private FormFragment aggregateSecurityEventListenerForm;

    // Policy
    @FindBy(id = ELYTRON_JACC_POLICY_FORM) private FormFragment policyJaccForm;
    @FindBy(id = ELYTRON_CUSTOM_POLICY_FORM) private FormFragment policyCustomForm;
    @FindBy(id = ELYTRON_CUSTOM_POLICY_EMPTY) private EmptyState emptyPolicy;

    // Dir Context
    @FindBy(id = ELYTRON_DIR_CONTEXT + "-" + TABLE + WRAPPER) private TableFragment dirContextTable;
    @FindBy(id = ELYTRON_DIR_CONTEXT + "-" + FORM) private FormFragment dirContextForm;
    @FindBy(id = ELYTRON_DIR_CONTEXT + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment dirContextCredentialReferenceForm;
    @FindBy(id = ELYTRON_DIR_CONTEXT + "-" + TAB_CONTAINER) private TabsFragment dirContextTabs;

    public TableFragment getCredentialStoreTable() {
        return credentialStoreTable;
    }

    public FormFragment getCredentialStoreForm() {
        return credentialStoreForm;
    }

    public FormFragment getCredentialStoreCredentialReferenceForm() {
        return credentialStoreCredentialReferenceForm;
    }

    public TabsFragment getCredentialStoreTab() {
        return credentialStoreTab;
    }

    public TableFragment getFilteringKeyStoreTable() {
        return filteringKeyStoreTable;
    }

    public FormFragment getFilteringKeyStoreForm() {
        return filteringKeyStoreForm;
    }

    public FormFragment getKeyStoreCredentialReferenceForm() {
        return keyStoreCredentialReferenceForm;
    }

    public TableFragment getKeyStoreTable() {
        return keyStoreTable;
    }

    public FormFragment getKeyStoreForm() {
        return keyStoreForm;
    }

    public TabsFragment getKeyStoreTab() {
        return keyStoreTab;
    }

    public TableFragment getLdapKeyStoreTable() {
        return ldapKeyStoreTable;
    }

    public FormFragment getLdapKeyStoreForm() {
        return ldapKeyStoreForm;
    }

    public FormFragment getLdapKeyStoreNewItemTemplateForm() {
        return ldapKeyStoreNewItemTemplateForm;
    }

    public TabsFragment getLdapKeyStoreTab() {
        return ldapKeyStoreTab;
    }

    public TableFragment getAggregateProvidersTable() {
        return aggregateProvidersTable;
    }

    public FormFragment getAggregateProvidersForm() {
        return aggregateProvidersForm;
    }

    public TableFragment getClientSslContextTable() {
        return clientSslContextTable;
    }

    public FormFragment getClientSslContextForm() {
        return clientSslContextForm;
    }

    public TableFragment getKeyManagerTable() {
        return keyManagerTable;
    }

    public FormFragment getKeyManagerForm() {
        return keyManagerForm;
    }

    public FormFragment getKeyManagerCredentialReferenceForm() {
        return keyManagerCredentialReferenceForm;
    }

    public TabsFragment getKeyManagerTab() {
        return keyManagerTab;
    }

    public TableFragment getProviderLoaderTable() {
        return providerLoaderTable;
    }

    public FormFragment getProviderLoaderForm() {
        return providerLoaderForm;
    }

    public TableFragment getServerSslContextTable() {
        return serverSslContextTable;
    }

    public FormFragment getServerSslContextForm() {
        return serverSslContextForm;
    }

    public TableFragment getSecurityDomainTable() {
        return securityDomainTable;
    }

    public FormFragment getSecurityDomainForm() {
        return securityDomainForm;
    }

    public TableFragment getSecurityDomainRealmsTable() {
        return securityDomainRealmsTable;
    }

    public FormFragment getSecurityDomainRealmsForm() {
        return securityDomainRealmsForm;
    }

    public TableFragment getTrustManagerTable() {
        return trustManagerTable;
    }

    public FormFragment getTrustManagerForm() {
        return trustManagerForm;
    }

    public FormFragment getTrustManagerCertificateRevocationListForm() {
        return trustManagerCertificateRevocationListForm;
    }

    public TabsFragment getTrustManagerTab() {
        return trustManagerTab;
    }

    public TableFragment getAuthenticationConfigurationTable() {
        return authenticationConfigurationTable;
    }

    public FormFragment getAuthenticationConfigurationForm() {
        return authenticationConfigurationForm;
    }

    public TabsFragment getAuthenticationConfigurationTabs() {
        return authenticationConfigurationTabs;
    }

    public FormFragment getAuthConfigCredentialReferenceForm() {
        return authConfigCredentialReferenceForm;
    }

    public TableFragment getAuthenticationContextTable() {
        return authenticationContextTable;
    }

    public FormFragment getAuthenticationContextForm() {
        return authenticationContextForm;
    }

    public TableFragment getAuthenticationContextMatchRulesTable() {
        return authenticationContextMatchRulesTable;
    }

    public FormFragment getAuthenticationContextMatchRulesForm() {
        return authenticationContextMatchRulesForm;
    }

    public TableFragment getFileAuditLogTable() {
        return fileAuditLogTable;
    }

    public FormFragment getFileAuditLogForm() {
        return fileAuditLogForm;
    }

    public TableFragment getPeriodicRotatingFileAuditLogTable() {
        return periodicRotatingFileAuditLogTable;
    }

    public FormFragment getPeriodicRotatingFileAuditLogForm() {
        return periodicRotatingFileAuditLogForm;
    }

    public TableFragment getSizeRotatingFileAuditLogTable() {
        return sizeRotatingFileAuditLogTable;
    }

    public FormFragment getSizeRotatingFileAuditLogForm() {
        return sizeRotatingFileAuditLogForm;
    }

    public TableFragment getSyslogAuditLogTable() {
        return syslogAuditLogTable;
    }

    public FormFragment getSyslogAuditLogForm() {
        return syslogAuditLogForm;
    }

    public TableFragment getAggregateSecurityEventListenerTable() {
        return aggregateSecurityEventListenerTable;
    }

    public FormFragment getAggregateSecurityEventListenerForm() {
        return aggregateSecurityEventListenerForm;
    }

    public FormFragment getPolicyJaccForm() {
        return policyJaccForm;
    }

    public FormFragment getPolicyCustomForm() {
        return policyCustomForm;
    }

    public EmptyState getEmptyPolicy() {
        return emptyPolicy;
    }

    public TableFragment getDirContextTable() {
        return dirContextTable;
    }

    public FormFragment getDirContextForm() {
        return dirContextForm;
    }

    public FormFragment getDirContextCredentialReferenceForm() {
        return dirContextCredentialReferenceForm;
    }

    public TabsFragment getDirContextTabs() {
        return dirContextTabs;
    }
}