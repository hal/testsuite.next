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

import static org.jboss.hal.dmr.ModelDescriptionConstants.BRIDGE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.MESSAGING_SERVER_CLUSTERING)
public class MessagingServerClusteringPage extends BasePage {

    @FindBy(id = MESSAGING_BROADCAST_GROUP + "-" + TABLE + WRAPPER) private TableFragment broadcastGroupTable;
    @FindBy(id = MESSAGING_BROADCAST_GROUP + "-" + FORM) private FormFragment broadcastGroupForm;
    @FindBy(id = MESSAGING_DISCOVERY_GROUP + "-" + TABLE + WRAPPER) private TableFragment discoveryGroupTable;
    @FindBy(id = MESSAGING_DISCOVERY_GROUP + "-" + FORM) private FormFragment discoveryGroupForm;
    @FindBy(id = MESSAGING_CLUSTER_CONNECTION + "-" + TABLE + WRAPPER) private TableFragment clusterConnectionTable;
    @FindBy(id = MESSAGING_CLUSTER_CONNECTION + "-" + FORM) private FormFragment clusterConnectionForm;
    @FindBy(id = MESSAGING_GROUPING_HANDLER + "-" + TABLE + WRAPPER) private TableFragment groupingHandlerTable;
    @FindBy(id = MESSAGING_GROUPING_HANDLER + "-" + FORM) private FormFragment groupingHandlerForm;
    @FindBy(id = MESSAGING_SERVER + "-" +  BRIDGE + "-" + TABLE + WRAPPER) private TableFragment bridgeTable;
    @FindBy(id = MESSAGING_BRIDGE + "-" + FORM) private FormFragment bridgeForm;
    @FindBy(id = MESSAGING_SERVER + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment bridgeCRForm;
    @FindBy(id = MESSAGING_SERVER + "-" +  BRIDGE + "-" + TAB_CONTAINER) private TabsFragment bridgeFormsTab;

    public TableFragment getBroadcastGroupTable() {
        return broadcastGroupTable;
    }

    public FormFragment getBroadcastGroupForm() {
        return broadcastGroupForm;
    }

    public TableFragment getDiscoveryGroupTable() {
        return discoveryGroupTable;
    }

    public FormFragment getDiscoveryGroupForm() {
        return discoveryGroupForm;
    }

    public TableFragment getClusterConnectionTable() {
        return clusterConnectionTable;
    }

    public FormFragment getClusterConnectionForm() {
        return clusterConnectionForm;
    }

    public TableFragment getGroupingHandlerTable() {
        return groupingHandlerTable;
    }

    public FormFragment getGroupingHandlerForm() {
        return groupingHandlerForm;
    }

    public TableFragment getBridgeTable() {
        return bridgeTable;
    }

    public FormFragment getBridgeForm() {
        return bridgeForm;
    }

    public FormFragment getBridgeCRForm() {
        return bridgeCRForm;
    }

    public TabsFragment getBridgeFormsTab() {
        return bridgeFormsTab;
    }
}
