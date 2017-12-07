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
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.POOLED_CONNECTION_FACTORY;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.MESSAGING_SERVER_CONNECTION)
public class MessagingServerConnectionsPage extends BasePage {

    @FindBy(id = MESSAGING_ACCEPTOR + "-" + TABLE + WRAPPER) private TableFragment acceptorGenericTable;
    @FindBy(id = MESSAGING_ACCEPTOR + "-" + FORM) private FormFragment acceptorGenericForm;
    @FindBy(id = MESSAGING_IN_VM_ACCEPTOR + "-" + TABLE + WRAPPER) private TableFragment acceptorInVMTable;
    @FindBy(id = MESSAGING_IN_VM_ACCEPTOR + "-" + FORM) private FormFragment acceptorInVMForm;
    @FindBy(id = MESSAGING_HTTP_ACCEPTOR + "-" + TABLE + WRAPPER) private TableFragment acceptorHttpTable;
    @FindBy(id = MESSAGING_HTTP_ACCEPTOR + "-" + FORM) private FormFragment acceptorHttpForm;
    @FindBy(id = MESSAGING_REMOTE_ACCEPTOR + "-" + TABLE + WRAPPER) private TableFragment acceptorRemoteTable;
    @FindBy(id = MESSAGING_REMOTE_ACCEPTOR + "-" + FORM) private FormFragment acceptorRemoteForm;
    @FindBy(id = MESSAGING_CONNECTOR + "-" + TABLE + WRAPPER) private TableFragment connectorGenericTable;
    @FindBy(id = MESSAGING_CONNECTOR + "-" + FORM) private FormFragment connectorGenericForm;
    @FindBy(id = MESSAGING_IN_VM_CONNECTOR + "-" + TABLE + WRAPPER) private TableFragment connectorInVMTable;
    @FindBy(id = MESSAGING_IN_VM_CONNECTOR + "-" + FORM) private FormFragment connectorInVMForm;
    @FindBy(id = MESSAGING_HTTP_CONNECTOR + "-" + TABLE + WRAPPER) private TableFragment connectorHttpTable;
    @FindBy(id = MESSAGING_HTTP_CONNECTOR + "-" + FORM) private FormFragment connectorHttpForm;
    @FindBy(id = MESSAGING_REMOTE_CONNECTOR + "-" + TABLE + WRAPPER) private TableFragment connectorRemoteTable;
    @FindBy(id = MESSAGING_REMOTE_CONNECTOR + "-" + FORM) private FormFragment connectorRemoteForm;
    @FindBy(id = MESSAGING_CONNECTOR_SERVICE + "-" + TABLE + WRAPPER) private TableFragment connectorServiceTable;
    @FindBy(id = MESSAGING_CONNECTOR_SERVICE + "-" + FORM) private FormFragment connectorServiceForm;
    @FindBy(id = MESSAGING_CONNECTION_FACTORY + "-" + TABLE + WRAPPER) private TableFragment connectionFactoryTable;
    @FindBy(id = MESSAGING_CONNECTION_FACTORY + "-" + FORM) private FormFragment connectionFactoryForm;
    @FindBy(id = MESSAGING_SERVER + "-" +  POOLED_CONNECTION_FACTORY + "-" + TABLE + WRAPPER) private TableFragment pooledConnectionFactoryTable;
    @FindBy(id = MESSAGING_POOLED_CONNECTION_FACTORY + "-" + FORM) private FormFragment pooledConnectionFactoryForm;
    @FindBy(id = MESSAGING_SERVER + "-" + CREDENTIAL_REFERENCE + "-" + FORM) private FormFragment pooledConnectionFactoryCRForm;
    @FindBy(id = MESSAGING_SERVER + "-" + CREDENTIAL_REFERENCE + "-" + FORM + "-" + EMPTY) private EmptyState pooledConnectionFactoryCREmptyState;
    @FindBy(id = MESSAGING_SERVER + "-" +  POOLED_CONNECTION_FACTORY + "-" + TAB_CONTAINER) private TabsFragment pooledFormsTab;

    public TableFragment getAcceptorGenericTable() {
        return acceptorGenericTable;
    }

    public FormFragment getAcceptorGenericForm() {
        return acceptorGenericForm;
    }

    public TableFragment getAcceptorInVMTable() {
        return acceptorInVMTable;
    }

    public FormFragment getAcceptorInVMForm() {
        return acceptorInVMForm;
    }

    public TableFragment getAcceptorHttpTable() {
        return acceptorHttpTable;
    }

    public FormFragment getAcceptorHttpForm() {
        return acceptorHttpForm;
    }

    public TableFragment getAcceptorRemoteTable() {
        return acceptorRemoteTable;
    }

    public FormFragment getAcceptorRemoteForm() {
        return acceptorRemoteForm;
    }

    public TableFragment getConnectorGenericTable() {
        return connectorGenericTable;
    }

    public FormFragment getConnectorGenericForm() {
        return connectorGenericForm;
    }

    public TableFragment getConnectorInVMTable() {
        return connectorInVMTable;
    }

    public FormFragment getConnectorInVMForm() {
        return connectorInVMForm;
    }

    public TableFragment getConnectorHttpTable() {
        return connectorHttpTable;
    }

    public FormFragment getConnectorHttpForm() {
        return connectorHttpForm;
    }

    public TableFragment getConnectorRemoteTable() {
        return connectorRemoteTable;
    }

    public FormFragment getConnectorRemoteForm() {
        return connectorRemoteForm;
    }

    public TableFragment getConnectorServiceTable() {
        return connectorServiceTable;
    }

    public FormFragment getConnectorServiceForm() {
        return connectorServiceForm;
    }

    public TableFragment getConnectionFactoryTable() {
        return connectionFactoryTable;
    }

    public FormFragment getConnectionFactoryForm() {
        return connectionFactoryForm;
    }

    public TableFragment getPooledConnectionFactoryTable() {
        return pooledConnectionFactoryTable;
    }

    public FormFragment getPooledConnectionFactoryForm() {
        return pooledConnectionFactoryForm;
    }

    public FormFragment getPooledConnectionFactoryCRForm() {
        return pooledConnectionFactoryCRForm;
    }

    public TabsFragment getPooledFormsTab() {
        return pooledFormsTab;
    }

    public EmptyState getPooledConnectionFactoryCREmptyState() {
        return pooledConnectionFactoryCREmptyState;
    }
}
