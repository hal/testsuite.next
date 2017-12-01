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
package org.jboss.hal.testsuite.test.configuration;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

/** Base class for testing credential reference attributes. */
@FixMethodOrder(NAME_ASCENDING)
public abstract class CredentialReferenceTest {

    @Inject private Console console;

    protected abstract FormFragment form();

    protected abstract ResourceVerifier resourceVerifier();

    @Test
    public void updateAliasWithoutStore() {
        form().edit();
        form().clear(STORE);
        form().text(ALIAS, Random.name());
        form().clear(CLEAR_TEXT);

        form().trySave();
        form().expectError(STORE);
    }

    @Test
    public void updateStoreWithoutAlias() {
        form().edit();
        form().text(STORE, Random.name());
        form().clear(ALIAS);
        form().clear(CLEAR_TEXT);

        form().trySave();
        form().expectError(ALIAS);
    }

    @Test
    public void updateStoreAndClearText() {
        form().edit();
        form().text(STORE, Random.name());
        form().text(ALIAS, Random.name());
        form().text(CLEAR_TEXT, Random.name());
        form().trySave();
        form().expectError(STORE);
        form().expectError(CLEAR_TEXT);
    }

    @Test
    public void updateStoreAndAlias() throws Exception {
        String store = Ids.build(STORE, Random.name());
        String alias = Ids.build(ALIAS, Random.name());

        form().edit();
        form().text(STORE, store);
        form().text(ALIAS, alias);
        form().clear(CLEAR_TEXT);

        form().save();
        console.verifySuccess();
        resourceVerifier()
                .verifyAttribute(CredentialReference.fqName(STORE), store)
                .verifyAttribute(CredentialReference.fqName(ALIAS), alias);
    }

    @Test
    public void updateClearText() throws Exception {
        String clearText = Ids.build(CLEAR_TEXT, Random.name());

        form().edit();
        form().clear(STORE);
        form().clear(ALIAS);
        form().text(CLEAR_TEXT, clearText);

        form().save();
        console.verifySuccess();
        resourceVerifier().verifyAttribute(CredentialReference.fqName(CLEAR_TEXT), clearText);
    }

    @Test
    public void zzzDelete() throws Exception {
        form().remove();
        console.verifySuccess();
        resourceVerifier().verifyAttributeIsUndefined(CREDENTIAL_REFERENCE);
    }
}
