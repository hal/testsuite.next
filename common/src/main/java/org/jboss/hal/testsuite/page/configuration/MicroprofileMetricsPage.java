package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.MICRO_PROFILE_METRICS)
public class MicroprofileMetricsPage extends BasePage {

    @FindBy(id = Ids.MICRO_PROFILE_METRICS_FORM)
    private FormFragment microprofileMetricsForm;

    public FormFragment getMicroprofileMetricsForm() {
        return microprofileMetricsForm;
    }
}
