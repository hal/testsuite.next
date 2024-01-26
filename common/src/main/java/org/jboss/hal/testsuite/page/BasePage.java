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

import java.util.Map;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public abstract class BasePage extends AbstractPage {

    @Override
    public void navigate() {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value()).build();
        browser.navigate().refresh();
        console.navigate(placeRequest);
    }

    /**
     * Navigates to the name token specified in the {@code @Place} annotation appending the specified name/value pair.
     */
    public void navigate(String name, String value) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value())
                .with(name, value)
                .build();
        console.navigate(placeRequest);
    }

    /**
     * Navigates to the name token specified in the {@code @Place} annotation appending the specified name/value pair.
     */
    public void navigate(Map<String, String> params) {
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value())
                .with(params)
                .build();
        console.navigate(placeRequest);
    }

    /**
     * Navigates again to the name token specified in the {@code @Place} annotation appending the specified name/value pair with forced browser refresh. <br />
     * Use only in case that you need to refresh browser and navigate again to the same name token to e.g. reload new resource.
     * In other cases use {@link #navigate(String, String)} instead.
     */
    public void navigateAgain(String name, String value) {
        browser.navigate().refresh();
        navigate(name, value);
        if (!console.verifyNoError()) {
            // try again, the resource may be unavailable
            browser.navigate().refresh();
            navigate(name, value);
        }
    }
}
