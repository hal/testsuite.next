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

import static org.jboss.hal.dmr.ModelDescriptionConstants.IIOP_OPENJDK;

@Place(NameTokens.IIOP)
public class IIOPPage extends BasePage {

    public static final String IIOP_PREFIX = "iiop-openjdk-form";

    @FindBy(id = IIOP_PREFIX + "-" + Ids.TAB_CONTAINER) private TabsFragment tabs;
    @FindBy(id = IIOP_PREFIX + "-group-orb-form") private FormFragment orbForm;
    @FindBy(id = IIOP_PREFIX + "-group-naming-form") private FormFragment namingForm;
    @FindBy(id = IIOP_PREFIX + "-group-initializers-form") private FormFragment initializersForm;
    @FindBy(id = IIOP_PREFIX + "-group-as-context-form") private FormFragment asContextForm;
    @FindBy(id = IIOP_PREFIX + "-group-sas-context-form") private FormFragment sasContextForm;
    @FindBy(id = IIOP_PREFIX + "-group-security-form") private FormFragment securityForm;
    @FindBy(id = IIOP_PREFIX + "-group-tcp-form") private FormFragment tcpForm;
    @FindBy(id = IIOP_OPENJDK + "-properties-form") private FormFragment propertiesForm;

    public TabsFragment getTabs() {
        return tabs;
    }

    public FormFragment getOrbForm() {
        return orbForm;
    }

    public FormFragment getNamingForm() {
        return namingForm;
    }

    public FormFragment getInitializersForm() {
        return initializersForm;
    }

    public FormFragment getAsContextForm() {
        return asContextForm;
    }

    public FormFragment getSasContextForm() {
        return sasContextForm;
    }

    public FormFragment getSecurityForm() {
        return securityForm;
    }

    public FormFragment getTcpForm() {
        return tcpForm;
    }

    public FormFragment getPropertiesForm() {
        return propertiesForm;
    }
}
