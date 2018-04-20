package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.UNDERTOW_HANDLER)
public class UndertowHandlersPage extends BasePage {

    @FindBy(id = "undertow-file-handler-table_wrapper")
    private TableFragment fileHandlerTable;

    @FindBy(id = "undertow-file-handler-form")
    private FormFragment fileHandlerForm;

    @FindBy(id = "undertow-reverse-proxy-table_wrapper")
    private TableFragment reverseProxyTable;

    @FindBy(id = "undertow-reverse-proxy-form")
    private FormFragment reverseProxyForm;

    public TableFragment getFileHandlerTable() {
        return fileHandlerTable;
    }

    public FormFragment getFileHandlerForm() {
        return fileHandlerForm;
    }

    public TableFragment getReverseProxyTable() {
        return reverseProxyTable;
    }

    public FormFragment getReverseProxyForm() {
        return reverseProxyForm;
    }
}
