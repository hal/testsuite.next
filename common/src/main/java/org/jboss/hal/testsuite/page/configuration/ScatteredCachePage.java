package org.jboss.hal.testsuite.page.configuration;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.CSS.bootstrapSelect;
import static org.jboss.hal.resources.CSS.btn;
import static org.jboss.hal.resources.CSS.btnDefault;
import static org.jboss.hal.testsuite.Selectors.contains;

@Place("scattered-cache")
public class ScatteredCachePage extends BasePage {

    public static final String FILE_STORE_WRITE_BEHAVIOUR_TAB = "scattered-cache-cache-store-file-write-tab";
    private static final String FILE_STORE_ATTRIBUTES_TAB = "scattered-cache-cache-store-file-attributes-tab";
    public static final String CUSTOM_STORE_WRITE_BEHAVIOUR_TAB = "scattered-cache-cache-store-custom-write-tab";
    private static final String CUSTOM_STORE_ATTRIBUTES_TAB = "scattered-cache-cache-store-custom-attributes-tab";

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

    @FindBy(css = "label[for='scattered-cache-store-select'] + div." + bootstrapSelect)
    private SelectFragment switchStoreDropdown;

    @FindBy(id = "scattered-cache-cache-store-file-tab-container")
    private TabsFragment fileStoreTab;

    @FindBy(id = "scattered-cache-cache-store-file-form")
    private FormFragment fileStoreAttributesForm;

    @FindBy(id = "scattered-cache-cache-store-file-behind-form")
    private FormFragment fileStoreWriteBehindForm;

    @FindBy(id = "scattered-cache-cache-store-custom-tab-container")
    private TabsFragment customStoreTab;

    @FindBy(id = "scattered-cache-cache-store-custom-form")
    private FormFragment customStoreAttributesForm;

    @FindBy(id = "scattered-cache-cache-store-custom-behind-form")
    private FormFragment customStoreWriteBehindForm;

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

    public FormFragment getFileStoreAttributesForm() {
        switchStoreDropdown.select("File");
        fileStoreTab.select(FILE_STORE_ATTRIBUTES_TAB);
        return fileStoreAttributesForm;
    }

    public TabsFragment getFileStoreTab() {
        return fileStoreTab;
    }

    public FormFragment getFileStoreWriteBehindForm() {
        switchStoreDropdown.select("File");
        fileStoreTab.select(FILE_STORE_WRITE_BEHAVIOUR_TAB);
        return fileStoreWriteBehindForm;
    }

    public TabsFragment getCustomStoreTab() {
        return customStoreTab;
    }

    public FormFragment getCustomStoreAttributesForm() {
        switchStoreDropdown.select("Custom");
        customStoreTab.select(CUSTOM_STORE_ATTRIBUTES_TAB);
        return customStoreAttributesForm;
    }

    public FormFragment getCustomStoreWriteBehindForm() {
        switchStoreDropdown.select("Custom");
        customStoreTab.select(CUSTOM_STORE_WRITE_BEHAVIOUR_TAB);
        return customStoreWriteBehindForm;
    }

    public void switchBehaviour() {
        WebElement button = browser.findElement(
            ByJQuery.selector("button." + btn + "." + btnDefault + contains("Switch Behaviour") + ":visible"));
        Graphene.waitGui().until().element(button).is().visible();
        button.click();
    }
}
