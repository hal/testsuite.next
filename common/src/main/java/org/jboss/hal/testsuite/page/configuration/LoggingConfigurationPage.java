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

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.openqa.selenium.WebElement;

public interface LoggingConfigurationPage {

     FormFragment getRootLoggerForm();

     TableFragment getCategoryTable();

     FormFragment getCategoryForm();

     TableFragment getConsoleHandlerTable();

     FormFragment getConsoleHandlerForm();

     TableFragment getFileHandlerTable();

     WebElement getNewFileInputElement();

     FormFragment getFileHandlerForm();

     WebElement getReadFileInputElement();

     TableFragment getPeriodicHandlerTable();

     WebElement getNewPeriodicFileInputElement();

     FormFragment getPeriodicHandlerForm();

     WebElement getReadPeriodicFileInputElement();

     TableFragment getPeriodicSizeHandlerTable();

     WebElement getNewPeriodicSizeFileInputElement();

     FormFragment getPeriodicSizeHandlerForm();

     WebElement getReadPeriodicSizeFileInputElement();

     TableFragment getSizeHandlerTable();

     WebElement getNewSizeFileInputElement();

     FormFragment getSizeHandlerForm();

     WebElement getReadSizeFileInputElement();

     TableFragment getAsyncHandlerTable();

     FormFragment getAsyncHandlerForm();

     TableFragment getCustomHandlerTable();

     FormFragment getCustomHandlerForm();

     TableFragment getSyslogHandlerTable();

     FormFragment getSyslogHandlerForm();

     TableFragment getPatternFormatterTable();

     FormFragment getPatternFormatterForm();

     TableFragment getCustomFormatterTable();

     FormFragment getCustomFormatterForm();

     TableFragment getJsonFormatterTable();

     FormFragment getJsonFormatterForm();

     TableFragment getXmlFormatterTable();

     FormFragment getXmlFormatterForm();

     void navigate();
}
