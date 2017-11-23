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

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.JCA)
public class JcaPage extends BasePage {

    @FindBy(id = Ids.JCA_TAB_CONTAINER) private TabsFragment configurationTabs;
    @FindBy(id = Ids.JCA_CCM_FORM) private FormFragment cachedConnectionManagerForm;
    @FindBy(id = Ids.JCA_ARCHIVE_VALIDATION_FORM) private FormFragment archiveValidationForm;
    @FindBy(id = Ids.JCA_BEAN_VALIDATION_FORM) private FormFragment beanValidationForm;
    @FindBy(id = Ids.JCA_TRACER_FORM) private FormFragment tracerForm;
    @FindBy(id = Ids.JCA_BOOTSTRAP_CONTEXT_TABLE + WRAPPER) private TableFragment bootstrapContextTable;
    @FindBy(id = Ids.JCA_BOOTSTRAP_CONTEXT_FORM) private FormFragment bootstrapContextForm;
    @FindBy(id = Ids.JCA_WORKMANAGER_TABLE + WRAPPER) private TableFragment wmTable;
    @FindBy(id = Ids.JCA_WORKMANAGER + "-" + Ids.JCA_THREAD_POOL_TABLE + WRAPPER) private TableFragment wmThreadPoolTable;
    @FindBy(id = Ids.JCA_WORKMANAGER + "-" + Ids.JCA_THREAD_POOL_TAB_CONTAINER) private TabsFragment wmThreadPoolTabs;
    @FindBy(id = Ids.JCA_WORKMANAGER + "-" + Ids.JCA_THREAD_POOL_ATTRIBUTES_FORM) private FormFragment wmThreadPoolAttributesForm;
    @FindBy(id = Ids.JCA_WORKMANAGER + "-" + Ids.JCA_THREAD_POOL_SIZING_FORM) private FormFragment wmThreadPoolSizingForm;

    public TabsFragment getConfigurationTabs() {
        return configurationTabs;
    }

    public FormFragment getCachedConnectionManagerForm() {
        return cachedConnectionManagerForm;
    }

    public FormFragment getArchiveValidationForm() {
        return archiveValidationForm;
    }

    public FormFragment getBeanValidationForm() {
        return beanValidationForm;
    }

    public FormFragment getTracerForm() {
        return tracerForm;
    }

    public TableFragment getBootstrapContextTable() {
        return bootstrapContextTable;
    }

    public FormFragment getBootstrapContextForm() {
        return bootstrapContextForm;
    }

    public TableFragment getWmTable() {
        return wmTable;
    }

    public TableFragment getWmThreadPoolTable() {
        return wmThreadPoolTable;
    }

    public TabsFragment getWmThreadPoolTabs() {
        return wmThreadPoolTabs;
    }

    public FormFragment getWmThreadPoolAttributesForm() {
        return wmThreadPoolAttributesForm;
    }

    public FormFragment getWmThreadPoolSizingForm() {
        return wmThreadPoolSizingForm;
    }
}
