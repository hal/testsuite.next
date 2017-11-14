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
import org.jboss.hal.resources.CSS;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Fragment for 'pages' inside views to manage deeply nested resources. Not related to real pages with a token.
 * Typically pages in views consists of a main page with a table which contains a link to sub page(s). The sub page(s)
 * contain a {@linkplain PageBreadcrumbFragment breadcrumb} to go back to the main page.
 */
public class PagesFragment {

    @Root private WebElement root;
    @FindBy(css = "ol." + CSS.breadcrumb + "." + CSS.page) private PageBreadcrumbFragment breadcrumb;

    public PageBreadcrumbFragment breadcrumb() {
        return breadcrumb;
    }

    public WebElement getRoot() {
        return root;
    }
}
