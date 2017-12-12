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
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SOURCE_CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TARGET_CREDENTIAL_REFERENCE;
import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.JMS_BRIDGE;
import static org.jboss.hal.resources.Ids.TAB_CONTAINER;

@Place(NameTokens.JMS_BRIDGE)
public class MessagingJmsBridgePage extends BasePage {

    @FindBy(id = JMS_BRIDGE + "-" + FORM) private FormFragment attributesForm;
    @FindBy(id = JMS_BRIDGE + "-" + FORM + "-source") private FormFragment sourceForm;
    @FindBy(id = JMS_BRIDGE + "-" + FORM + "-target") private FormFragment targetForm;
    @FindBy(id = JMS_BRIDGE + "-" + TAB_CONTAINER) private TabsFragment tabs;
    @FindBy(id = JMS_BRIDGE + "-" + SOURCE_CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment sourceCredentialReferenceForm;
    @FindBy(id = JMS_BRIDGE + "-" + TARGET_CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment targetCredentialReferenceForm;

    public FormFragment getAttributesForm() {
        return attributesForm;
    }

    public FormFragment getSourceForm() {
        return sourceForm;
    }

    public FormFragment getTargetForm() {
        return targetForm;
    }

    public TabsFragment getTabs() {
        return tabs;
    }

    public FormFragment getSourceCredentialReferenceForm() {
        return sourceCredentialReferenceForm;
    }

    public FormFragment getTargetCredentialReferenceForm() {
        return targetCredentialReferenceForm;
    }
}
