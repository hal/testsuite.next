package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.LOGGING_PROFILE)
public class LoggingProfileConfigurationPage extends BasePage implements LoggingConfigurationPage {

    @FindBy(id = "logging-profile-root-logger-form") private FormFragment rootLoggerForm;
    @FindBy(id = "logging-profile-category-table" + WRAPPER) private TableFragment categoryTable;
    @FindBy(id = "logging-profile-category-form") private FormFragment categoryForm;
    @FindBy(id = "logging-profile-handler-async-table" + WRAPPER) private TableFragment asyncHandlerTable;
    @FindBy(id = "logging-profile-handler-async-form") private FormFragment asyncHandlerForm;
    @FindBy(id = "logging-profile-handler-console-table" + WRAPPER) private TableFragment consoleHandlerTable;
    @FindBy(id = "logging-profile-handler-console-form") private FormFragment consoleHandlerForm;
    @FindBy(id = "logging-profile-handler-file-table" + WRAPPER) private TableFragment fileHandlerTable;
    @FindBy(id = "logging-profile-handler-file-table-add-file-editing") private WebElement newFileInputElement;
    @FindBy(id = "logging-profile-handler-file-form") private FormFragment fileHandlerForm;
    @FindBy(id = "logging-profile-handler-file-form-file-readonly") private WebElement readFileInputElement;
    @FindBy(id = "logging-profile-handler-periodic-rotating-file-table" + WRAPPER) private TableFragment periodicHandlerTable;
    @FindBy(id = "logging-profile-handler-periodic-rotating-file-table-add-file-editing") private WebElement newPeriodicFileInputElement;
    @FindBy(id = "logging-profile-handler-periodic-rotating-file-form") private FormFragment periodicHandlerForm;
    @FindBy(id = "logging-profile-handler-periodic-rotating-file-form-file-readonly") private WebElement readPeriodicFileInputElement;
    @FindBy(id = "logging-profile-handler-periodic-size-rotating-file-table" + WRAPPER) private TableFragment periodicSizeHandlerTable;
    @FindBy(id = "logging-profile-handler-periodic-size-rotating-file-table-add-file-editing") private WebElement newPeriodicSizeFileInputElement;
    @FindBy(id = "logging-profile-handler-periodic-size-rotating-file-form") private FormFragment periodicSizeHandlerForm;
    @FindBy(id = "logging-profile-handler-periodic-size-rotating-file-form-file-readonly") private WebElement readPeriodicSizeFileInputElement;
    @FindBy(id = "logging-profile-handler-size-rotating-file-table" + WRAPPER) private TableFragment sizeHandlerTable;
    @FindBy(id = "logging-profile-handler-size-rotating-file-table-add-file-editing") private WebElement newSizeFileInputElement;
    @FindBy(id = "logging-profile-handler-size-rotating-file-form") private FormFragment sizeHandlerForm;
    @FindBy(id = "logging-profile-handler-size-rotating-file-form-file-readonly") private WebElement readSizeFileInputElement;
    @FindBy(id = "logging-profile-handler-custom-table" + WRAPPER) private TableFragment customHandlerTable;
    @FindBy(id = "logging-profile-handler-custom-form") private FormFragment customHandlerForm;
    @FindBy(id = "logging-profile-handler-socket-table" + WRAPPER) private TableFragment socketHandlerTable;
    @FindBy(id = "logging-profile-handler-socket-form") private FormFragment socketHandlerForm;
    @FindBy(id = "logging-profile-handler-syslog-table" + WRAPPER) private TableFragment syslogHandlerTable;
    @FindBy(id = "logging-profile-handler-syslog-form") private FormFragment syslogHandlerForm;
    @FindBy(id = "logging-profile-formatter-pattern-table" + WRAPPER) private TableFragment patternFormatterTable;
    @FindBy(id = "logging-profile-formatter-pattern-form") private FormFragment patternFormatterForm;
    @FindBy(id = "logging-profile-formatter-custom-table" + WRAPPER) private TableFragment customFormatterTable;
    @FindBy(id = "logging-profile-formatter-custom-form") private FormFragment customFormatterForm;
    @FindBy(id = "logging-profile-json-formatter-table" + WRAPPER) private TableFragment jsonFormatterTable;
    @FindBy(id = "logging-profile-json-formatter-form") private FormFragment jsonFormatterForm;
    @FindBy(id = "logging-profile-xml-formatter-table" + WRAPPER) private TableFragment xmlFormatterTable;
    @FindBy(id = "logging-profile-xml-formatter-form") private FormFragment xmlFormatterForm;

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

    @Override
    public TableFragment getSocketHandlerTable() {
        return socketHandlerTable;
    }

    @Override
    public FormFragment getSocketHandlerForm() {
        return socketHandlerForm;
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

    public TableFragment getCustomFormatterTable() {
        return customFormatterTable;
    }

    public FormFragment getCustomFormatterForm() {
        return customFormatterForm;
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
