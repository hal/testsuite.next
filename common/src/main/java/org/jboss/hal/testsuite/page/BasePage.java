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
package org.jboss.hal.testsuite.page;

import java.net.URL;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class BasePage {

    @Drone protected WebDriver browser;
    @FindBy(id = Ids.ROOT_CONTAINER) private WebElement rootContainer;
    @Inject private Console console;
    @ArquillianResource private URL url;

    /** Navigates to the name token specified in the {@code @Place} annotation. */
    public void navigate() {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value()).build();
        browser.get(console.absoluteUrl(placeRequest));
        console.waitUntilPresent();
    }

    /**
     * Navigates to the name token specified in the {@code @Place} annotation appending the specified name/value pair.
     */
    public void navigate(String name, String value) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value())
                .with(name, value)
                .build();
        browser.get(console.absoluteUrl(placeRequest));
        console.waitUntilPresent();
    }

    public WebElement getRootContainer() {
        return rootContainer;
    }

    private Place assertPlace() {
        Place place = this.getClass().getAnnotation(Place.class);
        if (place == null) {
            throw new IllegalArgumentException(
                    String.format("The page object '%s' that you are navigating to is not annotated with @Place",
                            this.getClass().getSimpleName()));
        }
        return place;
    }
}
