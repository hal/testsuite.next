/*
 * Copyright 2015-2018 Red Hat, Inc, and individual contributors.
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
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.SECURITY_CONFIGURATION)
public class SecurityLegacyPage extends BasePage {

    @FindBy(id = "security-configuration-" + FORM) private FormFragment configurationForm;
    @FindBy(id = SECURITY + "-" + ELYTRON_KEY_MANAGER + "-" + TABLE +  WRAPPER) private TableFragment elytronKeyManagerTable;
    @FindBy(id = SECURITY + "-" + ELYTRON_KEY_MANAGER + "-" + FORM) private FormFragment elytronKeyManagerForm;
    @FindBy(id = SECURITY + "-" + ELYTRON_KEY_STORE + "-" + TABLE +  WRAPPER) private TableFragment elytronKeyStoreTable;
    @FindBy(id = SECURITY + "-" + ELYTRON_KEY_STORE + "-" + FORM) private FormFragment elytronKeyStoreForm;
    @FindBy(id = SECURITY + "-elytron-realm-" + TABLE +  WRAPPER) private TableFragment elytronRealmTable;
    @FindBy(id = SECURITY + "-elytron-realm-" + FORM) private FormFragment elytronRealmForm;
    @FindBy(id = SECURITY + "-" + ELYTRON_TRUST_MANAGER + "-" + TABLE +  WRAPPER) private TableFragment elytronTrustManagerTable;
    @FindBy(id = SECURITY + "-" + ELYTRON_TRUST_MANAGER + "-" + FORM) private FormFragment elytronTrustManagerForm;
    @FindBy(id = SECURITY + "-elytron-trust-store-" + TABLE +  WRAPPER) private TableFragment elytronTrustStoreTable;
    @FindBy(id = SECURITY + "-elytron-trust-store-" + FORM) private FormFragment elytronTrustStoreForm;
    @FindBy(id = SECURITY + "-vault-" + FORM) private FormFragment vaultForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getElytronKeyManagerTable() {
        return elytronKeyManagerTable;
    }

    public FormFragment getElytronKeyManagerForm() {
        return elytronKeyManagerForm;
    }

    public TableFragment getElytronKeyStoreTable() {
        return elytronKeyStoreTable;
    }

    public FormFragment getElytronKeyStoreForm() {
        return elytronKeyStoreForm;
    }

    public TableFragment getElytronRealmTable() {
        return elytronRealmTable;
    }

    public FormFragment getElytronRealmForm() {
        return elytronRealmForm;
    }

    public TableFragment getElytronTrustManagerTable() {
        return elytronTrustManagerTable;
    }

    public FormFragment getElytronTrustManagerForm() {
        return elytronTrustManagerForm;
    }

    public TableFragment getElytronTrustStoreTable() {
        return elytronTrustStoreTable;
    }

    public FormFragment getElytronTrustStoreForm() {
        return elytronTrustStoreForm;
    }

    public FormFragment getVaultForm() {
        return vaultForm;
    }
}
