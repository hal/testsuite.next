/*
 * Copyright 2015-2021 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.page.runtime;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.HostsPreviewFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HOST;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HOSTS;
import static org.jboss.hal.resources.Ids.DOMAIN_BROWSE_BY;


@Place(NameTokens.RUNTIME)
public class HostsPage extends BasePage {
    public static final String MAIN_SERVER_GROUP = "main-server-group";

    @FindBy(id = "server") private WebElement serverElement;
    @FindBy(id = "rss") private WebElement rssElement;
    @FindBy(id = "undertow-runtime-deployment-column") private WebElement undertowRuntimeDeploymentColumn;
    @FindBy(id = "hal-finder-preview") private WebElement halFinderPreview;

    @Override
    public void navigate() {
        console.finder(NameTokens.RUNTIME, new FinderPath().append(DOMAIN_BROWSE_BY, HOSTS)).column(HOST);
    }

    public HostsPreviewFragment navigateToHostsFragment() {
        return navigateAndReturnHostsFragment();
    }

    private HostsPreviewFragment navigateAndReturnHostsFragment() {
        return console.finder(NameTokens.RUNTIME, new FinderPath().append(DOMAIN_BROWSE_BY, HOSTS))
                .preview(HostsPreviewFragment.class);
    }

    /**
     * Return all hosts (Webelemets) from  address Runtime > Hosts
     * @return
     */
    public List<WebElement> getHostsItems() {
        return browser.findElement(By.id("host")).findElement(By.className("pinnable")).findElements(By.className("item-text"));
    }

    /**
     * Wait for load Server view (Runtime > Hosts > HC > Server) and return it as WebElement
     * @return
     */
    public WebElement getServerElement() {
        waitGui().until().element(serverElement).is().visible();
        return serverElement;
    }

    /**
     * Wait for load Monitor view (Runtime > Hosts > HC > Server > Monitor) and return it as WebElemnt
     * This webelement is displayed as Monitor fragment but has id set to rss.
     * @return
     */
    public WebElement getRssElement() {
        waitGui().until().element(rssElement).is().visible();
        return rssElement;
    }

    /**
     * Wait for load view with info about deployed application (Runtime > Hosts > HC > Server > Monitor > Web > Deployment > deployed application) and return it as WebElement
     * @return
     */
    public WebElement getHalFinderPreview() {
        waitGui().until().element(halFinderPreview).is().visible();
        return halFinderPreview;
    }

    /**
     * Wait for load Deployment view  (Runtime > Hosts > HC > Server > Monitor > Web > Deployment) and return it as Webelement
     * @return
     */
    public WebElement getUndertowRuntimeDeploymentColumn() {
        waitGui().until().element(undertowRuntimeDeploymentColumn).is().visible();
        return  undertowRuntimeDeploymentColumn;
    }



    /**
     * Go to Runtime > Hosts > HC host > server-one > Web (Undertow) > Deployment
     */
    public void goToDeploymentOnHcServer() throws InterruptedException {
        getHostsItems().get(1).click();

        WebElement localServerelement = getServerElement();
        //Sometimes page is displayed but no server is loaded so we need reload
        if (localServerelement.findElements(By.className("item-text")).isEmpty()) {
            localServerelement.findElement(By.id("server-refresh")).click();
            TimeUnit.SECONDS.sleep(1);
        }
        waitGui().until().element(localServerelement.findElement(By.className("pinnable")).findElement(By.cssSelector("small[title=\"" + MAIN_SERVER_GROUP + "\"]"))).is().visible();
        localServerelement.findElement(By.className("pinnable")).findElement(By.cssSelector("small[title=\"" + MAIN_SERVER_GROUP + "\"]")).click();
        waitGui().until().element(By.id("undertow"));
        getRssElement().findElement(By.id("undertow")).click();
        browser.findElement(By.id("undertow-runtime")).findElement(By.id("deployment")).click();
    }

}
