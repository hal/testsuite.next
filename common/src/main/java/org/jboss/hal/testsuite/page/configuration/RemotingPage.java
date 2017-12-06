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
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.REMOTING)
public class RemotingPage extends BasePage {

    @FindBy(id = "remoting-endpoint-configuration-tab-container") private TabsFragment endpointTabs;
    @FindBy(id = "remoting-endpoint-configuration-attributes-form") private FormFragment endpointAttributesForm;
    @FindBy(id = "remoting-endpoint-configuration-security-form") private FormFragment endpointSecurityForm;
    @FindBy(id = "remoting-endpoint-configuration-channels-form") private FormFragment endpointChannelsForm;
    @FindBy(id = "remoting-connector-table" + WRAPPER) private TableFragment connectorTable;
    @FindBy(id = Ids.REMOTING_CONNECTOR_TAB_CONTAINER) private TabsFragment connectorTabs;
    @FindBy(id = Ids.REMOTING_CONNECTOR_FORM) private FormFragment connectorAttributesForm;
    @FindBy(id = Ids.REMOTING_CONNECTOR_SECURITY_FORM) private FormFragment connectorSecurityForm;
    @FindBy(id = Ids.REMOTING_CONNECTOR_SECURITY_POLICY_FORM) private FormFragment connectorPolicyForm;
    @FindBy(id = "remoting-http-connector-table" + WRAPPER) private TableFragment httpConnectorTable;
    @FindBy(id = Ids.REMOTING_HTTP_CONNECTOR_TAB_CONTAINER) private TabsFragment httpConnectorTabs;
    @FindBy(id = Ids.REMOTING_HTTP_CONNECTOR_FORM) private FormFragment httpConnectorAttributesForm;
    @FindBy(id = Ids.REMOTING_HTTP_CONNECTOR_SECURITY_FORM) private FormFragment httpConnectorSecurityForm;
    @FindBy(id = Ids.REMOTING_HTTP_CONNECTOR_SECURITY_POLICY_FORM) private FormFragment httpConnectorPolicyForm;
    @FindBy(id = "remoting-local-outbound-table" + WRAPPER) private TableFragment localOutboundTable;
    @FindBy(id = "remoting-local-outbound-form") private FormFragment localOutboundForm;
    @FindBy(id = "remoting-outbound-table" + WRAPPER) private TableFragment outboundTable;
    @FindBy(id = "remoting-outbound-form") private FormFragment outboundForm;
    @FindBy(id = "remoting-remote-outbound-table" + WRAPPER) private TableFragment remoteOutboundTable;
    @FindBy(id = "remoting-remote-outbound-form") private FormFragment remoteOutboundForm;

    public TabsFragment getEndpointTabs() {
        return endpointTabs;
    }

    public FormFragment getEndpointAttributesForm() {
        return endpointAttributesForm;
    }

    public FormFragment getEndpointSecurityForm() {
        return endpointSecurityForm;
    }

    public FormFragment getEndpointChannelsForm() {
        return endpointChannelsForm;
    }

    public TableFragment getConnectorTable() {
        return connectorTable;
    }

    public TabsFragment getConnectorTabs() {
        return connectorTabs;
    }

    public FormFragment getConnectorAttributesForm() {
        return connectorAttributesForm;
    }

    public FormFragment getConnectorSecurityForm() {
        return connectorSecurityForm;
    }

    public FormFragment getConnectorPolicyForm() {
        return connectorPolicyForm;
    }

    public TableFragment getHttpConnectorTable() {
        return httpConnectorTable;
    }

    public TabsFragment getHttpConnectorTabs() {
        return httpConnectorTabs;
    }

    public FormFragment getHttpConnectorAttributesForm() {
        return httpConnectorAttributesForm;
    }

    public FormFragment getHttpConnectorSecurityForm() {
        return httpConnectorSecurityForm;
    }

    public FormFragment getHttpConnectorPolicyForm() {
        return httpConnectorPolicyForm;
    }

    public TableFragment getLocalOutboundTable() {
        return localOutboundTable;
    }

    public FormFragment getLocalOutboundForm() {
        return localOutboundForm;
    }

    public TableFragment getOutboundTable() {
        return outboundTable;
    }

    public FormFragment getOutboundForm() {
        return outboundForm;
    }

    public TableFragment getRemoteOutboundTable() {
        return remoteOutboundTable;
    }

    public FormFragment getRemoteOutboundForm() {
        return remoteOutboundForm;
    }
}
