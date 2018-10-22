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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.TourWizardFragment;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.createPageFragment;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.testsuite.Selectors.contains;

@Place(NameTokens.HOMEPAGE)
public class HomePage extends BasePage {

    private static final String MODULES_SELECTOR = "a[data-element=moduleHeader]";
    @FindBy(css = MODULES_SELECTOR) private List<WebElement> modules;
    @FindBy(id = Ids.HEADER_USERNAME) private WebElement userElement;

    public List<WebElement> getModules() {
        return modules;
    }

    public void waitUntilHomePageIsLoaded() {
        Graphene.waitModel().until().element(By.cssSelector(MODULES_SELECTOR)).is().present();
    }

    /**
     * Navigates to {@code https://localhost:9993/console/ts.html}, this can be changed by passing
     * {@code suite.https.url} system property.
     */
    public void navigateViaHttps() {
        URL httpsBaseUrl;
        try {
            httpsBaseUrl = new URL(ConfigUtils.get("suite.https.url", "https://localhost:9993/console/ts.html"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(assertPlace().value()).build();
        browser.navigate().refresh();
        console.navigate(placeRequest, By.id(Ids.ROOT_CONTAINER), httpsBaseUrl);
    }

    public void logout() {
        userElement.click();
        By logoutSelector = By.cssSelector("a[data-element=logout]");
        Graphene.waitGui().until().element(logoutSelector).is().clickable();
        browser.findElement(logoutSelector).click();
    }

    public TourWizardFragment openTourWizard() {
        By wizardLinkSelector = ByJQuery.selector("a." + CSS.clickable + contains("Take a Tour"));
        getRootContainer().findElement(wizardLinkSelector).click();
        waitGui().until().element(TourWizardFragment.WIZARD_SELECTOR).is().visible();
        return createPageFragment(TourWizardFragment.class, getRootContainer());
    }
}
