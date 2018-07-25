package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place("scattered-cache")
public class ScatteredCachePage extends BasePage {


    @FindBy(id = "scattered-cache-tab-container")
    private TabsFragment configurationTab;

    @FindBy(id = "scattered-cache-form")
    private FormFragment configurationForm;

    @FindBy(id = "scattered-cache-cache-component-expiration-form")
    private FormFragment expirationForm;

    @FindBy(id = "scattered-cache-cache-component-locking-form")
    private FormFragment lockingForm;

    public FormFragment getConfigurationForm() {
        configurationTab.select("scattered-cache-tab");
        return configurationForm;
    }

    public FormFragment getExpirationForm() {
        configurationTab.select("scattered-cache-cache-component-expiration-tab");
        return expirationForm;
    }

    public FormFragment getLockingForm() {
        configurationTab.select("scattered-cache-cache-component-locking-tab");
        return lockingForm;
    }
}
