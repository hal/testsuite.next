/*
 *  Copyright 2022 Red Hat
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jboss.hal.testsuite.creaper.command;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.wildfly.extras.creaper.core.online.OnlineCommand;
import org.wildfly.extras.creaper.core.online.OnlineCommandContext;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_MANAGER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;

public class AddKeyManager implements OnlineCommand {

    private final String name;

    public AddKeyManager(final String name) {
        this.name = name;
    }

    @Override
    public void apply(final OnlineCommandContext context) throws Exception {
        String keyStoreName = Ids.build("ks", Random.name());
        context.client.apply(new AddKeyStore(keyStoreName));

        Operations operations = new Operations(context.client);
        Address address = Address.subsystem(ELYTRON).and(KEY_MANAGER, name);
        operations.add(address, Values.of(KEY_STORE, keyStoreName)
                .and(CREDENTIAL_REFERENCE, CredentialReference.clearText("secret")));
    }
}
