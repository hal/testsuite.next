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
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.SOCKET_BINDING_GROUP)
public class SocketBindingPage extends BasePage {

    @FindBy(id = Ids.SOCKET_BINDING_GROUP + "-" + Ids.CONFIGURATION + "-" + Ids.FORM) private FormFragment configurationForm;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_INBOUND + "-" + Ids.TABLE + WRAPPER) private TableFragment inboundTable;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_INBOUND + "-" + Ids.FORM) private FormFragment inboundForm;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_OUTBOUND_LOCAL + "-" + Ids.TABLE + WRAPPER) private TableFragment outboundLocalTable;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_OUTBOUND_LOCAL + "-" + Ids.FORM) private FormFragment outboundLocalForm;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_OUTBOUND_REMOTE + "-" + Ids.TABLE + WRAPPER) private TableFragment outboundRemoteTable;
    @FindBy(id = Ids.SOCKET_BINDING_GROUP_OUTBOUND_REMOTE + "-" + Ids.FORM) private FormFragment outboundRemoteForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getInboundTable() {
        return inboundTable;
    }

    public FormFragment getInboundForm() {
        return inboundForm;
    }

    public TableFragment getOutboundLocalTable() {
        return outboundLocalTable;
    }

    public FormFragment getOutboundLocalForm() {
        return outboundLocalForm;
    }

    public TableFragment getOutboundRemoteTable() {
        return outboundRemoteTable;
    }

    public FormFragment getOutboundRemoteForm() {
        return outboundRemoteForm;
    }
}
