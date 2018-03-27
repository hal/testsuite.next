package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(Ids.UNDERTOW_APP_SECURITY_DOMAIN)
public class UndertowApplicationSecurityDomainPage extends BasePage {

    @FindBy(id = Ids.UNDERTOW_APP_SECURITY_DOMAIN_TAB)
    private TabsFragment tab;

    @FindBy(id = Ids.UNDERTOW_APP_SECURITY_DOMAIN_FORM)
    private FormFragment attributesForm;

    @FindBy(id = "undertow-single-sign-on-table")
    private TableFragment singleSignOnTable;

    @FindBy(id = Ids.UNDERTOW_APP_SECURITY_DOMAIN + "-credential-reference-tab")
    private FormFragment credentialReferenceTab;

    public TabsFragment getTab() {
        return tab;
    }

    public FormFragment getAttributesForm() {
        getTab().select(Ids.UNDERTOW_APP_SECURITY_DOMAIN_TAB);
        return attributesForm;
    }

    public TableFragment getSingleSignOnTable() {
        getTab().select("undertow-single-sign-on-table");
        return singleSignOnTable;
    }

    public FormFragment getCredentialReferenceTab() {
        return credentialReferenceTab;
    }
}
