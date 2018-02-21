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

import static org.jboss.hal.resources.Ids.CONFIGURATION;
import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.SECURITY_DOMAIN)
public class SecurityDomainLegacyPage extends BasePage {

    @FindBy(id = "security-domain-" + CONFIGURATION + "-" + FORM) private FormFragment configurationForm;
    @FindBy(id = "security-domain-authentication-" + TABLE +  WRAPPER) private TableFragment authenticationTable;
    @FindBy(id = "security-domain-authentication-" + FORM) private FormFragment authenticationForm;
    @FindBy(id = "security-domain-authorization-" + TABLE +  WRAPPER) private TableFragment authorizationTable;
    @FindBy(id = "security-domain-authorization-" + FORM) private FormFragment authorizationForm;
    @FindBy(id = "security-domain-audit-" + TABLE +  WRAPPER) private TableFragment auditTable;
    @FindBy(id = "security-domain-audit-" + FORM) private FormFragment auditForm;
    @FindBy(id = "security-domain-acl-" + TABLE +  WRAPPER) private TableFragment aclTable;
    @FindBy(id = "security-domain-acl-" + FORM) private FormFragment aclForm;
    @FindBy(id = "security-domain-mapping-" + TABLE +  WRAPPER) private TableFragment mappingTable;
    @FindBy(id = "security-domain-mapping-" + FORM) private FormFragment mappingForm;
    @FindBy(id = "security-domain-trust-" + TABLE +  WRAPPER) private TableFragment identityTrustTable;
    @FindBy(id = "security-domain-trust-" + FORM) private FormFragment identityTrustForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getAuthenticationTable() {
        return authenticationTable;
    }

    public FormFragment getAuthenticationForm() {
        return authenticationForm;
    }

    public TableFragment getAuthorizationTable() {
        return authorizationTable;
    }

    public FormFragment getAuthorizationForm() {
        return authorizationForm;
    }

    public TableFragment getAuditTable() {
        return auditTable;
    }

    public FormFragment getAuditForm() {
        return auditForm;
    }

    public TableFragment getAclTable() {
        return aclTable;
    }

    public FormFragment getAclForm() {
        return aclForm;
    }

    public TableFragment getMappingTable() {
        return mappingTable;
    }

    public FormFragment getMappingForm() {
        return mappingForm;
    }

    public TableFragment getIdentityTrustTable() {
        return identityTrustTable;
    }

    public FormFragment getIdentityTrustForm() {
        return identityTrustForm;
    }
}
