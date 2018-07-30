package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.CSS.bootstrapSelect;

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

    @FindBy(id = "scattered-cache-cache-component-partition-handling-form")
    private FormFragment partitionHandlingForm;

    @FindBy(id = "scattered-cache-cache-component-state-transfer-form")
    private FormFragment stateTransferForm;

    @FindBy(id = "scattered-cache-cache-component-transaction-form")
    private FormFragment transactionForm;

    @FindBy(css = "label[for='scattered-cache-memory-select'] + div." + bootstrapSelect)
    private SelectFragment switchMemoryDropdown;

    @FindBy(id = "scattered-cache-cache-memory-object-form")
    private FormFragment objectMemoryForm;

    @FindBy(id = "scattered-cache-cache-memory-binary-form")
    private FormFragment binaryMemoryForm;

    @FindBy(id = "scattered-cache-cache-memory-off-heap-form")
    private FormFragment offHeapMemoryForm;

    @FindBy(id = "scattered-cache-backups-table_wrapper")
    private TableFragment backupsTable;

    @FindBy(id = "scattered-cache-backups-form")
    private FormFragment backupsForm;

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

    public FormFragment getPartitionHandlingForm() {
        configurationTab.select("scattered-cache-cache-component-partition-handling-tab");
        return partitionHandlingForm;
    }

    public FormFragment getStateTransferForm() {
        configurationTab.select("scattered-cache-cache-component-state-transfer-tab");
        return stateTransferForm;
    }

    public FormFragment getTransactionForm() {
        configurationTab.select("scattered-cache-cache-component-transaction-tab");
        return transactionForm;
    }

    public SelectFragment getSwitchMemoryDropdown() {
        return switchMemoryDropdown;
    }

    public TableFragment getBackupsTable() {
        return backupsTable;
    }

    public FormFragment getBackupsForm() {
        return backupsForm;
    }

    public FormFragment getObjectMemoryForm() {
        switchMemoryDropdown.select("Object");
        return objectMemoryForm;
    }

    public FormFragment getBinaryMemoryForm() {
        switchMemoryDropdown.select("Binary");
        return binaryMemoryForm;
    }

    public FormFragment getOffHeapMemoryForm() {
        switchMemoryDropdown.select("Off Heap");
        return offHeapMemoryForm;
    }
}
