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

@Place(NameTokens.MODCLUSTER)
public class ModclusterPage extends BasePage {

    @FindBy(id = "modcluster-configuration-tab-container") private TabsFragment tabs;
    @FindBy(id = "advertising-form") private FormFragment advertisingForm;
    @FindBy(id = "sessions-form") private FormFragment sessionsForm;
    @FindBy(id = "web-contexts-form") private FormFragment webContextsForm;
    @FindBy(id = "proxies-form") private FormFragment proxiesForm;
    @FindBy(id = "networking-form") private FormFragment networkingForm;
    @FindBy(id = "modcluster-ssl-form") private FormFragment sslForm;

    public TabsFragment getTabs() {
        return tabs;
    }

    public FormFragment getAdvertisingForm() {
        return advertisingForm;
    }

    public FormFragment getSessionsForm() {
        return sessionsForm;
    }

    public FormFragment getWebContextsForm() {
        return webContextsForm;
    }

    public FormFragment getProxiesForm() {
        return proxiesForm;
    }

    public FormFragment getNetworkingForm() {
        return networkingForm;
    }

    public FormFragment getSslForm() {
        return sslForm;
    }
}
