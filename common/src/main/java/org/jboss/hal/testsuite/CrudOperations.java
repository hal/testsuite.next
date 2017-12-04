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
package org.jboss.hal.testsuite;

import java.util.function.Consumer;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

/** Methods useful to test and verify CRUD operations in application views. */
public class CrudOperations {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    @Inject private Console console;


    // ------------------------------------------------------ create

    /** Adds a resource using the 'Add' button of the specified table. */
    public void create(Address address, TableFragment table, String name) throws Exception {
        create(address, table, form -> form.text(NAME, name));
    }

    /** Adds a resource using the 'Add' button of the specified table. */
    public void create(Address address, TableFragment table, Consumer<FormFragment> initialValues)
            throws Exception {
        create(address, table, initialValues, ResourceVerifier::verifyExists);
    }

    /** Adds a resource using the 'Add' button of the specified table. */
    public void create(Address address, TableFragment table, Consumer<FormFragment> initialValues,
            VerifyChanges verifyChanges) throws Exception {
        AddResourceDialogFragment dialog = table.add();
        initialValues.accept(dialog.getForm());
        dialog.add();

        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }

    /** Adds a singleton resource using the main action of an empty state. */
    public void createSingleton(Address address, FormFragment form) throws Exception {
        createSingleton(address, form, null);
    }

    /** Adds a singleton resource using the main action of an empty state and the specified initial form values. */
    public void createSingleton(Address address, FormFragment form, Consumer<FormFragment> initialValues)
            throws Exception {
        createSingleton(address, form, initialValues, ResourceVerifier::verifyExists);
    }

    /** Adds a singleton resource using the main action of an empty state and the specified initial form values. */
    public void createSingleton(Address address, FormFragment form, Consumer<FormFragment> initialValues,
            VerifyChanges verifyChanges) throws Exception {
        form.emptyState().mainAction();
        if (initialValues != null) {
            AddResourceDialogFragment dialog = console.addResourceDialog();
            initialValues.accept(dialog.getForm()); // use the form of add resource dialog!
            dialog.add();
        }

        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }


    // ------------------------------------------------------ create and expect error

    /** Tries to add a resource and expects client side errors. */
    public void createWithError(TableFragment table, String name, String expectError) {
        createWithError(table, form -> form.text(NAME, name), expectError);
    }

    /** Tries to add a resource and expects client side errors. */
    public void createWithError(TableFragment table, Consumer<FormFragment> initialValues,
            String expectError) {
        AddResourceDialogFragment dialog = table.add();
        FormFragment form = dialog.getForm();
        initialValues.accept(form);
        dialog.getPrimaryButton().click();
        form.expectError(expectError);
    }


    // ------------------------------------------------------ reset

    /** Resets the specified form and verifies the related resource. */
    public void reset(Address address, FormFragment form) throws Exception {
        reset(address, form, ResourceVerifier::verifyReset);
    }

    /** Resets the specified form and verifies the related resource. */
    public void reset(Address address, FormFragment form, VerifyChanges verifyChanges) throws Exception {
        form.reset();
        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }


    // ------------------------------------------------------ update

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute) throws Exception {
        update(address, form, attribute, Random.name());
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute, boolean value) throws Exception {
        update(address, form, f -> f.flip(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute, int value) throws Exception {
        update(address, form, f -> f.number(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute, long value) throws Exception {
        update(address, form, f -> f.number(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute, String value) throws Exception {
        update(address, form, f -> f.text(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, String attribute, ModelNode value) throws Exception {
        update(address, form, f -> f.properties(attribute).add(value),
                verifier -> verifier.verifyAttribute(attribute, value));
    }

    /** Updates the specified form and verifies the changes. */
    public void update(Address address, FormFragment form, Consumer<FormFragment> modifyFields,
            VerifyChanges verifyChanges) throws Exception {
        form.edit();
        modifyFields.accept(form);
        form.save();

        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }


    // ------------------------------------------------------ update and expect error

    /** Tries to update the specified form and expects client side errors. */
    public void updateWithError(FormFragment form, String attribute, int value) {
        updateWithError(form, f -> f.number(attribute, value), attribute);
    }

    /** Tries to update the specified form and expects client side errors. */
    public void updateWithError(FormFragment form, String attribute, String value) {
        updateWithError(form, f -> f.text(attribute, value), attribute);
    }

    /** Tries to update the specified form and expects client side errors. */
    public void updateWithError(FormFragment form, Consumer<FormFragment> modifyFields, String... expectError) {
        form.edit();
        modifyFields.accept(form);
        form.trySave();
        if (expectError != null) {
            for (String field : expectError) {
                form.expectError(field);
            }
        }
    }


    // ------------------------------------------------------ delete

    /** Removes the named resource from the table and verifies that it no longer exists. */
    public void delete(Address address, TableFragment table, String name) throws Exception {
        delete(address, table, name, ResourceVerifier::verifyDoesNotExist);
    }

    /** Removes the named resource from the table and verifies that it no longer exists. */
    public void delete(Address address, TableFragment table, String name, VerifyChanges verifyChanges)
            throws Exception {
        table.remove(name);
        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }

    /** Removes a singleton resource and verifies that the resource no longer exists. */
    public void deleteSingleton(Address address, FormFragment form) throws Exception {
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(address, client).verifyDoesNotExist();
    }


    public interface VerifyChanges {

        void verify(ResourceVerifier resourceVerifier) throws Exception;
    }
}
