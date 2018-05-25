package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.LOGGING_CONFIGURATION)
public class LoggingSubsystemConfigurationPage extends BasePage implements LoggingConfigurationPage {

    @FindBy(id = "logging-config-form") private FormFragment configurationForm;
    @FindBy(id = "logging-root-logger-form") private FormFragment rootLoggerForm;
    @FindBy(id = "logging-category-table" + WRAPPER) private TableFragment categoryTable;
    @FindBy(id = "logging-category-form") private FormFragment categoryForm;
    @FindBy(id = "logging-handler-async-table" + WRAPPER) private TableFragment asyncHandlerTable;
    @FindBy(id = "logging-handler-async-form") private FormFragment asyncHandlerForm;
    @FindBy(id = "logging-handler-console-table" + WRAPPER) private TableFragment consoleHandlerTable;
    @FindBy(id = "logging-handler-console-form") private FormFragment consoleHandlerForm;
    @FindBy(id = "logging-handler-file-table" + WRAPPER) private TableFragment fileHandlerTable;
    @FindBy(id = "logging-handler-file-table-add-file-editing") private WebElement newFileInputElement;
    @FindBy(id = "logging-handler-file-form") private FormFragment fileHandlerForm;
    @FindBy(id = "logging-handler-file-form-file-readonly") private WebElement readFileInputElement;
    @FindBy(id = "logging-handler-periodic-rotating-file-table" + WRAPPER) private TableFragment periodicHandlerTable;
    @FindBy(id = "logging-handler-periodic-rotating-file-table-add-file-editing") private WebElement newPeriodicFileInputElement;
    @FindBy(id = "logging-handler-periodic-rotating-file-form") private FormFragment periodicHandlerForm;
    @FindBy(id = "logging-handler-periodic-rotating-file-form-file-readonly") private WebElement readPeriodicFileInputElement;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-table" + WRAPPER) private TableFragment periodicSizeHandlerTable;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-table-add-file-editing") private WebElement newPeriodicSizeFileInputElement;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-form") private FormFragment periodicSizeHandlerForm;
    @FindBy(id = "logging-handler-periodic-size-rotating-file-form-file-readonly") private WebElement readPeriodicSizeFileInputElement;
    @FindBy(id = "logging-handler-size-rotating-file-table" + WRAPPER) private TableFragment sizeHandlerTable;
    @FindBy(id = "logging-handler-size-rotating-file-table-add-file-editing") private WebElement newSizeFileInputElement;
    @FindBy(id = "logging-handler-size-rotating-file-form") private FormFragment sizeHandlerForm;
    @FindBy(id = "logging-handler-size-rotating-file-form-file-readonly") private WebElement readSizeFileInputElement;
    @FindBy(id = "logging-handler-custom-table" + WRAPPER) private TableFragment customHandlerTable;
    @FindBy(id = "logging-handler-custom-form") private FormFragment customHandlerForm;
    @FindBy(id = "logging-handler-syslog-table" + WRAPPER) private TableFragment syslogHandlerTable;
    @FindBy(id = "logging-handler-syslog-form") private FormFragment syslogHandlerForm;
    @FindBy(id = "logging-formatter-pattern-table" + WRAPPER) private TableFragment patternFormatterTable;
    @FindBy(id = "logging-formatter-pattern-form") private FormFragment patternFormatterForm;
    @FindBy(id = "logging-json-formatter-table" + WRAPPER) private TableFragment jsonFormatterTable;
    @FindBy(id = "logging-json-formatter-form") private FormFragment jsonFormatterForm;
    @FindBy(id = "logging-xml-formatter-table" + WRAPPER) private TableFragment xmlFormatterTable;
    @FindBy(id = "logging-xml-formatter-form") private FormFragment xmlFormatterForm;

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public FormFragment getRootLoggerForm() {
        return rootLoggerForm;
    }

    public TableFragment getCategoryTable() {
        return categoryTable;
    }

    public FormFragment getCategoryForm() {
        return categoryForm;
    }

    public TableFragment getConsoleHandlerTable() {
        return consoleHandlerTable;
    }

    public FormFragment getConsoleHandlerForm() {
        return consoleHandlerForm;
    }

    public TableFragment getFileHandlerTable() {
        return fileHandlerTable;
    }

    public WebElement getNewFileInputElement() {
        return newFileInputElement;
    }

    public FormFragment getFileHandlerForm() {
        return fileHandlerForm;
    }

    public WebElement getReadFileInputElement() {
        return readFileInputElement;
    }

    public TableFragment getPeriodicHandlerTable() {
        return periodicHandlerTable;
    }

    public WebElement getNewPeriodicFileInputElement() {
        return newPeriodicFileInputElement;
    }

    public FormFragment getPeriodicHandlerForm() {
        return periodicHandlerForm;
    }

    public WebElement getReadPeriodicFileInputElement() {
        return readPeriodicFileInputElement;
    }

    public TableFragment getPeriodicSizeHandlerTable() {
        return periodicSizeHandlerTable;
    }

    public WebElement getNewPeriodicSizeFileInputElement() {
        return newPeriodicSizeFileInputElement;
    }

    public FormFragment getPeriodicSizeHandlerForm() {
        return periodicSizeHandlerForm;
    }

    public WebElement getReadPeriodicSizeFileInputElement() {
        return readPeriodicSizeFileInputElement;
    }

    public TableFragment getSizeHandlerTable() {
        return sizeHandlerTable;
    }

    public WebElement getNewSizeFileInputElement() {
        return newSizeFileInputElement;
    }

    public FormFragment getSizeHandlerForm() {
        return sizeHandlerForm;
    }

    public WebElement getReadSizeFileInputElement() {
        return readSizeFileInputElement;
    }

    @Override
    public TableFragment getAsyncHandlerTable() {
        return asyncHandlerTable;
    }

    @Override
    public FormFragment getAsyncHandlerForm() {
        return asyncHandlerForm;
    }

    public TableFragment getCustomHandlerTable() {
        return customHandlerTable;
    }

    public FormFragment getCustomHandlerForm() {
        return customHandlerForm;
    }

    public TableFragment getSyslogHandlerTable() {
        return syslogHandlerTable;
    }

    public FormFragment getSyslogHandlerForm() {
        return syslogHandlerForm;
    }

    @Override
    public TableFragment getPatternFormatterTable() {
        return patternFormatterTable;
    }

    @Override
    public FormFragment getPatternFormatterForm() {
        return patternFormatterForm;
    }

    public TableFragment getJsonFormatterTable() {
        return jsonFormatterTable;
    }

    public FormFragment getJsonFormatterForm() {
        return jsonFormatterForm;
    }

    public TableFragment getXmlFormatterTable() {
        return xmlFormatterTable;
    }

    public FormFragment getXmlFormatterForm() {
        return xmlFormatterForm;
    }

}
