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

import org.jboss.arquillian.graphene.page.Location;
import org.jboss.hal.testsuite.arquillian.HalScheme;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location(scheme = HalScheme.class, value = "#batch-jberet-configuration${profile}")
public class BatchPage extends BasePage {

    @FindBy(id = "batch-configuration-item") private WebElement configurationItem;
    @FindBy(id = "batch-configuration-form") private FormFragment configurationForm;

    @FindBy(id = "batch-in-memory-job-repo-item") private WebElement inMemoryItem;
    @FindBy(id = "batch-in-memory-job-repo-table_wrapper") private TableFragment inMemoryTable;

    @FindBy(id = "batch-jdbc-job-repo-item") private WebElement jdbcItem;
    @FindBy(id = "batch-jdbc-job-repo-table_wrapper") private TableFragment jdbcTable;
    @FindBy(id = "batch-jdbc-job-repo-form") private FormFragment jdbcForm;

    @FindBy(id = "batch-thread-factory-item") private WebElement threadFactoryItem;
    @FindBy(id = "batch-thread-factory-table_wrapper") private TableFragment threadFactoryTable;
    @FindBy(id = "batch-thread-factory-form") private FormFragment threadFactoryForm;

    @FindBy(id = "batch-thread-pool-item") private WebElement threadPoolItem;
    @FindBy(id = "batch-thread-pool-table_wrapper") private TableFragment threadPoolTable;
    @FindBy(id = "batch-thread-pool-form") private FormFragment threadPoolForm;

    public WebElement getConfigurationItem() {
        return configurationItem;
    }

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public WebElement getInMemoryItem() {
        return inMemoryItem;
    }

    public TableFragment getInMemoryTable() {
        return inMemoryTable;
    }

    public WebElement getJdbcItem() {
        return jdbcItem;
    }

    public TableFragment getJdbcTable() {
        return jdbcTable;
    }

    public FormFragment getJdbcForm() {
        return jdbcForm;
    }

    public WebElement getThreadFactoryItem() {
        return threadFactoryItem;
    }

    public TableFragment getThreadFactoryTable() {
        return threadFactoryTable;
    }

    public FormFragment getThreadFactoryForm() {
        return threadFactoryForm;
    }

    public WebElement getThreadPoolItem() {
        return threadPoolItem;
    }

    public TableFragment getThreadPoolTable() {
        return threadPoolTable;
    }

    public FormFragment getThreadPoolForm() {
        return threadPoolForm;
    }
}
