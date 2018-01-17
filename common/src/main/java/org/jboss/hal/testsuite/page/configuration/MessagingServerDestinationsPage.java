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
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.MESSAGING_SERVER_DESTINATION)
public class MessagingServerDestinationsPage extends BasePage {

    // ----------- Core queue
    @FindBy(id = MESSAGING_CORE_QUEUE + "-" + TABLE + WRAPPER) private TableFragment coreQueueTable;
    @FindBy(id = MESSAGING_CORE_QUEUE + "-" + FORM) private FormFragment coreQueueForm;

    // ----------- JMS queue
    @FindBy(id = MESSAGING_JMS_QUEUE + "-" + TABLE + WRAPPER) private TableFragment jmsQueueTable;
    @FindBy(id = MESSAGING_JMS_QUEUE + "-" + FORM) private FormFragment jmsQueueForm;

    // ----------- JMS Topic
    @FindBy(id = MESSAGING_JMS_TOPIC + "-" + TABLE + WRAPPER) private TableFragment jmsTopicTable;
    @FindBy(id = MESSAGING_JMS_TOPIC + "-" + FORM) private FormFragment jmsTopicForm;

    // ----------- Security Setting
    @FindBy(id = "messaging-security-setting-role-" + TABLE + WRAPPER) private TableFragment securitySettingTable;
    @FindBy(id = "messaging-security-setting-role-" + FORM) private FormFragment securitySettingForm;

    // ----------- Address Setting
    @FindBy(id = MESSAGING_ADDRESS_SETTING + "-" + TABLE + WRAPPER) private TableFragment addressSettingTable;
    @FindBy(id = MESSAGING_ADDRESS_SETTING + "-" + FORM) private FormFragment addressSettingForm;

    // ----------- Divert
    @FindBy(id = MESSAGING_DIVERT + "-" + TABLE + WRAPPER) private TableFragment divertTable;
    @FindBy(id = MESSAGING_DIVERT + "-" + FORM) private FormFragment divertForm;

    public TableFragment getCoreQueueTable() {
        return coreQueueTable;
    }

    public FormFragment getCoreQueueForm() {
        return coreQueueForm;
    }

    public TableFragment getJmsQueueTable() {
        return jmsQueueTable;
    }

    public FormFragment getJmsQueueForm() {
        return jmsQueueForm;
    }

    public TableFragment getJmsTopicTable() {
        return jmsTopicTable;
    }

    public FormFragment getJmsTopicForm() {
        return jmsTopicForm;
    }

    public TableFragment getSecuritySettingTable() {
        return securitySettingTable;
    }

    public FormFragment getSecuritySettingForm() {
        return securitySettingForm;
    }

    public TableFragment getAddressSettingTable() {
        return addressSettingTable;
    }

    public FormFragment getAddressSettingForm() {
        return addressSettingForm;
    }

    public TableFragment getDivertTable() {
        return divertTable;
    }

    public FormFragment getDivertForm() {
        return divertForm;
    }
}
