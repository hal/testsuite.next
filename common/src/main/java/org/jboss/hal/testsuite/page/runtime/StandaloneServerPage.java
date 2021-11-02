package org.jboss.hal.testsuite.page.runtime;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.ssl.EnableSslWizard;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.RELOAD;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.STANDALONE_SERVER)
public class StandaloneServerPage extends BasePage {

    @FindBy(id = Ids.ENABLE_SSL) private WebElement enableSslButton;
    @FindBy(id = Ids.DISABLE_SSL) private WebElement disableSslButton;
    @FindBy(id = "constant-headers-table" + WRAPPER) private TableFragment constantHeadersTable;
    @FindBy(id = "constant-headers-form") private FormFragment constantHeadersForm;
    @FindBy(id = "constant-headers-header-table" + WRAPPER) private TableFragment headersTable;
    @FindBy(id = "constant-headers-header-form") private FormFragment headersForm;

    public StandaloneServerPage navigateToHttpManagementPage() {
        navigate();
        console.verticalNavigation().selectPrimary(Ids.HTTP_INTERFACE_ITEM);
        return this;
    }

    public EnableSslWizard enableSslWizard() {
        navigateToHttpManagementPage();
        enableSslButton.click();
        return console.wizard(EnableSslWizard.class);
    }

    public void disableSslWithReload() {
        navigateToHttpManagementPage();
        disableSslButton.click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().flip(RELOAD, true);
        dialog.getPrimaryButton().click();
    }

    public TableFragment getConstantHeadersTable() {
        return constantHeadersTable;
    }

    public FormFragment getConstantHeadersForm() {
        return constantHeadersForm;
    }

    public TableFragment getHeadersTable() {
        return headersTable;
    }

    public FormFragment getHeadersForm() {
        return headersForm;
    }
}
