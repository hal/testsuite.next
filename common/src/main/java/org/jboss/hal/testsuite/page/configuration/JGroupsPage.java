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

import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.JGROUPS)
public class JGroupsPage extends BasePage {

    @FindBy(id = Ids.JGROUPS_FORM) private FormFragment configurationForm;

    // ----------- stack
    @FindBy(id = Ids.JGROUPS_STACK_CONFIG + "-" + TABLE + WRAPPER) private TableFragment stackTable;
    @FindBy(id = Ids.JGROUPS_STACK_CONFIG + "-" + Ids.FORM) private FormFragment stackForm;

    // ----------- stack / relay
    @FindBy(id = Ids.JGROUPS_RELAY + "-" + TABLE + WRAPPER) private TableFragment relayTable;
    @FindBy(id = Ids.JGROUPS_RELAY + "-" + Ids.FORM) private FormFragment relayForm;

    // ----------- stack / relay / remote site
    @FindBy(id = Ids.JGROUPS_REMOTE_SITE + "-" + TABLE + WRAPPER) private TableFragment relayRemoteSiteTable;
    @FindBy(id = Ids.JGROUPS_REMOTE_SITE + "-" + Ids.FORM) private FormFragment relayRemoteSiteForm;

    // ----------- stack / protocol
    @FindBy(id = Ids.JGROUPS_PROTOCOL + "-" + TABLE + WRAPPER) private TableFragment protocolTable;
    @FindBy(id = Ids.JGROUPS_PROTOCOL + "-" + Ids.FORM) private FormFragment protocolForm;

    // ----------- stack / transport
    @FindBy(id = Ids.JGROUPS_TRANSPORT + "-" + TABLE + WRAPPER) private TableFragment transportTable;
    @FindBy(id = Ids.JGROUPS_TRANSPORT + "-" + Ids.FORM) private FormFragment transportAttributesForm;
    @FindBy(id = Ids.JGROUPS_TRANSPORT_THREADPOOL_DEFAULT_FORM) private FormFragment transportThreadPoolDefaultForm;
    @FindBy(id = Ids.JGROUPS_TRANSPORT_THREADPOOL_INTERNAL_FORM) private FormFragment transportThreadPoolInternalForm;
    @FindBy(id = Ids.JGROUPS_TRANSPORT_THREADPOOL_OOB_FORM) private FormFragment transportThreadPoolOobForm;
    @FindBy(id = Ids.JGROUPS_TRANSPORT_THREADPOOL_TIMER_FORM) private FormFragment transportThreadPoolTimerForm;
    @FindBy(id = Ids.JGROUPS_TRANSPORT_THREADPOOL_TAB_CONTAINER) private TabsFragment transportThreadPoolTab;

    // ----------- channel
    @FindBy(id = Ids.JGROUPS_CHANNEL_CONFIG + "-" + TABLE + WRAPPER) private TableFragment channelTable;
    @FindBy(id = Ids.JGROUPS_CHANNEL_CONFIG + "-" + Ids.FORM) private FormFragment channelForm;

    // ----------- channel / fork
    @FindBy(id = Ids.JGROUPS_CHANNEL_FORK + "-" + TABLE + WRAPPER) private TableFragment channelForkTable;

    // ----------- channel
    @FindBy(id = Ids.JGROUPS_CHANNEL_FORK_PROTOCOL + "-" + TABLE + WRAPPER) private TableFragment forkProtocolTable;
    @FindBy(id = Ids.JGROUPS_CHANNEL_FORK_PROTOCOL + "-" + Ids.FORM) private FormFragment forkProtocolForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getStackTable() {
        return stackTable;
    }

    public FormFragment getStackForm() {
        return stackForm;
    }

    public TableFragment getRelayTable() {
        return relayTable;
    }

    public FormFragment getRelayForm() {
        return relayForm;
    }

    public TableFragment getRelayRemoteSiteTable() {
        return relayRemoteSiteTable;
    }

    public FormFragment getRelayRemoteSiteForm() {
        return relayRemoteSiteForm;
    }

    public TableFragment getProtocolTable() {
        return protocolTable;
    }

    public FormFragment getProtocolForm() {
        return protocolForm;
    }

    public TableFragment getTransportTable() {
        return transportTable;
    }

    public FormFragment getTransportAttributesForm() {
        return transportAttributesForm;
    }

    public FormFragment getTransportThreadPoolDefaultForm() {
        return transportThreadPoolDefaultForm;
    }

    public FormFragment getTransportThreadPoolInternalForm() {
        return transportThreadPoolInternalForm;
    }

    public FormFragment getTransportThreadPoolOobForm() {
        return transportThreadPoolOobForm;
    }

    public FormFragment getTransportThreadPoolTimerForm() {
        return transportThreadPoolTimerForm;
    }

    public TabsFragment getTransportThreadPoolTab() {
        return transportThreadPoolTab;
    }

    public TableFragment getChannelTable() {
        return channelTable;
    }

    public FormFragment getChannelForm() {
        return channelForm;
    }

    public TableFragment getChannelForkTable() {
        return channelForkTable;
    }

    public TableFragment getForkProtocolTable() {
        return forkProtocolTable;
    }

    public FormFragment getForkProtocolForm() {
        return forkProtocolForm;
    }
}
