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

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CUSTOM_POLICY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JACC_POLICY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.POLICY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.URL;
import static org.jboss.hal.resources.Ids.ELYTRON_CUSTOM_POLICY_EMPTY;
import static org.jboss.hal.resources.Ids.ELYTRON_DIR_CONTEXT;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.Selectors.contains;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ADD_CUSTOM_POLICY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ADD_JACC_POLICY;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_CONTEXT_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_CR_CRT;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_CR_DEL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_CR_UPD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.OTHER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.POLICY_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.POL_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.PRINCIPAL;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.dirContextAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.policyAddress;
@RunWith(Arquillian.class)
public class OtherSettingsTest extends AbstractOtherSettingsTest {

    // --------------- jacc policy

    @Test
    public void jaccPolicyCreate() throws Exception {
        operations.removeIfExists(policyAddress(POL_CREATE));
        console.reload();
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        EmptyState emptyState = page.getEmptyPolicy();
        By selector = ByJQuery.selector("button" + contains(ADD_JACC_POLICY));
        emptyState.getRoot().findElement(selector).click();

        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().text(NAME, POL_CREATE);
        addDialog.add();

        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyExists();
    }

    @Test
    public void jaccPolicyUpdate() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode empty = new ModelNode();
            empty.setEmptyObject();
            operations.add(policyAddress(POL_CREATE), Values.of(JACC_POLICY, empty));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyJaccForm();
        crud.update(policyAddress(POL_CREATE), form, f -> f.text(POLICY, ANY_STRING),
                verify -> verify.verifyAttribute("jacc-policy.policy", ANY_STRING));
    }

    @Test
    public void jaccPolicyDelete() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode empty = new ModelNode();
            empty.setEmptyObject();
            operations.add(policyAddress(POL_CREATE), Values.of(JACC_POLICY, empty));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyJaccForm();
        form.getRoot().findElement(By.cssSelector("a[data-operation=remove]")).click();
        console.confirmationDialog().confirm();
        waitGui().until().element(By.id(ELYTRON_CUSTOM_POLICY_EMPTY)).is().visible();
        // form.remove operation doesn't work because it waits for the blank-slate-pf css of form to become visible
        // but the emptyState div is outside the form div
        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyDoesNotExist();
    }

    // --------------- custom policy

    @Test
    public void customPolicyCreate() throws Exception {
        operations.removeIfExists(policyAddress(POL_CREATE));
        console.reload();
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        EmptyState emptyState = page.getEmptyPolicy();
        By selector = ByJQuery.selector("button" + contains(ADD_CUSTOM_POLICY));
        emptyState.getRoot().findElement(selector).click();

        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().text(NAME, POL_CREATE);
        addDialog.getForm().text(CLASS_NAME, ANY_STRING);
        addDialog.add();

        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyExists();
    }

    @Test
    public void customPolicyUpdate() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode customPolicy = new ModelNode();
            customPolicy.get(CLASS_NAME).set(ANY_STRING);
            operations.add(policyAddress(POL_CREATE), Values.of(CUSTOM_POLICY, customPolicy));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyCustomForm();
        String module = Random.name();
        crud.update(policyAddress(POL_CREATE), form, f -> f.text(MODULE, module),
                verify -> verify.verifyAttribute("custom-policy.module", module));
    }

    // There is no need for a customPolicyDelete test as the "remove" UI operation
    // is the same for jacc and custom policy

    // --------------- dir-context

    @Test
    public void dirContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();

        crud.create(dirContextAddress(DIR_CREATE), table, f -> {
            f.text(NAME, DIR_CREATE);
            f.text(URL, ANY_STRING);
        });
    }

    @Test
    public void dirContextTryCreate() {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, DIR_CREATE), URL);
    }

    @Test
    public void dirContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextForm();
        table.bind(form);
        table.select(DIR_UPDATE);
        crud.update(dirContextAddress(DIR_UPDATE), form, PRINCIPAL);
    }

    @Test
    public void dirContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        crud.delete(dirContextAddress(DIR_DELETE), table, DIR_DELETE);
    }

    @Test
    public void dirContextCredentialReferenceAdd() throws Exception {
        String clearTextValue = Random.name();
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_CRT);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        crud.createSingleton(dirContextAddress(DIR_CR_CRT), form,
                formFragment -> formFragment.text(ElytronFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT, clearTextValue),
                resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
    }

    @Test
    public void dirContextCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_UPD);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        crud.update(dirContextAddress(DIR_CR_UPD), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + PROPERTY_DELIMITER + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void dirContextCredentialReferenceDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_DEL);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        crud.deleteSingleton(dirContextAddress(DIR_CR_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }
}
