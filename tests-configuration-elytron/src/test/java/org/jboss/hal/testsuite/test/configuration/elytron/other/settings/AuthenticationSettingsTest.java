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
package org.jboss.hal.testsuite.test.configuration.elytron.other.settings;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.resources.Ids.ELYTRON_AUTHENTICATION_CONFIGURATION;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHENTICATION_CONFIGURATION_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHENTICATION_CONTEXT_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHENTICATION_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUTHENTICATION_NAME;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_CR_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_CR_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_CR_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CF_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_MR_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_MR_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_MR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.AUT_CT_UPDATE2;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.EXTENDS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MATCH_ABSTRACT_TYPE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MATCH_HOST;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MATCH_RULES;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MATCH_RULES_TITLE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.authenticationConfigurationAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.authenticationContextAddress;
@RunWith(Arquillian.class)
public class AuthenticationSettingsTest extends AbstractOtherSettingsTest {

    // --------------- authentication-configuration

    @Test
    public void authenticationConfigurationCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();

        crud.create(authenticationConfigurationAddress(AUT_CF_CREATE), table, AUT_CF_CREATE);
    }

    @Test
    public void authenticationConfigurationUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthenticationConfigurationForm();
        table.bind(form);
        table.select(AUT_CF_UPDATE);
        crud.update(authenticationConfigurationAddress(AUT_CF_UPDATE), form, AUTHENTICATION_NAME);
    }

    @Test
    public void authenticationConfigurationDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        crud.delete(authenticationConfigurationAddress(AUT_CF_DELETE), table, AUT_CF_DELETE);
    }

    @Test
    public void authenticationConfigurationCredentialReferenceAdd() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_CRT);
        page.getAuthenticationConfigurationTabs().select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        String clearTextValue = Random.name();
        crud.createSingleton(authenticationConfigurationAddress(AUT_CF_CR_CRT), form, formFragment -> formFragment.text("clear-text", clearTextValue), resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
        console.verifySuccess();
    }

    @Test
    public void authenticationConfigurationCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_UPD);
        page.getAuthenticationConfigurationTabs()
                .select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        crud.update(authenticationConfigurationAddress(AUT_CF_CR_UPD), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + PROPERTY_DELIMITER + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void authenticationConfigurationCredentialReferenceDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_DEL);
        page.getAuthenticationConfigurationTabs()
                .select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        crud.deleteSingleton(authenticationConfigurationAddress(AUT_CF_CR_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }


    // --------------- authentication-context

    @Test
    public void authenticationContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();

        crud.create(authenticationContextAddress(AUT_CT_CREATE), table, AUT_CT_CREATE);
    }

    @Test
    public void authenticationContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();
        FormFragment form = page.getAuthenticationContextForm();
        table.bind(form);
        table.select(AUT_CT_UPDATE);
        crud.update(authenticationContextAddress(AUT_CT_UPDATE), form, EXTENDS, AUT_CT_UPDATE2);
    }

    @Test
    public void authenticationContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();
        crud.delete(authenticationContextAddress(AUT_CT_DELETE), table, AUT_CT_DELETE);
    }

    @Test
    public void authenticationContextMatchRulesCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();

        autTable.action(AUT_CT_UPDATE, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.create(authenticationContextAddress(AUT_CT_UPDATE), table,
                    f -> f.text(MATCH_ABSTRACT_TYPE, AUT_CT_MR_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(MATCH_RULES, MATCH_ABSTRACT_TYPE, AUT_CT_MR_CREATE));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void authenticationContextMatchRulesUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();
        FormFragment form = page.getAuthenticationContextMatchRulesForm();
        table.bind(form);

        autTable.action(AUT_CT_UPDATE2, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        table.select(AUT_CT_MR_UPDATE);
        try {
            crud.update(authenticationContextAddress(AUT_CT_UPDATE2), form, f -> f.text(MATCH_HOST, ANY_STRING),
                    vc -> vc.verifyListAttributeContainsSingleValue(MATCH_RULES, MATCH_HOST, ANY_STRING));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void authenticationContextMatchRulesDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();

        autTable.action(AUT_CT_UPDATE2, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.delete(authenticationContextAddress(AUT_CT_UPDATE2), table, AUT_CT_MR_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(MATCH_RULES, MATCH_ABSTRACT_TYPE,
                            AUT_CT_MR_DELETE));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }
}
