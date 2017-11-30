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
package org.jboss.hal.testsuite.dmr;

import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;

/**
 * Factory methods to create a credential reference model node which can be used in {@code :write-attribute} operations.
 */
public final class CredentialReference {

    /** Create a new credential reference model node with random store and alias names. */
    public static ModelNode storeAlias() {
        return storeAlias(Ids.build(STORE, Random.name()), Ids.build(ALIAS, Random.name()));
    }

    /** Create a new credential reference model node with the specified store and alias names. */
    public static ModelNode storeAlias(String store, String alias) {
        ModelNode modelNode = new ModelNode();
        modelNode.get(STORE).set(store);
        modelNode.get(ALIAS).set(alias);
        modelNode.get(CLEAR_TEXT).set(new ModelNode());
        return modelNode;
    }

    /** Create a new credential reference model node with random clear text. */
    public static ModelNode clearText() {
        return clearText(Ids.build(CLEAR_TEXT, Random.name()));
    }

    /** Create a new credential reference model node with the specified clear text. */
    public static ModelNode clearText(String clearText) {
        ModelNode modelNode = new ModelNode();
        modelNode.get(STORE).set(new ModelNode());
        modelNode.get(ALIAS).set(new ModelNode());
        modelNode.get(CLEAR_TEXT).set(clearText);
        return modelNode;
    }

    private CredentialReference() {
    }
}
