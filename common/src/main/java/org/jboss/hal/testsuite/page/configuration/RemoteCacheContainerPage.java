package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place("remote-cache-container")
public class RemoteCacheContainerPage extends BasePage {

    @FindBy(id = "rcc-tabs")
    private TabsFragment configurationTab;


    @FindBy(id = "rcc-configuration-form")
    private FormFragment configurationForm;

    @FindBy(id = "near-cache-invalidation-form")
    private FormFragment nearCacheForm;

    @FindBy(id = "rc-table_wrapper")
    private TableFragment remoteClusterTable;

    @FindBy(id = "rc-form")
    private FormFragment remoteClusterForm;

    @FindBy(id = "connection-pool-form")
    private FormFragment connectionPoolForm;

    @FindBy(id = "thread-pool-form")
    private FormFragment threadPoolForm;

    @FindBy(id = "security-form")
    private FormFragment securityForm;

    public TabsFragment getConfigurationTab() {
        return configurationTab;
    }

    public FormFragment getConfigurationForm() {
        configurationTab.select("rcc-configuration-tab");
        return configurationForm;
    }

    public FormFragment getNearCacheForm() {
        configurationTab.select("rcc-near-cache-tab");
        return nearCacheForm;
    }

    public TableFragment getRemoteClusterTable() {
        return remoteClusterTable;
    }

    public FormFragment getRemoteClusterForm() {
        return remoteClusterForm;
    }

    public FormFragment getConnectionPoolForm() {
        return connectionPoolForm;
    }

    public FormFragment getThreadPoolForm() {
        return threadPoolForm;
    }

    public FormFragment getSecurityForm() {
        return securityForm;
    }
}
