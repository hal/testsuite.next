package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.MESSAGING_REMOTE_ACTIVEMQ)
public class MessagingRemoteActiveMQPage extends BasePage {

    @FindBy(id = "msg-remote-connector-table_wrapper")
    private TableFragment genericConnectorTable;

    @FindBy(id = "msg-remote-connector-form")
    private FormFragment genericConnectorForm;

    @FindBy(id = "msg-remote-in-vm-connector-table_wrapper")
    private TableFragment inVMConnectorTable;

    @FindBy(id = "msg-remote-in-vm-connector-form")
    private FormFragment inVMConnectorForm;

    @FindBy(id = "msg-remote-http-connector-table_wrapper")
    private TableFragment httpConnectorTable;

    @FindBy(id = "msg-remote-http-connector-form")
    private FormFragment httpConnectorForm;

    @FindBy(id = "msg-remote-remote-connector-table_wrapper")
    private TableFragment remoteConnectorTable;

    @FindBy(id = "msg-remote-remote-connector-form")
    private FormFragment remoteConnectorForm;

    @FindBy(id = "msg-remote-discovery-group-table_wrapper")
    private TableFragment discoveryGroupTable;

    @FindBy(id = "msg-remote-discovery-group-form")
    private FormFragment dicoveryGroupForm;

    @FindBy(id = "msg-remote-connection-factory-table_wrapper")
    private TableFragment connectionFactoryTable;

    @FindBy(id = "msg-remote-connection-factory-form")
    private FormFragment connectionFactoryForm;

    @FindBy(id = "msg-remote-activemq-pooled-connection-factory-table_wrapper")
    private TableFragment pooledConnectionFactoryTable;

    @FindBy(id = "msg-remote-activemq-pooled-connection-factory-tab-container")
    private TabsFragment pooledConnectionFactoryTab;

    @FindBy(id = "messaging-pooled-connection-factory-form")
    private FormFragment pooledConnectionFactoryForm;

    @FindBy(id = "msg-remote-activemq-credential-reference-form")
    private FormFragment pooledConnectionFactoryCredentialReferenceForm;

    @FindBy(id = "msg-remote-external-queue-table_wrapper")
    private TableFragment externalJMSQueueTable;

    @FindBy(id = "msg-remote-external-queue-form")
    private FormFragment externalJMSQueueForm;

    @FindBy(id = "msg-remote-external-topic-table_wrapper")
    private TableFragment externalJMSTopicTable;

    @FindBy(id = "msg-remote-external-topic-form")
    private FormFragment externalJMSTopicForm;

    public TableFragment getGenericConnectorTable() {
        return genericConnectorTable;
    }

    public FormFragment getGenericConnectorForm() {
        return genericConnectorForm;
    }

    public TableFragment getInVMConnectorTable() {
        return inVMConnectorTable;
    }

    public FormFragment getInVMConnectorForm() {
        return inVMConnectorForm;
    }

    public TableFragment getHttpConnectorTable() {
        return httpConnectorTable;
    }

    public FormFragment getHttpConnectorForm() {
        return httpConnectorForm;
    }

    public TableFragment getRemoteConnectorTable() {
        return remoteConnectorTable;
    }

    public FormFragment getRemoteConnectorForm() {
        return remoteConnectorForm;
    }

    public TableFragment getDiscoveryGroupTable() {
        return discoveryGroupTable;
    }

    public FormFragment getDicoveryGroupForm() {
        return dicoveryGroupForm;
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

    public TabsFragment getPooledConnectionFactoryTab() {
        return pooledConnectionFactoryTab;
    }

    public FormFragment getPooledConnectionFactoryForm() {
        getPooledConnectionFactoryTab().select("msg-remote-activemq-pooled-connection-factory-attributes-tab");
        return pooledConnectionFactoryForm;
    }

    public FormFragment getPooledConnectionFactoryCredentialReferenceForm() {
        getPooledConnectionFactoryTab().select("msg-remote-activemq-pooled-connection-factory-credential-reference-tab");
        return pooledConnectionFactoryCredentialReferenceForm;
    }

    public TableFragment getExternalJMSQueueTable() {
        return externalJMSQueueTable;
    }

    public FormFragment getExternalJMSQueueForm() {
        return externalJMSQueueForm;
    }

    public TableFragment getExternalJMSTopicTable() {
        return externalJMSTopicTable;
    }

    public FormFragment getExternalJMSTopicForm() {
        return externalJMSTopicForm;
    }


}
