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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("#configuration")
public class StandaloneConfigurationPage extends BasePage {

    @FindBy(id = "subsystems") private WebElement subsystems;
    @FindBy(id = "interfaces") private WebElement interfaces;
    @FindBy(id = "socket-bindings") private WebElement socketBindings;
    @FindBy(id = "paths") private WebElement paths;
    @FindBy(id = "system-properties") private WebElement systemProperties;
    @FindBy(css = "a[href='#runtime']") private WebElement runtimeLink;

    public WebElement getSubsystems() {
        return subsystems;
    }

    public WebElement getInterfaces() {
        return interfaces;
    }

    public WebElement getSocketBindings() {
        return socketBindings;
    }

    public WebElement getPaths() {
        return paths;
    }

    public WebElement getSystemProperties() {
        return systemProperties;
    }

    public WebElement getRuntimeLink() {
        return runtimeLink;
    }
}
