package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.UNDERTOW)
public class UndertowPage extends BasePage {

    @FindBy(id = Ids.UNDERTOW_GLOBAL_SETTINGS + "-" + Ids.FORM)
    private FormFragment configurationForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

}
