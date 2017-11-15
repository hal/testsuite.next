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
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.EE)
public class EEPage extends BasePage {

    @FindBy(id = Ids.EE_ATTRIBUTES_FORM) private FormFragment attributesForm;
    @FindBy(id = Ids.EE_GLOBAL_MODULES_TABLE + WRAPPER) private TableFragment globalModulesTable;
    @FindBy(id = Ids.EE_DEFAULT_BINDINGS_FORM) private FormFragment defaultBindingsForm;
    @FindBy(id = Ids.EE_CONTEXT_SERVICE + "-" + Ids.TABLE + WRAPPER) private TableFragment contextServiceTable;
    @FindBy(id = Ids.EE_CONTEXT_SERVICE + "-" + Ids.FORM) private FormFragment contextServiceForm;
    @FindBy(id = Ids.EE_MANAGED_EXECUTOR + "-" + Ids.TABLE + WRAPPER) private TableFragment executorTable;
    @FindBy(id = Ids.EE_MANAGED_EXECUTOR + "-" + Ids.FORM) private FormFragment executorForm;
    @FindBy(id = Ids.EE_MANAGED_EXECUTOR_SCHEDULED + "-" + Ids.TABLE + WRAPPER) private TableFragment executorScheduledTable;
    @FindBy(id = Ids.EE_MANAGED_EXECUTOR_SCHEDULED + "-" + Ids.FORM) private FormFragment executorScheduledForm;
    @FindBy(id = Ids.EE_MANAGED_THREAD_FACTORY + "-" + Ids.TABLE + WRAPPER) private TableFragment threadFactoryTable;
    @FindBy(id = Ids.EE_MANAGED_THREAD_FACTORY + "-" + Ids.FORM) private FormFragment threadFactoryForm;

    public FormFragment getAttributesForm() {
        return attributesForm;
    }

    public TableFragment getGlobalModulesTable() {
        return globalModulesTable;
    }

    public FormFragment getDefaultBindingsForm() {
        return defaultBindingsForm;
    }

    public TableFragment getContextServiceTable() {
        return contextServiceTable;
    }

    public FormFragment getContextServiceForm() {
        return contextServiceForm;
    }

    public TableFragment getExecutorTable() {
        return executorTable;
    }

    public FormFragment getExecutorForm() {
        return executorForm;
    }

    public TableFragment getExecutorScheduledTable() {
        return executorScheduledTable;
    }

    public FormFragment getExecutorScheduledForm() {
        return executorScheduledForm;
    }

    public TableFragment getThreadFactoryTable() {
        return threadFactoryTable;
    }

    public FormFragment getThreadFactoryForm() {
        return threadFactoryForm;
    }
}
