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

@Place(NameTokens.EJB3_CONFIGURATION)
public class EJBConfigurationPage extends BasePage {

    // container page
    @FindBy(id = "ejb3-configuration-form") private FormFragment configurationForm;

    // container / thread-pool page
    @FindBy(id = "ejb3-thread-pool-table_wrapper") private TableFragment threadPoolTable;
    @FindBy(id = "ejb3-thread-pool-form") private FormFragment threadPoolForm;

    // container / remoting profile
    @FindBy(id = "ejb3-remoting-profile-table_wrapper") private TableFragment remotingProfileTable;
    @FindBy(id = "ejb3-remoting-profile-form") private FormFragment remotingProfileForm;

    // bean pool
    @FindBy(id = "ejb3-bean-pool-table_wrapper") private TableFragment beanPoolTable;
    @FindBy(id = "ejb3-bean-pool-form") private FormFragment beanPoolForm;

    // state management / cache
    @FindBy(id = "ejb3-cache-table_wrapper") private TableFragment cacheTable;
    @FindBy(id = "ejb3-cache-form") private FormFragment cacheForm;

    // state management / passivation
    @FindBy(id = "ejb3-passivation-table_wrapper") private TableFragment passivationTable;
    @FindBy(id = "ejb3-passivation-form") private FormFragment passivationForm;

    // services / async
    @FindBy(id = "ejb3-service-async-form") private FormFragment serviceAsyncForm;

    // services / identity
    @FindBy(id = "ejb3-service-identity-form") private FormFragment serviceIdentityForm;

    // services / iiop
    @FindBy(id = "ejb3-service-iiop-form") private FormFragment serviceIiopForm;

    // services / remote
    @FindBy(id = "ejb3-service-remote-form") private FormFragment serviceRemoteForm;

    // services / timer
    @FindBy(id = "ejb3-service-timer-form") private FormFragment serviceTimerForm;

    // mdb delivery
    @FindBy(id = "ejb3-mdb-delivery-group-table_wrapper") private TableFragment mdbDeliveryTable;
    @FindBy(id = "ejb3-mdb-delivery-group-form") private FormFragment mdbDeliveryForm;

    // security domain
    @FindBy(id = "ejb3-app-security-domain-table_wrapper") private TableFragment securityDomainTable;
    @FindBy(id = "ejb3-app-security-domain-form") private FormFragment securityDomainForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getThreadPoolTable() {
        return threadPoolTable;
    }

    public FormFragment getThreadPoolForm() {
        return threadPoolForm;
    }

    public TableFragment getRemotingProfileTable() {
        return remotingProfileTable;
    }

    public FormFragment getRemotingProfileForm() {
        return remotingProfileForm;
    }

    public TableFragment getBeanPoolTable() {
        return beanPoolTable;
    }

    public FormFragment getBeanPoolForm() {
        return beanPoolForm;
    }

    public TableFragment getCacheTable() {
        return cacheTable;
    }

    public FormFragment getCacheForm() {
        return cacheForm;
    }

    public TableFragment getPassivationTable() {
        return passivationTable;
    }

    public FormFragment getPassivationForm() {
        return passivationForm;
    }

    public FormFragment getServiceAsyncForm() {
        return serviceAsyncForm;
    }

    public FormFragment getServiceIdentityForm() {
        return serviceIdentityForm;
    }

    public FormFragment getServiceIiopForm() {
        return serviceIiopForm;
    }

    public FormFragment getServiceRemoteForm() {
        return serviceRemoteForm;
    }

    public FormFragment getServiceTimerForm() {
        return serviceTimerForm;
    }

    public TableFragment getMdbDeliveryTable() {
        return mdbDeliveryTable;
    }

    public FormFragment getMdbDeliveryForm() {
        return mdbDeliveryForm;
    }

    public TableFragment getSecurityDomainTable() {
        return securityDomainTable;
    }

    public FormFragment getSecurityDomainForm() {
        return securityDomainForm;
    }
}
