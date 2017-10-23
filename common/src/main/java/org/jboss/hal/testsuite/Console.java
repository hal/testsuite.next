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
package org.jboss.hal.testsuite;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.location.exception.LocationException;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FinderFragment;
import org.jboss.hal.testsuite.fragment.FooterFragment;
import org.jboss.hal.testsuite.fragment.HeaderFragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.hal.resources.CSS.alertSuccess;
import static org.jboss.hal.resources.CSS.navbar;
import static org.jboss.hal.resources.CSS.toastNotificationsListPf;

/**
 * Holds global methods and provides access to central classes such as {@linkplain HeaderFragment header}, {@linkplain
 * FooterFragment footer}, {@linkplain FinderFragment finder} and {@linkplain DialogFragment dialogs}.
 *
 * <p>Use Arquillian dependency injection to inject an instance of this class into your test class, page or page
 * fragment:</p>
 * <pre>
 * {@code @}RunWith(Arquillian.class)
 * public class YourTest {
 *     {@code @}Drone private WebDriver browser;
 *     {@code @}Inject private Console console;
 *     ...
 * }
 * </pre>
 */
public class Console {

    private static final long DEFAULT_PAGE_LOAD_TIMEOUT = 30;

    @Drone private WebDriver browser;
    @ArquillianResource private URL url;


    // ------------------------------------------------------ navigation

    /** Waits until the console is loaded, that is until the root container is present. */
    public void waitUntilLoaded() {
        waitUntilLoaded(Ids.ROOT_CONTAINER);
    }

    public void waitUntilLoaded(String id) {
        waitGui().withTimeout(DEFAULT_PAGE_LOAD_TIMEOUT, SECONDS)
                .until().element(By.id(id))
                .is().present();
    }


    // ------------------------------------------------------ notifications

    /** Verifies that a success notification is visible */
    public void success() {
        WebElement element = browser.findElement(By.cssSelector("." + toastNotificationsListPf + " ." + alertSuccess));
        // use waitModel() since it might take some time until the notification is visible
        waitModel().until().element(element).is().visible();
    }


    // ------------------------------------------------------ fragment access

    /** Navigates to the specified finder token, creates the finder fragment */
    public FinderFragment finder(String token) {
        try {
            String absoluteUrl = new URL(url, token).toExternalForm();
            browser.navigate().to(absoluteUrl);
            waitUntilLoaded();
            FinderFragment finder = createPageFragment(FinderFragment.class, browser.findElement(By.id(Ids.FINDER)));
            finder.initToken(token);
            return finder;
        } catch (MalformedURLException e) {
            throw new LocationException("URL to construct is malformed.", e.getCause());
        }
    }

    public HeaderFragment header() {
        return createPageFragment(HeaderFragment.class, browser.findElement(By.cssSelector("nav." + navbar)));
    }

    public FooterFragment footer() {
        return createPageFragment(FooterFragment.class, browser.findElement(By.cssSelector("footer.footer")));
    }

    /** Returns the currently opened dialog. */
    public DialogFragment dialog() {
        return dialog(DialogFragment.class);
    }

    /** Returns the currently opened add resource dialog. */
    public AddResourceDialogFragment addResourceDialog() {
        return dialog(AddResourceDialogFragment.class);
    }

    private <T extends DialogFragment> T dialog(Class<T> dialogClass) {
        WebElement dialogElement = browser.findElement(By.id(Ids.HAL_MODAL));
        T dialog = createPageFragment(dialogClass, dialogElement);
        waitGui().until().element(dialogElement).is().visible();
        return dialog;
    }
}
