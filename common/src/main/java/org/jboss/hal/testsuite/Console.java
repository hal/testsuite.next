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
import java.util.List;
import java.util.Set;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatException;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.location.exception.LocationException;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.ConfirmationDialogFragment;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FooterFragment;
import org.jboss.hal.testsuite.fragment.HeaderFragment;
import org.jboss.hal.testsuite.fragment.VerticalNavigationFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.FinderSegment;
import org.jboss.hal.testsuite.util.Library;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.hal.resources.CSS.*;
import static org.jboss.hal.resources.UIConstants.MEDIUM_TIMEOUT;
import static org.jboss.hal.testsuite.page.Places.finderPlace;
import static org.junit.Assert.assertEquals;

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

    @Drone private WebDriver browser;
    @ArquillianResource private URL baseUrl;
    private TokenFormatter tokenFormatter;

    public Console() {
        tokenFormatter = new HalTokenFormatter();
    }


    // ------------------------------------------------------ navigation

    /** Navigates to the place request and waits until the id {@link Ids#ROOT_CONTAINER} is present. */
    public void navigate(PlaceRequest request) {
        navigate(request, By.id(Ids.ROOT_CONTAINER));
    }

    /** Navigates to the place request and waits until the selector is present. */
    public void navigate(PlaceRequest request, By selector) {
        String fragment = tokenFormatter.toPlaceToken(request);
        String hashFragment = fragment.startsWith("#") ? fragment : "#" + fragment;
        try {
            URL url = new URL(baseUrl, hashFragment);
            browser.navigate().to(url);
            waitModel().until().element(selector).is().present();
            browser.manage().window().maximize();
        } catch (MalformedURLException e) {
            throw new LocationException("Malformed URL: ", e.getCause());
        }
    }

    public void reload() {
        browser.navigate().refresh();
        waitModel().until().element(By.id(Ids.ROOT_CONTAINER)).is().present();
    }

    public void verify(PlaceRequest placeRequest) {
        String expected = tokenFormatter.toPlaceToken(placeRequest);
        String actual = StringUtils.substringAfter(browser.getCurrentUrl(), "#");
        assertEquals(expected, actual);
    }


    // ------------------------------------------------------ notifications

    /** Waits until all notifications are gone. */
    public void waitNoNotification() {
        waitModel().until().element(By.cssSelector("." + toastNotificationsListPf + ":empty")).is().present();
    }

    /** Verifies that a success notification is visible */
    public void verifySuccess() {
        verifyNotification(alertSuccess);
    }

    /** Verifies that an error notification is visible */
    public void verifyError() {
        verifyNotification(alertDanger);
    }

    private void verifyNotification(String css) {
        waitModel().until() // use waitModel() since it might take some time until the notification is visible
                .element(By.cssSelector("." + toastNotificationsListPf + " ." + css))
                .is().visible();
    }


    // ------------------------------------------------------ fragment access (a-z)

    /** Returns the currently opened add resource dialog. */
    public AddResourceDialogFragment addResourceDialog() {
        return dialog(AddResourceDialogFragment.class);
    }

    /** Returns the currently opened confirmation dialog. */
    public ConfirmationDialogFragment confirmationDialog() {
        return dialog(ConfirmationDialogFragment.class);
    }

    /** Returns the currently opened dialog. */
    public DialogFragment dialog() {
        return dialog(DialogFragment.class);
    }

    private <T extends DialogFragment> T dialog(Class<T> dialogClass) {
        Library.letsSleep(MEDIUM_TIMEOUT);
        WebElement dialogElement = browser.findElement(By.id(Ids.HAL_MODAL));
        waitGui().until().element(dialogElement).is().visible();
        return createPageFragment(dialogClass, dialogElement);
    }

    /** Navigates to the specified token, creates and returns the finder fragment */
    public FinderFragment finder(String token) {
        return finder(token, null);
    }

    /** Navigates to the specified token, selects the finder path, creates and returns the finder fragment */
    public FinderFragment finder(String token, FinderPath path) {
        By selector = By.id(Ids.FINDER);
        if (path != null && !path.isEmpty()) {
            FinderSegment segment = path.last();
            if (segment.getItemId() != null) {
                selector = By.id(segment.getItemId());
            } else if (segment.getColumnId() != null) {
                selector = By.id(segment.getColumnId());
            }
        }
        navigate(finderPlace(token, path), selector);
        return createPageFragment(FinderFragment.class, browser.findElement(selector));
    }

    public FooterFragment footer() {
        return createPageFragment(FooterFragment.class, browser.findElement(By.cssSelector("footer.footer")));
    }

    public HeaderFragment header() {
        return createPageFragment(HeaderFragment.class, browser.findElement(By.cssSelector("nav." + navbar)));
    }

    public VerticalNavigationFragment verticalNavigation() {
        return createPageFragment(VerticalNavigationFragment.class,
                browser.findElement(By.cssSelector("." + navPfVerticalHal)));
    }

    public WizardFragment wizard() {
        Library.letsSleep(MEDIUM_TIMEOUT);
        WebElement wizardElement = browser.findElement(By.id(Ids.HAL_WIZARD));
        waitGui().until().element(wizardElement).is().visible();
        return createPageFragment(WizardFragment.class, wizardElement);
    }


    // ------------------------------------------------------ elements

    /**
     * Scrolls the specified element into the visible area of the browser window.
     * @param element to scroll to
     * @param scrollIntoViewOptions - see
     * <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollIntoView#Parameters">related js documentation</a>
     * @return provided element
     */
    public WebElement scrollIntoView(WebElement element, String scrollIntoViewOptions) {
        JavascriptExecutor js = (JavascriptExecutor) browser;
        js.executeScript("arguments[0].scrollIntoView(" + scrollIntoViewOptions + ");", element);
        return element;
    }

    /**
     * The top of the element will be aligned to the top of the visible area of the scrollable ancestor.
     * @param element to scroll to
     * @return provided element
     */
    public WebElement scrollIntoView(WebElement element) {
        return scrollIntoView(element, "true");
    }

    /**
     * The bottom of the element will be aligned as much as possible to the bottom of the visible area of the scrollable ancestor.
     * @param element to scroll to
     * @return provided element
     */
    public WebElement scrollToBottom(WebElement element) {
        return scrollIntoView(element, "false");
    }

    // ------------------------------------------------------ inner classes


    private static class HalTokenFormatter implements TokenFormatter {

        @Override
        public String toHistoryToken(List<PlaceRequest> placeRequestHierarchy) throws TokenFormatException {
            throw new UnsupportedOperationException();
        }

        @Override
        public PlaceRequest toPlaceRequest(String placeToken) throws TokenFormatException {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<PlaceRequest> toPlaceRequestHierarchy(String historyToken) throws TokenFormatException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toPlaceToken(PlaceRequest placeRequest) throws TokenFormatException {
            StringBuilder builder = new StringBuilder();
            builder.append(placeRequest.getNameToken());
            Set<String> params = placeRequest.getParameterNames();
            if (params != null) {
                for (String param : params) {
                    builder.append(";")
                            .append(param)
                            .append("=")
                            .append(placeRequest.getParameter(param, null));
                }
            }
            return builder.toString();
        }
    }
}
