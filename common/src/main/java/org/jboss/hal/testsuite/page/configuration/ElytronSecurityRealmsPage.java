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
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.resources.Ids.ATTRIBUTES;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_SECURITY_REALMS)
public class ElytronSecurityRealmsPage extends BasePage {

    // Aggregate Realms
    @FindBy(id = ELYTRON_AGGREGATE_REALM + "-" + TABLE + WRAPPER) private TableFragment aggregateRealmTable;
    @FindBy(id = ELYTRON_AGGREGATE_REALM + "-" + FORM) private FormFragment aggregateRealmForm;

    // Caching Realm
    @FindBy(id = ELYTRON_CACHING_REALM + "-" + TABLE + WRAPPER) private TableFragment cachingRealmTable;
    @FindBy(id = ELYTRON_CACHING_REALM + "-" + FORM) private FormFragment cachingRealmForm;

    // Custom Modifiable Realm
    @FindBy(id = ELYTRON_CUSTOM_MODIFIABLE_REALM + "-" + TABLE + WRAPPER) private TableFragment customModifiableRealmTable;
    @FindBy(id = ELYTRON_CUSTOM_MODIFIABLE_REALM + "-" + FORM) private FormFragment customModifiableRealmForm;

    // Custom Realm
    @FindBy(id = ELYTRON_CUSTOM_REALM + "-" + TABLE + WRAPPER) private TableFragment customRealmTable;
    @FindBy(id = ELYTRON_CUSTOM_REALM + "-" + FORM) private FormFragment customRealmForm;

    // Filesystem Realm
    @FindBy(id = ELYTRON_FILESYSTEM_REALM + "-" + TABLE + WRAPPER) private TableFragment filesystemRealmTable;
    @FindBy(id = ELYTRON_FILESYSTEM_REALM + "-" + FORM) private FormFragment filesystemRealmForm;

    // Identity Realm
    @FindBy(id = ELYTRON_IDENTITY_REALM + "-" + TABLE + WRAPPER) private TableFragment identityRealmTable;
    @FindBy(id = ELYTRON_IDENTITY_REALM + "-" + FORM) private FormFragment identityRealmForm;

    // JDBC Realm
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + TABLE + WRAPPER) private TableFragment jdbcRealmTable;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-" + TABLE + WRAPPER) private TableFragment principalQueryTable;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-" + ATTRIBUTES + "-" + FORM) private FormFragment principalQueryAttributesForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-clear-password-mapper-" + FORM) private FormFragment principalQueryClearPasswordForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-bcrypt-mapper-" + FORM) private FormFragment principalQueryBcryptForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-salted-simple-digest-mapper-" + FORM) private FormFragment principalQuerySaltedSimpleDigestForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-simple-digest-mapper-" + FORM) private FormFragment principalQuerySimpleDigestForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-scram-mapper-" + FORM) private FormFragment principalQueryScramForm;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + PRINCIPAL_QUERY + "-" + TAB_CONTAINER) private TabsFragment principalQueryTabs;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + ATTRIBUTE_MAPPING + "-" + TABLE + WRAPPER) private TableFragment jdbcRealmAttributeMappingTable;
    @FindBy(id = ELYTRON_JDBC_REALM + "-" + ATTRIBUTE_MAPPING + "-" + FORM) private FormFragment jdbcRealmAttributeMappingForm;

    // Key Store Realm
    @FindBy(id = ELYTRON_KEY_STORE_REALM + "-" + TABLE + WRAPPER) private TableFragment keyStoreRealmTable;
    @FindBy(id = ELYTRON_KEY_STORE_REALM + "-" + FORM) private FormFragment keyStoreRealmForm;

    // LDAP Realm
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + TABLE + WRAPPER) private TableFragment ldapRealmTable;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + TAB_CONTAINER) private TabsFragment ldapRealmFormTabs;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + FORM) private FormFragment ldapRealmForm;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + IDENTITY_MAPPING + "-" + FORM) private FormFragment ldapRealmIdentityMappingForm;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + USER_PASSWORD_MAPPER + "-" + FORM) private FormFragment ldapRealmUserPasswordMapperForm;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + OTP_CREDENTIAL_MAPPER + "-" + FORM) private FormFragment ldapRealmOTPCredentialMapperForm;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + X509_CREDENTIAL_MAPPER + "-" + FORM) private FormFragment ldapRealmX509CredentialMapperForm;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + ATTRIBUTE_MAPPING + "-" + TABLE + WRAPPER) private TableFragment ldapRealmIdentityAttributeMappingsTable;
    @FindBy(id = ELYTRON_LDAP_REALM + "-" + ATTRIBUTE_MAPPING + "-" + FORM) private FormFragment ldapRealmIdentityAttributeMappingsForm;

    // Properties Realm
    @FindBy(id = ELYTRON_PROPERTIES_REALM + "-" + TABLE + WRAPPER) private TableFragment propertiesRealmTable;
    @FindBy(id = ELYTRON_PROPERTIES_REALM + "-" + TAB_CONTAINER) private TabsFragment propertiesRealmFormTabs;
    @FindBy(id = ELYTRON_PROPERTIES_REALM + "-" + FORM) private FormFragment propertiesRealmForm;
    @FindBy(id = ELYTRON_PROPERTIES_REALM + "-groups-properties-" + FORM) private FormFragment propertiesRealmGroupsForm;
    @FindBy(id = ELYTRON_PROPERTIES_REALM + "-users-properties-" + FORM) private FormFragment propertiesRealmUsersForm;

    // Token Realm
    @FindBy(id = ELYTRON_TOKEN_REALM + "-" + TABLE + WRAPPER) private TableFragment tokenRealmTable;
    @FindBy(id = ELYTRON_TOKEN_REALM + "-" + TAB_CONTAINER) private TabsFragment tokenRealmFormTabs;
    @FindBy(id = ELYTRON_TOKEN_REALM + "-" + FORM) private FormFragment tokenRealmForm;
    @FindBy(id = ELYTRON_TOKEN_REALM + "-jwt-" + FORM) private FormFragment tokenRealmJWTForm;
    @FindBy(id = ELYTRON_TOKEN_REALM + "-oauth2-introspection-" + FORM) private FormFragment tokenRealmOAuth2Form;

    // Constant Realm Mapper
    @FindBy(id = ELYTRON_CONSTANT_REALM_MAPPER + "-" + TABLE + WRAPPER) private TableFragment constantRealmMapperTable;
    @FindBy(id = ELYTRON_CONSTANT_REALM_MAPPER + "-" + FORM) private FormFragment constantRealmMapperForm;

    // Custom Realm Mapper
    @FindBy(id = ELYTRON_CUSTOM_REALM_MAPPER + "-" + TABLE + WRAPPER) private TableFragment customRealmMapperTable;
    @FindBy(id = ELYTRON_CUSTOM_REALM_MAPPER + "-" + FORM) private FormFragment customRealmMapperForm;

    // Mapped Regex Realm Mapper
    @FindBy(id = ELYTRON_MAPPED_REGEX_REALM_MAPPER + "-" + TABLE + WRAPPER) private TableFragment mappedRegexRealmMapperTable;
    @FindBy(id = ELYTRON_MAPPED_REGEX_REALM_MAPPER + "-" + FORM) private FormFragment mappedRegexRealmMapperForm;

    // Simple Regex Realm Mapper
    @FindBy(id = ELYTRON_SIMPLE_REGEX_REALM_MAPPER + "-" + TABLE + WRAPPER) private TableFragment simpleRegexRealmMapperTable;
    @FindBy(id = ELYTRON_SIMPLE_REGEX_REALM_MAPPER + "-" + FORM) private FormFragment simpleRegexRealmMapperForm;

    public TableFragment getAggregateRealmTable() {
        return aggregateRealmTable;
    }

    public FormFragment getAggregateRealmForm() {
        return aggregateRealmForm;
    }

    public TableFragment getCachingRealmTable() {
        return cachingRealmTable;
    }

    public FormFragment getCachingRealmForm() {
        return cachingRealmForm;
    }

    public TableFragment getCustomModifiableRealmTable() {
        return customModifiableRealmTable;
    }

    public FormFragment getCustomModifiableRealmForm() {
        return customModifiableRealmForm;
    }

    public TableFragment getCustomRealmTable() {
        return customRealmTable;
    }

    public FormFragment getCustomRealmForm() {
        return customRealmForm;
    }

    public TableFragment getFilesystemRealmTable() {
        return filesystemRealmTable;
    }

    public FormFragment getFilesystemRealmForm() {
        return filesystemRealmForm;
    }

    public TableFragment getIdentityRealmTable() {
        return identityRealmTable;
    }

    public FormFragment getIdentityRealmForm() {
        return identityRealmForm;
    }

    public TableFragment getJdbcRealmTable() {
        return jdbcRealmTable;
    }

    public TableFragment getPrincipalQueryTable() {
        return principalQueryTable;
    }

    public FormFragment getPrincipalQueryAttributesForm() {
        return principalQueryAttributesForm;
    }

    public FormFragment getPrincipalQueryClearPasswordForm() {
        return principalQueryClearPasswordForm;
    }

    public FormFragment getPrincipalQueryBcryptForm() {
        return principalQueryBcryptForm;
    }

    public FormFragment getPrincipalQuerySaltedSimpleDigestForm() {
        return principalQuerySaltedSimpleDigestForm;
    }

    public FormFragment getPrincipalQuerySimpleDigestForm() {
        return principalQuerySimpleDigestForm;
    }

    public FormFragment getPrincipalQueryScramForm() {
        return principalQueryScramForm;
    }

    public TabsFragment getPrincipalQueryTabs() {
        return principalQueryTabs;
    }

    public TableFragment getJdbcRealmAttributeMappingTable() {
        return jdbcRealmAttributeMappingTable;
    }

    public FormFragment getJdbcRealmAttributeMappingForm() {
        return jdbcRealmAttributeMappingForm;
    }

    public TableFragment getKeyStoreRealmTable() {
        return keyStoreRealmTable;
    }

    public FormFragment getKeyStoreRealmForm() {
        return keyStoreRealmForm;
    }

    public TableFragment getLdapRealmTable() {
        return ldapRealmTable;
    }

    public TabsFragment getLdapRealmFormTabs() {
        return ldapRealmFormTabs;
    }

    public FormFragment getLdapRealmForm() {
        return ldapRealmForm;
    }

    public FormFragment getLdapRealmIdentityMappingForm() {
        return ldapRealmIdentityMappingForm;
    }

    public FormFragment getLdapRealmUserPasswordMapperForm() {
        return ldapRealmUserPasswordMapperForm;
    }

    public FormFragment getLdapRealmOTPCredentialMapperForm() {
        return ldapRealmOTPCredentialMapperForm;
    }

    public FormFragment getLdapRealmX509CredentialMapperForm() {
        return ldapRealmX509CredentialMapperForm;
    }

    public TableFragment getLdapRealmIdentityAttributeMappingsTable() {
        return ldapRealmIdentityAttributeMappingsTable;
    }

    public FormFragment getLdapRealmIdentityAttributeMappingsForm() {
        return ldapRealmIdentityAttributeMappingsForm;
    }

    public TableFragment getPropertiesRealmTable() {
        return propertiesRealmTable;
    }

    public TabsFragment getPropertiesRealmFormTabs() {
        return propertiesRealmFormTabs;
    }

    public FormFragment getPropertiesRealmForm() {
        return propertiesRealmForm;
    }

    public FormFragment getPropertiesRealmGroupsForm() {
        return propertiesRealmGroupsForm;
    }

    public FormFragment getPropertiesRealmUsersForm() {
        return propertiesRealmUsersForm;
    }

    public TableFragment getTokenRealmTable() {
        return tokenRealmTable;
    }

    public TabsFragment getTokenRealmFormTabs() {
        return tokenRealmFormTabs;
    }

    public FormFragment getTokenRealmForm() {
        return tokenRealmForm;
    }

    public FormFragment getTokenRealmJWTForm() {
        return tokenRealmJWTForm;
    }

    public FormFragment getTokenRealmOAuth2Form() {
        return tokenRealmOAuth2Form;
    }

    public TableFragment getConstantRealmMapperTable() {
        return constantRealmMapperTable;
    }

    public FormFragment getConstantRealmMapperForm() {
        return constantRealmMapperForm;
    }

    public TableFragment getCustomRealmMapperTable() {
        return customRealmMapperTable;
    }

    public FormFragment getCustomRealmMapperForm() {
        return customRealmMapperForm;
    }

    public TableFragment getMappedRegexRealmMapperTable() {
        return mappedRegexRealmMapperTable;
    }

    public FormFragment getMappedRegexRealmMapperForm() {
        return mappedRegexRealmMapperForm;
    }

    public TableFragment getSimpleRegexRealmMapperTable() {
        return simpleRegexRealmMapperTable;
    }

    public FormFragment getSimpleRegexRealmMapperForm() {
        return simpleRegexRealmMapperForm;
    }
}