package org.jboss.hal.testsuite.test.configuration.transaction;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.resources.Ids.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
* @author vobratil@redhat.com
 *
 * This is a test for customer case JBEAP-23617.
*/
@RunWith(Arquillian.class)
public class StatisticsDisplayedTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    @Inject private Console console;

    @Drone
    private static WebDriver browser;

    @Before
    public void undefineAttribute() throws Exception {

        operations.undefineAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATISTICS_ENABLED);
    }

    /**
     * The tests check whether the Statistics Disabled div is truly displayed only when statistics are disabled. The div
     * should not appear to the user when statistics are enabled, but it's still present in the HTML code.
     */
    @Test
    public void transactionStatisticsEnabled() throws Exception {

        operations.writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATISTICS_ENABLED, true);

        FinderFragment fragment = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(STANDALONE_SERVER_COLUMN, "standalone-host-" + serverEnvironmentUtils.getServerHostName())
                .append(RUNTIME_SUBSYSTEM, "transactions"));

        WebElement div = browser.findElement(By.id(TRANSACTION_STATISTICS_DISABLED));

        assertFalse(div.isDisplayed());
    }

    @Test
    public void transactionStatisticsDisabled() throws Exception {

        operations.writeAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATISTICS_ENABLED, false);

        FinderFragment fragment = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(STANDALONE_SERVER_COLUMN, "standalone-host-" + serverEnvironmentUtils.getServerHostName())
                .append(RUNTIME_SUBSYSTEM, "transactions"));

        WebElement div = browser.findElement(By.id(TRANSACTION_STATISTICS_DISABLED));

        assertTrue(div.isDisplayed());
    }

    @AfterClass
    public static void tearDown() throws Exception {

        operations.undefineAttribute(TransactionFixtures.TRANSACTIONS_ADDRESS, TransactionFixtures.STATISTICS_ENABLED);
    }
}
