package org.jboss.hal.testsuite.fragment.ssl;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.RadioButtonGroup;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class EnableSslWizard extends WizardFragment {

    @Drone
    private WebDriver browser;

    private static final String
        MUTUAL = "mutual",
        KEY_STORE_STRATEGY = "key-store-strategy",
        CONFIGURATION_FORM_ID = Ids.build(Ids.MANAGEMENT, Ids.ENABLE_SSL, "edit", Ids.FORM),
        REVIEW_FORM_ID = Ids.build(Ids.MANAGEMENT, Ids.ENABLE_SSL, "read", Ids.FORM);

    public EnableSslWizard enableMutualAuthentication() {
        return selectRadioButtonById(MUTUAL, "choose-mutual-yes");
    }

    public EnableSslWizard disableMutualAuthentication() {
        return selectRadioButtonById(MUTUAL, "choose-mutual-no");
    }

    public EnableSslWizard createAllResources() {
        return selectRadioButtonById(KEY_STORE_STRATEGY, "strategy-create-all");
    }

    public EnableSslWizard createKeyStore() {
        return selectRadioButtonById(KEY_STORE_STRATEGY, "strategy-create-key-store");
    }

    public EnableSslWizard reuseKeyStore() {
        return selectRadioButtonById(KEY_STORE_STRATEGY, "strategy-reuse-key-store");
    }

    public EnableSslWizard tryNextToConfigurationWithExpectError(String errorMessage) {
        next();
        By alertSelector = By.className(CSS.alertDanger);
        waitGui().until(errorMessage).element(root, alertSelector).is().visible();
        return this;
    }

    public EnableSslWizard nextConfiguration() {
        next(CONFIGURATION_FORM_ID);
        return this;
    }

    public EnableSslWizard nextReview() {
        next(REVIEW_FORM_ID);
        return this;
    }

    public FormFragment getConfigurationForm() {
        return getForm(CONFIGURATION_FORM_ID);
    }

    public FormFragment getReviewForm() {
        return getForm(REVIEW_FORM_ID);
    }

    private EnableSslWizard selectRadioButtonById(String name, String id) {
        new RadioButtonGroup(name, root).pickById(id);
        return this;
    }
}
