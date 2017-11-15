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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

/** Fragment for the vertical navigation. Provides methods to select primary and secondary items. */
public class VerticalNavigationFragment {

    @Root private WebElement root;

    public void selectPrimary(String id) {
        selectItem(id);
    }

    public void selectSecondary(String primaryId, String secondaryId) {
        root.findElement(By.cssSelector("#" + primaryId + " > a")).click();
        waitGui().until().element(By.id(primaryId + "-secondary")).is().visible();
        selectItem(secondaryId);
    }

    private void selectItem(String id) {
        root.findElement(By.cssSelector("#" + id + " > a")).click();
        waitGui().until().element(By.cssSelector("[data-vn-item-for=" + id + "]")).is().visible();
    }
}
