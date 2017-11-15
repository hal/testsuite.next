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

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.BATCH_CONFIGURATION)
public class BatchPage extends BasePage {

    @FindBy(id = "batch-configuration-form") private FormFragment configurationForm;
    @FindBy(id = "batch-in-memory-job-repo-table" + WRAPPER) private TableFragment inMemoryTable;
    @FindBy(id = "batch-jdbc-job-repo-table" + WRAPPER) private TableFragment jdbcTable;
    @FindBy(id = "batch-jdbc-job-repo-form") private FormFragment jdbcForm;
    @FindBy(id = "batch-thread-factory-table" + WRAPPER) private TableFragment threadFactoryTable;
    @FindBy(id = "batch-thread-factory-form") private FormFragment threadFactoryForm;
    @FindBy(id = "batch-thread-pool-table" + WRAPPER) private TableFragment threadPoolTable;
    @FindBy(id = "batch-thread-pool-form") private FormFragment threadPoolForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getInMemoryTable() {
        return inMemoryTable;
    }

    public TableFragment getJdbcTable() {
        return jdbcTable;
    }

    public FormFragment getJdbcForm() {
        return jdbcForm;
    }

    public TableFragment getThreadFactoryTable() {
        return threadFactoryTable;
    }

    public FormFragment getThreadFactoryForm() {
        return threadFactoryForm;
    }

    public TableFragment getThreadPoolTable() {
        return threadPoolTable;
    }

    public FormFragment getThreadPoolForm() {
        return threadPoolForm;
    }
}
