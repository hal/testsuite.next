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
package org.jboss.hal.testsuite.test.configuration.logging;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.resources.CSS.formControl;
import static org.jboss.hal.resources.CSS.formControlStatic;

/** Fragment for the composite form input (name + path) used in the logging subsystem. */
@SuppressWarnings("WeakerAccess")
public class FileInputFragment {

    @FindBy(css = "input[type=text]." + formControl) private List<WebElement> editing;
    @FindBy(css = "." + formControlStatic) private List<WebElement> readonly;

    public void setPath(String value) {
        WebElement inputElement = editing.get(0);
        inputElement.clear();
        waitGui().until().element(inputElement).value().equalTo("");
        inputElement.sendKeys(value);
        waitGui().until().element(inputElement).value().equalTo(value);
    }

    public String getPath() {
        return readonly.get(0).getText();
    }
}
