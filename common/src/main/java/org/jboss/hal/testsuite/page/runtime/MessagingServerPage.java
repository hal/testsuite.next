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
package org.jboss.hal.testsuite.page.runtime;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.MESSAGING_SERVER_RUNTIME)
public class MessagingServerPage extends BasePage {

    @FindBy(id = Ids.MESSAGING_SERVER_CONNECTION_TABLE) private TableFragment connectionTable;
    @FindBy(id = Ids.MESSAGING_SERVER_CONNECTION_FORM) private FormFragment connectionForm;
    @FindBy(id = Ids.MESSAGING_SERVER_CONSUMER_TABLE) private TableFragment consumerTable;
    @FindBy(id = Ids.MESSAGING_SERVER_CONSUMER_FORM) private FormFragment consumerForm;
    @FindBy(id = Ids.MESSAGING_SERVER_PRODUCER_TABLE) private TableFragment producerTable;
    @FindBy(id = Ids.MESSAGING_SERVER_PRODUCER_FORM) private FormFragment producerForm;
    @FindBy(id = Ids.MESSAGING_SERVER_CONNECTOR_TABLE) private TableFragment connectorTable;
    @FindBy(id = Ids.MESSAGING_SERVER_CONNECTOR_FORM) private FormFragment connectorForm;
    @FindBy(id = Ids.MESSAGING_SERVER_ROLE_TABLE) private TableFragment roleTable;
    @FindBy(id = Ids.MESSAGING_SERVER_ROLE_FORM) private FormFragment roleForm;
    @FindBy(id = Ids.MESSAGING_SERVER_TRANSACTION_TABLE) private TableFragment transactionTable;
    @FindBy(id = Ids.MESSAGING_SERVER_TRANSACTION_FORM) private FormFragment transactionForm;

    public TableFragment getConnectionTable() {
        return connectionTable;
    }

    public FormFragment getConnectionForm() {
        return connectionForm;
    }

    public TableFragment getConsumerTable() {
        return consumerTable;
    }

    public FormFragment getConsumerForm() {
        return consumerForm;
    }

    public TableFragment getProducerTable() {
        return producerTable;
    }

    public FormFragment getProducerForm() {
        return producerForm;
    }

    public TableFragment getConnectorTable() {
        return connectorTable;
    }

    public FormFragment getConnectorForm() {
        return connectorForm;
    }

    public TableFragment getRoleTable() {
        return roleTable;
    }

    public FormFragment getRoleForm() {
        return roleForm;
    }

    public TableFragment getTransactionTable() {
        return transactionTable;
    }

    public FormFragment getTransactionForm() {
        return transactionForm;
    }
}
