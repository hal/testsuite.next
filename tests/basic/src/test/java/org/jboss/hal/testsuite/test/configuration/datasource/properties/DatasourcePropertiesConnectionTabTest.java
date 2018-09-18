package org.jboss.hal.testsuite.test.configuration.datasource.properties;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.commands.datasources.AddXADataSource;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class DatasourcePropertiesConnectionTabTest extends AbstractDatasourcePropertiesTest {

    private static final String PG_XA_DATASOURCE_NAME = "postgresql-xa-data-source-" + Random.name();
    private static final String PG_DATASOURCE_NAME = "postgresql-data-source-" + Random.name();
    private static final String MYSQL_XA_DATASOURCE_NAME = "mysql-xa-data-source-" + Random.name();
    private static final String MYSQL_DATASOURCE_NAME = "mysql-data-source-" + Random.name();
    private static final String MSSQL_XA_DATASOURCE_NAME = "sqlserver-xa-data-source-" + Random.name();
    private static final String MSSQL_DATASOURCE_NAME = "sqlserver-data-source-" + Random.name();
    private static final String CUSTOM_DATASOURCE_NAME = "custom-data-source-" + Random.name();
    private static final String CUSTOM_XA_DATASOURCE_NAME = "custom-data-xa-source-" + Random.name();

    @BeforeClass
    public static void createDataSources()
        throws CommandFailedException, InterruptedException, TimeoutException, IOException {
        client.apply(new AddXADataSource.Builder<>(PG_XA_DATASOURCE_NAME)
            .driverName(PG_DRIVER_NAME)
            .jndiName(Random.jndiName(PG_XA_DATASOURCE_NAME))
            .addXaDatasourceProperty("ServerName", Random.name())
            .addXaDatasourceProperty("PortNumber",
                String.valueOf(AvailablePortFinder.getNextAvailableNonPrivilegedPort()))
            .addXaDatasourceProperty("DatabaseName", Random.name())
            .build());
        client.apply(new AddDataSource.Builder<>(PG_DATASOURCE_NAME)
            .driverName(PG_DRIVER_NAME)
            .jndiName(Random.jndiName(PG_DATASOURCE_NAME))
            .connectionUrl(Random.jndiName())
            .build());
        client.apply(new AddXADataSource.Builder<>(MYSQL_XA_DATASOURCE_NAME)
            .driverName(MYSQL_DRIVER_NAME)
            .jndiName(Random.jndiName(MYSQL_XA_DATASOURCE_NAME))
            .addXaDatasourceProperty("ServerName", Random.name())
            .addXaDatasourceProperty("DatabaseName", Random.name())
            .build());
        client.apply(new AddDataSource.Builder<>(MYSQL_DATASOURCE_NAME)
            .driverName(MYSQL_DRIVER_NAME)
            .jndiName(Random.jndiName(MYSQL_DATASOURCE_NAME))
            .connectionUrl(Random.jndiName())
            .build());
        client.apply(new AddXADataSource.Builder<>(MSSQL_XA_DATASOURCE_NAME)
            .driverName(MSSQL_DRIVER_NAME)
            .jndiName(Random.jndiName(MSSQL_XA_DATASOURCE_NAME))
            .addXaDatasourceProperty("SelectMethod", "cursor")
            .addXaDatasourceProperty("ServerName", Random.name())
            .addXaDatasourceProperty("DatabaseName", Random.name())
            .build());
        client.apply(new AddDataSource.Builder<>(MSSQL_DATASOURCE_NAME)
            .driverName(MSSQL_DRIVER_NAME)
            .connectionUrl(Random.jndiName())
            .jndiName(Random.jndiName(MSSQL_DATASOURCE_NAME))
            .build());
        client.apply(new AddXADataSource.Builder<>(CUSTOM_XA_DATASOURCE_NAME)
            .driverName(CUSTOM_DRIVER_NAME)
            .jndiName(Random.jndiName(CUSTOM_XA_DATASOURCE_NAME))
            .addXaDatasourceProperty("booleanProperty", true)
            .build());
        client.apply(new AddDataSource.Builder<>(CUSTOM_DATASOURCE_NAME)
            .driverName(CUSTOM_DRIVER_NAME)
            .connectionUrl(Random.jndiName())
            .jndiName(Random.jndiName(CUSTOM_DATASOURCE_NAME))
            .build());
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void cleanUp()
        throws IOException, OperationException, TimeoutException, InterruptedException {
        try {
            operations.removeIfExists(DataSourceFixtures.xaDataSourceAddress(PG_XA_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.dataSourceAddress(PG_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.xaDataSourceAddress(MYSQL_XA_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.dataSourceAddress(MYSQL_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.xaDataSourceAddress(MSSQL_XA_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.dataSourceAddress(MSSQL_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.xaDataSourceAddress(CUSTOM_XA_DATASOURCE_NAME));
            operations.removeIfExists(DataSourceFixtures.dataSourceAddress(CUSTOM_DATASOURCE_NAME));
            administration.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private DataSourcePage page;

    @Test
    public void editPGXADatasourceShowsSuggestions() {
        navigateToXADataSourceForm(PG_XA_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(PG_XA_DATASOURCE_CLASS, page.getXaConnectionForm());
    }

    @Test
    public void editPGDatasourceShowsSuggestions() {
        navigateToDataSourceForm(PG_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(PG_DATASOURCE_CLASS, page.getConnectionForm());
    }

    @Test
    public void editMySQLXADatasourceShowsSuggestions() {
        navigateToXADataSourceForm(MYSQL_XA_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(MYSQL_XA_DATASOURCE_CLASS, page.getXaConnectionForm());
    }

    @Test
    public void editMySQLDatasourceShowsSuggestions() {
        navigateToDataSourceForm(MYSQL_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(MYSQL_DATASOURCE_CLASS, page.getConnectionForm());
    }

    @Test
    public void editMSSQLXADatasourceShowsSuggestions() {
        navigateToXADataSourceForm(MSSQL_XA_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(MSSQL_XA_DATASOURCE_CLASS, page.getXaConnectionForm());
    }

    @Test
    public void editMSSQLDatasourceShowsSuggestions() {
        navigateToDataSourceForm(MSSQL_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(MSSQL_DATASOURCE_CLASS, page.getConnectionForm());
    }

    @Test
    public void editCustomXADatasourceShowsSuggestions() {
        navigateToXADataSourceForm(CUSTOM_XA_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(CUSTOM_XA_DATASOURCE_CLASS, page.getXaConnectionForm());
    }

    @Test
    public void editCustomDatasourceShowsSuggestions() {
        navigateToDataSourceForm(CUSTOM_DATASOURCE_NAME);
        verifySuggestionsArePresentInTheForm(CUSTOM_DATASOURCE_CLASS, page.getConnectionForm());
    }


    private void navigateToXADataSourceForm(String dataSourceName) {
        Map<String, String> params = new HashMap<>();
        params.put(NAME, dataSourceName);
        params.put("xa", "true");
        page.navigate(params);
        page.navigate(params);
        page.getXaTabs().select(Ids.build(Ids.XA_DATA_SOURCE, "connection", Ids.TAB));
        page.getXaConnectionForm().edit();
    }

    private void navigateToDataSourceForm(String dataSourceName) {
        page.navigate(NAME, dataSourceName);
        page.navigate(NAME, dataSourceName);
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "connection", Ids.TAB));
        page.getConnectionForm().edit();
    }

    private void verifySuggestionsArePresentInTheForm(Class<?> dataSourceClass, FormFragment form) {
        WebElement showAllButton = form.getRoot()
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
                + dataSourceClass.getSimpleName()
                + " class should be suggested in the form",
            containsAllIgnoreCase(getDataSourcePropertiesFromClass(dataSourceClass), actualSuggestions));
    }
}
