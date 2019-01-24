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
package org.jboss.hal.testsuite.fragment;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;

import static org.jboss.hal.resources.CSS.alertDanger;
import static org.jboss.hal.resources.CSS.alertInfo;
import static org.jboss.hal.resources.CSS.alertSuccess;
import static org.jboss.hal.resources.CSS.alertWarning;

public class AlertFragment {

    @Root private WebElement root;

    public boolean isSuccess() {
        return hasClass(alertSuccess);
    }

    public boolean isInfo() {
        return hasClass(alertInfo);
    }

    public boolean isWarning() {
        return hasClass(alertWarning);
    }

    public boolean isError() {
        return hasClass(alertDanger);
    }

    private boolean hasClass(String expected) {
        String cssClass = root.getAttribute("class");
        return cssClass != null && cssClass.contains(expected);
    }
}
