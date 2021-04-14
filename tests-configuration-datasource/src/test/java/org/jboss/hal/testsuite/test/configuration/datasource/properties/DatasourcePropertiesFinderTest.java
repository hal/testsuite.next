package org.jboss.hal.testsuite.test.configuration.datasource.properties;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.xaDataSourceAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;

@RunWith(Arquillian.class)
public class DatasourcePropertiesFinderTest extends AbstractDatasourcePropertiesTest {

    private static final String PG_XA_DATASOURCE_NAME = "postgresql-data-source-create-" + Random.name();
    private static final String MYSQL_XA_DATASOURCE_NAME = "mysql-data-source-create-" + Random.name();
    private static final String MSSQL_XA_DATASOURCE_NAME = "sqlserver-data-source-create-" + Random.name();
    private static final String CUSTOM_XA_DATASOURCE_NAME = "custom-data-source-create-" + Random.name();

    private static final String PG_SELECT_CSS_SELECTOR = "input[type=radio][name=template][value=postgresql-xa]";
    private static final String MYSQL_SELECT_CSS_SELECTOR = "input[type=radio][name=template][value=mysql-xa]";
    private static final String MSSQL_SELECT_CSS_SELECTOR = "input[type=radio][name=template][value=sqlserver-xa]";
    private static final String CUSTOM_SELECT_CSS_SELECTOR = "input[type=radio][name=template][value=custom]";

    static final Consumer<FormFragment> DO_NOT_FILL = (formFragment -> {
    });

    private enum DataSourceWizardConsumer {

        PG_DATASOURCE_WIZARD_CONSUMER(PG_DRIVER_NAME, PG_MODULE_NAME, PG_DRIVER_CLASS,
            PG_XA_DATASOURCE_CLASS, PG_SELECT_CSS_SELECTOR, DO_NOT_FILL),
        MYSQL_DATASOURCE_WIZARD_CONSUMER(MYSQL_DRIVER_NAME, MYSQL_MODULE_NAME, MYSQL_DRIVER_CLASS,
            MYSQL_XA_DATASOURCE_CLASS, MYSQL_SELECT_CSS_SELECTOR, DO_NOT_FILL),
        MSSQL_DATASOURCE_WIZARD_CONSUMER(MSSQL_DRIVER_NAME, MSSQL_MODULE_NAME, MSSQL_DRIVER_CLASS,
            MSSQL_XA_DATASOURCE_CLASS, MSSQL_SELECT_CSS_SELECTOR, DO_NOT_FILL),
        CUSTOM_DATASOURCE_WIZARD_CONSUMER(CUSTOM_DRIVER_NAME, CUSTOM_MODULE_NAME, CUSTOM_DRIVER_CLASS,
            CUSTOM_XA_DATASOURCE_CLASS, CUSTOM_SELECT_CSS_SELECTOR, formFragment -> {
            formFragment.properties("value")
                .add("booleanProperty", String.valueOf(true));
        });

        private final String driverName;
        private final String driverModuleName;
        private final Class<?> driverClass;
        private final Class<?> xaDatasourceClass;
        private final String wizardSelectSelector;
        private final Consumer<FormFragment> selectBoxHandler;

        DataSourceWizardConsumer(String driverName, String driverModuleName, Class<?> driverClass,
            Class<?> xaDatasourceClass, String wizardSelectSelector, Consumer<FormFragment> selectBoxHandler) {
            this.driverName = driverName;
            this.driverModuleName = driverModuleName;
            this.driverClass = driverClass;
            this.xaDatasourceClass = xaDatasourceClass;
            this.wizardSelectSelector = wizardSelectSelector;
            this.selectBoxHandler = selectBoxHandler;
        }

        public void fillInWizardAndVerifySuggestions(String datasourceName, WizardFragment wizard, WebDriver browser) {
            wizard.getRoot().findElement(By.cssSelector(wizardSelectSelector)).click();
            wizard.next();
            FormFragment datasourceAttributesForm = wizard.getForm(Ids.DATA_SOURCE_NAMES_FORM);
            datasourceAttributesForm.text("name", datasourceName);
            datasourceAttributesForm.text("jndi-name", Random.jndiName(datasourceName));
            wizard.next();
            FormFragment datasourceJDBCDriverForm = wizard.getForm(Ids.DATA_SOURCE_DRIVER_FORM);
            datasourceJDBCDriverForm.text("driver-name", driverName);
            datasourceJDBCDriverForm.text("driver-module-name", driverModuleName);
            datasourceJDBCDriverForm.text("driver-class-name", driverClass.getName());
            wizard.next();
            selectBoxHandler.accept(wizard.getForm(Ids.DATA_SOURCE_PROPERTIES_FORM));
            WebElement showAllButton = wizard.getForm(Ids.DATA_SOURCE_PROPERTIES_FORM)
                .getRoot()
                .findElement(By.cssSelector("button[title=\"Show all\"]"));
            Graphene.waitGui().until().element(showAllButton)
                .is().visible();
            showAllButton.click();
            Graphene.waitGui().until().element(ByJQuery.selector(".autocomplete-suggestion:visible")).is().visible();
            List<String> actualSuggestions = browser
                .findElements(ByJQuery.selector(".autocomplete-suggestion:visible"))
                .stream()
                .map(element -> element.getAttribute("data-val"))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            Assert.assertTrue("All properties provided by the "
                    + xaDatasourceClass.getSimpleName()
                    + " class should be suggested in the wizard",
                containsAllIgnoreCase(getDataSourcePropertiesFromClass(xaDatasourceClass), actualSuggestions));
            wizard.next();
            wizard.next();
            wizard.next();
            wizard.finishStayOpen();
            wizard.verifySuccess();
            wizard.close();
        }

    }

    @AfterClass
    public static void cleanUpDataSources() throws InterruptedException, TimeoutException, IOException,
        OperationException {
        operations.removeIfExists(xaDataSourceAddress(PG_XA_DATASOURCE_NAME));
        operations.removeIfExists(xaDataSourceAddress(MYSQL_XA_DATASOURCE_NAME));
        operations.removeIfExists(xaDataSourceAddress(MSSQL_XA_DATASOURCE_NAME));
        operations.removeIfExists(xaDataSourceAddress(CUSTOM_XA_DATASOURCE_NAME));
        administration.reloadIfRequired();
    }

    @Drone
    private WebDriver broswer;

    @Inject
    private Console console;

    @Before
    public void navigateToCreateXADatasourceWizard() {
        console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(DATASOURCES)
            .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES))).column(Ids.DATA_SOURCE_CONFIGURATION)
            .dropdownAction(Ids.DATA_SOURCE_ADD_ACTIONS, Ids.XA_DATA_SOURCE_ADD);
    }

    @Test
    public void createPGXADatasourceShowsSuggestions() throws Exception {
        DataSourceWizardConsumer.PG_DATASOURCE_WIZARD_CONSUMER.fillInWizardAndVerifySuggestions(PG_XA_DATASOURCE_NAME,
            console.wizard(), broswer);
        new ResourceVerifier(xaDataSourceAddress(PG_XA_DATASOURCE_NAME), client).verifyExists();
    }

    @Test
    public void createMySQLXADatasourceShowsSuggestions() throws Exception {
        DataSourceWizardConsumer.MYSQL_DATASOURCE_WIZARD_CONSUMER.fillInWizardAndVerifySuggestions(
            MYSQL_XA_DATASOURCE_NAME,
            console.wizard(), broswer);
        new ResourceVerifier(xaDataSourceAddress(MYSQL_XA_DATASOURCE_NAME), client).verifyExists();
    }

    @Test
    public void createMSSQLXADatasourceShowsSuggestions() throws Exception {
        DataSourceWizardConsumer.MSSQL_DATASOURCE_WIZARD_CONSUMER.fillInWizardAndVerifySuggestions(
            MSSQL_XA_DATASOURCE_NAME,
            console.wizard(), broswer);
        new ResourceVerifier(xaDataSourceAddress(MSSQL_XA_DATASOURCE_NAME), client).verifyExists();
    }

    @Test
    public void createCustomXADatasourceShowsSuggestions() throws Exception {
        DataSourceWizardConsumer.CUSTOM_DATASOURCE_WIZARD_CONSUMER.fillInWizardAndVerifySuggestions(
            CUSTOM_XA_DATASOURCE_NAME,
            console.wizard(), broswer);
        new ResourceVerifier(xaDataSourceAddress(CUSTOM_XA_DATASOURCE_NAME), client).verifyExists();
    }
}
