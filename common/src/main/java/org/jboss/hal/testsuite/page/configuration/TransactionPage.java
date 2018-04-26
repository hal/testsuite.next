package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.TRANSACTIONS)
public class TransactionPage extends BasePage {

    @FindBy(id = "tx-attributes-form")
    private FormFragment configurationForm;

    @FindBy(id = "tx-process-form")
    private FormFragment processForm;

    @FindBy(id = "tx-recovery-form")
    private FormFragment recoveryForm;

    @FindBy(id = "tx-path-form")
    private FormFragment pathForm;

    @FindBy(id = "tx-jdbc-form")
    private FormFragment jdbcForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public FormFragment getProcessForm() {
        return processForm;
    }

    public FormFragment getRecoveryForm() {
        return recoveryForm;
    }

    public FormFragment getPathForm() {
        return pathForm;
    }

    public FormFragment getJdbcForm() {
        return jdbcForm;
    }
}
