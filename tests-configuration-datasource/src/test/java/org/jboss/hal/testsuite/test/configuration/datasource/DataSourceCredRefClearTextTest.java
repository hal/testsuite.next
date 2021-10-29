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
package org.jboss.hal.testsuite.test.configuration.datasource;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.READ_ALIASES_OPERATION;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.H2_PASSWORD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CRED_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.credentialStoreAddress;

@RunWith(Arquillian.class)
public class DataSourceCredRefClearTextTest extends DataSourceCredRefBase {

    /**
     * Update the clear-text value of the credential reference. The number of aliases in the credential store must remain one.
     */
    @Test
    public void updateClearText() throws Exception {
        form.edit();
        form.text(CLEAR_TEXT, H2_PASSWORD);
        form.save();

        reload();
        new ResourceVerifier(credentialStoreAddress(CRED_ST_UPDATE), client).verifyTrue(
                "Alias not found in credential store",
                () -> {
                    ModelNodeResult result = operations.invoke(READ_ALIASES_OPERATION, credentialStoreAddress(CRED_ST_UPDATE));
                    return assertAlias(result, ALIAS_VALUE);
                });
    }
}
