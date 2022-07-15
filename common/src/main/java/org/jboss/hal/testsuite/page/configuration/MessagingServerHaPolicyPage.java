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
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_POLICY_EMPTY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION_COLOCATED;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION_LIVE_ONLY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION_SECONDARY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE_COLOCATED;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE_PRIMARY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE_SECONDARY;

@Place(NameTokens.MESSAGING_SERVER_HA_POLICY)
public class MessagingServerHaPolicyPage extends BasePage {

    @FindBy(id = MESSAGING_HA_POLICY_EMPTY) private EmptyState emptyState;
    @FindBy(id = MESSAGING_HA_REPLICATION_LIVE_ONLY + "-" + FORM) private FormFragment replicationLiveOnlyForm;
    @FindBy(id = MESSAGING_HA_REPLICATION_SECONDARY + "-" + FORM) private FormFragment replicationMasterForm;
    @FindBy(id = MESSAGING_HA_SHARED_STORE_SECONDARY + "-" + FORM) private FormFragment replicationSlaveForm;
    @FindBy(id = MESSAGING_HA_REPLICATION_COLOCATED + "-" + FORM) private FormFragment replicationColocatedForm;
    @FindBy(id = MESSAGING_HA_SHARED_STORE_PRIMARY + "-" + FORM) private FormFragment sharedStoreMasterForm;
    @FindBy(id = MESSAGING_HA_SHARED_STORE_SECONDARY + "-" + FORM) private FormFragment sharedStoreSlaveForm;
    @FindBy(id = MESSAGING_HA_SHARED_STORE_COLOCATED + "-" + FORM) private FormFragment sharedStoreColocatedForm;

    public EmptyState getEmptyState() {
        return emptyState;
    }

    public FormFragment getReplicationLiveOnlyForm() {
        return replicationLiveOnlyForm;
    }

    public FormFragment getReplicationMasterForm() {
        return replicationMasterForm;
    }

    public FormFragment getReplicationSlaveForm() {
        return replicationSlaveForm;
    }

    public FormFragment getReplicationColocatedForm() {
        return replicationColocatedForm;
    }

    public FormFragment getSharedStoreMasterForm() {
        return sharedStoreMasterForm;
    }

    public FormFragment getSharedStoreSlaveForm() {
        return sharedStoreSlaveForm;
    }

    public FormFragment getSharedStoreColocatedForm() {
        return sharedStoreColocatedForm;
    }
}
