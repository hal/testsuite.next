package org.jboss.hal.testsuite.page.runtime;

import java.io.IOException;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;



import static org.jboss.arquillian.graphene.Graphene.waitGui;

@Place(NameTokens.RUNTIME)
public class DataSourceRuntimePage extends BasePage {
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);


    @FindBy(id = "datasources") private WebElement datasources;

    @FindBy(id = Ids.DATA_SOURCE_RUNTIME) private WebElement dataSourceRuntime;

    @Override
    public void navigate()  {
       super.navigate();
        try {
            console.finder(NameTokens.RUNTIME).column(Ids.STANDALONE_SERVER_COLUMN)
                     .selectItem("standalone-host-" + serverEnvironmentUtils.getServerHostName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Wait for load  list of Datasource view  (Runtime > Server > Monitor > Datasources) and return it as WebElement
     * @return
     */
    public WebElement getDatasource() {
        waitGui().until().element(datasources).is().visible();
        return datasources;
    }

    /**
     * Wait for load Datasource view (Runtime > Server > Monitor > Datasources -> Datasource view) and return it as WebElement
     * @return
     */
    public WebElement getDataSourceRuntime() {
        waitGui().until().element(dataSourceRuntime).is().visible();
        return dataSourceRuntime;
    }
}
