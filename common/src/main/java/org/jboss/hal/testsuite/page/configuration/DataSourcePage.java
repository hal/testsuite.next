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
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.DATA_SOURCE_CONFIGURATION)
public class DataSourcePage extends BasePage {

    // non xa
    @FindBy(id = Ids.DATA_SOURCE_FORM + "-" + Ids.TAB_CONTAINER) private TabsFragment tabs;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-attributes-form") private FormFragment attributesForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-pool-form") private FormFragment poolForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-connection-form") private FormFragment connectionForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-security-form") private FormFragment securityForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-credential-reference-form") private FormFragment credentialReferenceForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-validation-form") private FormFragment validationForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-timeouts-form") private FormFragment timeoutsForm;
    @FindBy(id = Ids.DATA_SOURCE_CONFIGURATION + "-statements-tracking-form") private FormFragment statementsTrackingForm;

    // xa
    @FindBy(id = Ids.XA_DATA_SOURCE_FORM + "-" + Ids.TAB_CONTAINER) private TabsFragment xaTabs;
    @FindBy(id = Ids.XA_DATA_SOURCE + "-connection-form") private FormFragment xaConnectionForm;


    public TabsFragment getTabs() {
        return tabs;
    }

    public FormFragment getAttributesForm() {
        return attributesForm;
    }

    public FormFragment getPoolForm() {
        return poolForm;
    }

    public FormFragment getConnectionForm() {
        return connectionForm;
    }

    public FormFragment getSecurityForm() {
        return securityForm;
    }

    public FormFragment getCredentialReferenceForm() {
        return credentialReferenceForm;
    }

    public FormFragment getValidationForm() {
        return validationForm;
    }

    public FormFragment getTimeoutsForm() {
        return timeoutsForm;
    }

    public FormFragment getStatementsTrackingForm() {
        return statementsTrackingForm;
    }

    public TabsFragment getXaTabs() {
        return xaTabs;
    }

    public FormFragment getXaConnectionForm() {
        return xaConnectionForm;
    }
}
