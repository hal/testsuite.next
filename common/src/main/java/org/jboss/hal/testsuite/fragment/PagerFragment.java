/*
 * Copyright 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.fragment;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Fragment for the table pager. Use {@link TableFragment#getPager()} to get an instance.
 */
public class PagerFragment {

    private static final String VALUE = "value";

    @Root private WebElement root;

    public void goToFirstPage() {
        if (isFirstPage()) {
            return;
        }
        clickArrow("first");
    }

    public void goToPreviousPage() {
        if (isFirstPage()) {
            return;
        }
        clickArrow("prev");
    }

    public void goToNextPage() {
        if (isLastPage()) {
            return;
        }
        clickArrow("next");
    }

    public void goToLastPage() {
        if (isLastPage()) {
            return;
        }
        clickArrow("last");
    }

    public boolean isFirstPage() {
        return getCurrentPageNumber() == 1;
    }

    public boolean isLastPage() {
        return getCurrentPageNumber() >= getTotalPageNumber();
    }

    public Integer getTotalPageNumber() {
        return Integer.valueOf(root.findElement(By.cssSelector(".paginate_of > b")).getText());
    }

    public Integer getCurrentPageNumber() {
        return Integer.valueOf(getCurrentPageNumberString());
    }

    private void clickArrow(String className) {
        String originalCurrentPageNumberString = getCurrentPageNumberString();
        root.findElement(By.cssSelector("." + className + " span")).click();
        Graphene.waitModel().until().element(getCurrentPageNumberElement()).attribute(VALUE).not().equalTo(originalCurrentPageNumberString);
    }

    private WebElement getCurrentPageNumberElement() {
        return root.findElement(By.className("paginate_input"));
    }

    private String getCurrentPageNumberString() {
        return getCurrentPageNumberElement().getAttribute(VALUE);
    }
}
