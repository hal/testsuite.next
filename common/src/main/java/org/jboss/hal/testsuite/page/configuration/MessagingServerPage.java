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
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.Ids.ATTRIBUTES;
import static org.jboss.hal.resources.Ids.EMPTY;
import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER;

@Place(NameTokens.MESSAGING_SERVER)
public class MessagingServerPage extends BasePage {

    // ----------- Server / Configuration
    @FindBy(id = MESSAGING_SERVER + "-" + Ids.TAB_CONTAINER) private TabsFragment tab;
    @FindBy(id = MESSAGING_SERVER + "-" + ATTRIBUTES + "-" + FORM) private FormFragment attributesForm;
    @FindBy(id = MESSAGING_SERVER + "-group-management-" + FORM) private FormFragment managementForm;
    @FindBy(id = MESSAGING_SERVER + "-group-security-" + FORM) private FormFragment securityForm;
    @FindBy(id = MESSAGING_SERVER + "-group-journal-" + FORM) private FormFragment journalForm;
    @FindBy(id = MESSAGING_SERVER + "-group-cluster-" + FORM) private FormFragment clusterForm;
    @FindBy(id = MESSAGING_SERVER + "-cluster-credential-reference-" + FORM) private FormFragment clusterCredentialReferenceForm;
    @FindBy(id = MESSAGING_SERVER + "-cluster-credential-reference-" + FORM + "-" + EMPTY) private EmptyState clusterCredentialReferenceEmptyState;

    public TabsFragment getTab() {
        return tab;
    }

    public FormFragment getAttributesForm() {
        return attributesForm;
    }

    public FormFragment getManagementForm() {
        return managementForm;
    }

    public FormFragment getSecurityForm() {
        return securityForm;
    }

    public FormFragment getJournalForm() {
        return journalForm;
    }

    public FormFragment getClusterForm() {
        return clusterForm;
    }

    public FormFragment getClusterCredentialReferenceForm() {
        return clusterCredentialReferenceForm;
    }

    public EmptyState getClusterCredentialReferenceEmptyState() {
        return clusterCredentialReferenceEmptyState;
    }
}
