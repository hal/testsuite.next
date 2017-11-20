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
    @FindBy(id = "ejb-configuration-form") private FormFragment configurationForm;

    // container / thread-pool page
    @FindBy(id = "ejb-thread-pool-table_wrapper") private TableFragment threadPoolTable;
    @FindBy(id = "ejb-thread-pool-form") private FormFragment threadPoolForm;

    // container / remoting profile
    @FindBy(id = "ejb-remoting-profile-table_wrapper") private TableFragment remotingProfileTable;
    @FindBy(id = "ejb-remoting-profile-form") private FormFragment remotingProfileForm;

    // bean pool
    @FindBy(id = "ejb-bean-pool-table_wrapper") private TableFragment beanPoolTable;
    @FindBy(id = "ejb-bean-pool-form") private FormFragment beanPoolForm;

    // state management / cache
    @FindBy(id = "ejb-cache-table_wrapper") private TableFragment cacheTable;
    @FindBy(id = "ejb-cache-form") private FormFragment cacheForm;

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
}
