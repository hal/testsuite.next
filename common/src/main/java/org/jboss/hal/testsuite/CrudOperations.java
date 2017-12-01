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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class CrudOperations {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    @Inject private Console console;


    // ------------------------------------------------------ update

    public void update(Address address, FormFragment form, String attribute) throws Exception {
        update(address, form, attribute, Random.name());
    }

    public void update(Address address, FormFragment form, String attribute, int value) throws Exception {
        update(address, form, f -> f.number(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    public void update(Address address, FormFragment form, String attribute, long value) throws Exception {
        update(address, form, f -> f.number(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    public void update(Address address, FormFragment form, String attribute, String value) throws Exception {
        update(address, form, f -> f.text(attribute, value), verifier -> verifier.verifyAttribute(attribute, value));
    }

    public void update(Address address, FormFragment form, Consumer<FormFragment> modifyFields,
            VerifyChanges verifyChanges) throws Exception {
        form.edit();
        modifyFields.accept(form);
        form.save();

        console.verifySuccess();
        verifyChanges.verify(new ResourceVerifier(address, client));
    }


    public interface VerifyChanges {

        void verify(ResourceVerifier resourceVerifier) throws Exception;
    }
}
