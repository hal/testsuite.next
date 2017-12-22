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

@Place(NameTokens.RESOURCE_ADAPTER)
public class ResourceAdapterPage extends BasePage {

    @FindBy(id = "resource-adapter-configuration-form-tab-container") private TabsFragment configurationTabs;
    @FindBy(id = "resource-adapter-configuration-attributes-form") private FormFragment attributesForm;
    @FindBy(id = "resource-adapter-configuration-wm-security-form") private FormFragment wmSecurityForm;
    @FindBy(id = "resource-adapter-connection-definition-table") private TableFragment cdTable;
    @FindBy(id = "resource-adapter-connection-definition-form-tab-container") private TabsFragment cdTabs;
    @FindBy(id = "resource-adapter-connection-definition-attributes-form") private FormFragment cdAttributesForm;
    @FindBy(id = "resource-adapter-connection-definition-security") private FormFragment cdSecurityForm;
    @FindBy(id = "resource-adapter-connection-definition-recovery") private FormFragment cdRecoveryForm;
    @FindBy(id = "resource-adapter-connection-definition-validation") private FormFragment cdValidationForm;
    @FindBy(id = "resource-adapter-admin-object-table") private TableFragment adminObjectTable;
    @FindBy(id = "resource-adapter-admin-object-form") private FormFragment adminObjectForm;

    public TabsFragment getConfigurationTabs() {
        return configurationTabs;
    }

    public FormFragment getAttributesForm() {
        return attributesForm;
    }

    public FormFragment getWmSecurityForm() {
        return wmSecurityForm;
    }

    public TableFragment getCdTable() {
        return cdTable;
    }

    public TabsFragment getCdTabs() {
        return cdTabs;
    }

    public FormFragment getCdAttributesForm() {
        return cdAttributesForm;
    }

    public FormFragment getCdSecurityForm() {
        return cdSecurityForm;
    }

    public FormFragment getCdRecoveryForm() {
        return cdRecoveryForm;
    }

    public FormFragment getCdValidationForm() {
        return cdValidationForm;
    }

    public TableFragment getAdminObjectTable() {
        return adminObjectTable;
    }

    public FormFragment getAdminObjectForm() {
        return adminObjectForm;
    }
}
