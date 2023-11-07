package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.fragment.ssl.EnableSslWizard;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Place(Ids.UNDERTOW_SERVER)
public class UndertowServerPage extends BasePage {

    @FindBy(id = Ids.UNDERTOW_SERVER_CONFIGURATION_FORM)
    private FormFragment configurationForm;

    @FindBy(id = "undertow-host-tab-container")
    private TabsFragment hostsTab;

    @FindBy(id = Ids.UNDERTOW_HOST_TABLE + "_wrapper")
    private TableFragment hostsTable;

    @FindBy(id = Ids.UNDERTOW_HOST_ATTRIBUTES_FORM)
    private FormFragment hostsForm;

    @FindBy(id = Ids.UNDERTOW_HOST_ACCESS_LOG + "-form")
    private FormFragment hostsAccessLogForm;

    @FindBy(id = Ids.UNDERTOW_HOST_HTTP_INVOKER + "-form")
    private FormFragment hostsHttpInvokerForm;

    @FindBy(id = Ids.UNDERTOW_SERVER_AJP_LISTENER + "-form")
    private FormFragment ajpListenerForm;

    @FindBy(id = Ids.UNDERTOW_SERVER_AJP_LISTENER + "-table_wrapper")
    private TableFragment ajpListenerTable;

    @FindBy(id = Ids.UNDERTOW_SERVER_HTTP_LISTENER + "-form")
    private FormFragment httpListenerForm;

    @FindBy(id = Ids.UNDERTOW_SERVER_HTTP_LISTENER + "-table_wrapper")
    private TableFragment httpListenerTable;

    @FindBy(id = Ids.UNDERTOW_SERVER_HTTPS_LISTENER + "-form")
    private FormFragment httpsListenerForm;

    @FindBy(id = Ids.UNDERTOW_SERVER_HTTPS_LISTENER + "-table_wrapper")
    private TableFragment httpsListenerTable;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getHostsTable() {
        return hostsTable;
    }

    public FormFragment getHostsForm() {
        hostsTab.select("undertow-host-tab");
        return hostsForm;
    }

    public FormFragment getHostsAccessLogForm() {
        hostsTab.select(Ids.build(Ids.UNDERTOW_HOST_ACCESS_LOG,"tab"));
        return hostsAccessLogForm;
    }

    public FormFragment getHostsHttpInvokerForm() {
        hostsTab.select(Ids.build(Ids.UNDERTOW_HOST_HTTP_INVOKER, "tab"));
        return hostsHttpInvokerForm;
    }

    public FormFragment getAjpListenerForm() {
        return ajpListenerForm;
    }

    public TableFragment getAjpListenerTable() {
        return ajpListenerTable;
    }

    public FormFragment getHttpListenerForm() {
        return httpListenerForm;
    }

    public TableFragment getHttpListenerTable() {
        return httpListenerTable;
    }

    public FormFragment getHttpsListenerForm() {
        return httpsListenerForm;
    }

    public TableFragment getHttpsListenerTable() {
        return httpsListenerTable;
    }

    public EnableSslWizard enableSslWizard() {
        WebElement enableSslButton;
        try {
            enableSslButton = httpsListenerTable.button("Enable SSL");
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Enable SSL button is not present. Is a HTTPS listener selected?", e);
        }
        enableSslButton.click();
        return console.wizard(EnableSslWizard.class);
    }

    public AddResourceDialogFragment disableSslDialog() {
        WebElement disableSslButton;
        try {
            disableSslButton = httpsListenerTable.button("Disable SSL");
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Disable SSL button is not present. Is a HTTPS listener selected?", e);
        }
        disableSslButton.click();
        return console.addResourceDialog();
    }
}
