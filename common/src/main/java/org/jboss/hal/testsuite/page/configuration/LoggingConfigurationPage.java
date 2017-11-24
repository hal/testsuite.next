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

@Place(NameTokens.LOGGING_CONFIGURATION)
public class LoggingConfigurationPage extends BasePage {

    @FindBy(id = "logging-config-form") private FormFragment configurationForm;
    @FindBy(id = "logging-root-logger-form") private FormFragment rootLoggerForm;
    @FindBy(id = "logging-category-table" + WRAPPER) private TableFragment categoryTable;
    @FindBy(id = "logging-category-form") private FormFragment categoryForm;
    @FindBy(id = "logging-handler-console-table" + WRAPPER) private TableFragment consoleHandlerTable;
    @FindBy(id = "logging-handler-console-form") private FormFragment consoleHandlerForm;
    @FindBy(id = "logging-handler-file-table" + WRAPPER) private TableFragment fileHandlerTable;
    @FindBy(id = "logging-handler-file-form") private FormFragment fileHandlerForm;
    @FindBy(id = "logging-handler-periodic-rotating-file-table" + WRAPPER) private TableFragment periodicHandlerTable;
    @FindBy(id = "logging-handler-periodic-rotating-file-form") private FormFragment periodicHandlerForm;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-table" + WRAPPER) private TableFragment periodicSizeHandlerTable;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-form") private FormFragment periodicSizeHandlerForm;
    @FindBy(id = "logging-handler-size-rotating-file-table" + WRAPPER) private TableFragment sizeHandlerTable;
    @FindBy(id = "logging-handler-size-rotating-file-form") private FormFragment sizeHandlerForm;
    @FindBy(id = "logging-handler-async-table" + WRAPPER) private TableFragment asyncHandlerTable;
    @FindBy(id = "logging-handler-async-form") private FormFragment asyncHandlerForm;
    @FindBy(id = "logging-handler-custom-table" + WRAPPER) private TableFragment customHandlerTable;
    @FindBy(id = "logging-handler-custom-form") private FormFragment customHandlerForm;
    @FindBy(id = "logging-handler-syslog-table" + WRAPPER) private TableFragment syslogHandlerTable;
    @FindBy(id = "logging-handler-syslog-form") private FormFragment syslogHandlerForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public FormFragment getRootLoggerForm() {
        return rootLoggerForm;
    }

    public TableFragment getCategoryTable() {
        return categoryTable;
    }

    public FormFragment getCategoryForm() {
        return categoryForm;
    }

    public TableFragment getConsoleHandlerTable() {
        return consoleHandlerTable;
    }

    public FormFragment getConsoleHandlerForm() {
        return consoleHandlerForm;
    }

    public TableFragment getFileHandlerTable() {
        return fileHandlerTable;
    }

    public FormFragment getFileHandlerForm() {
        return fileHandlerForm;
    }

    public TableFragment getPeriodicHandlerTable() {
        return periodicHandlerTable;
    }

    public FormFragment getPeriodicHandlerForm() {
        return periodicHandlerForm;
    }

    public TableFragment getPeriodicSizeHandlerTable() {
        return periodicSizeHandlerTable;
    }

    public FormFragment getPeriodicSizeHandlerForm() {
        return periodicSizeHandlerForm;
    }

    public TableFragment getSizeHandlerTable() {
        return sizeHandlerTable;
    }

    public FormFragment getSizeHandlerForm() {
        return sizeHandlerForm;
    }

    public TableFragment getAsyncHandlerTable() {
        return asyncHandlerTable;
    }

    public FormFragment getAsyncHandlerForm() {
        return asyncHandlerForm;
    }

    public TableFragment getCustomHandlerTable() {
        return customHandlerTable;
    }

    public FormFragment getCustomHandlerForm() {
        return customHandlerForm;
    }

    public TableFragment getSyslogHandlerTable() {
        return syslogHandlerTable;
    }

    public FormFragment getSyslogHandlerForm() {
        return syslogHandlerForm;
    }
}
