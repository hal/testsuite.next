package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;



@RunWith(Arquillian.class)
@Category(Domain.class)
public class MissingOperationsTestCase {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String[] baseItems = {"Destroy", "Kill", "Copy"};
    private static final String[] startedItems = {"Reload", "Restart", "Suspend", "Stop"};
    private static final String[] suspendedItems = {"Resume"};
    private static final String[] stoppedItems = {"Start", "Remove"};
    private static final String RUNNING = "running";
    private static final String STOPPED = "STOPPED";
    private static final FinderPath SERVER_GROUP_FINDER_PATH =
            new FinderPath().append(Ids.DOMAIN_BROWSE_BY, "server-groups");
    private static final String SERVER_GROUP = "sg-main-server-group";
    private static String[] SERVERS_IN_SERVER_GROUP;

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;


    @BeforeClass
    public static void setUp() throws IOException {
        SERVERS_IN_SERVER_GROUP =
                operations.invoke("read-children-resources", Address.host(ConfigUtils.getDefaultHost()),
                        Values.of("child-type", "server-config")).listValue()
                        .stream()
                        .map(ModelNode::asProperty)
                        .filter(property -> property.getValue().get("group").asString().equals(SERVER_GROUP))
                        .map(Property::getName)
                        .toArray(String[]::new);
    }

    @Before
    public void startServersInServerGroupIfNeeded() throws IOException {
        operations.invoke("start-servers",
                Address.root().and("server-group", "sg-main-server-group"), Values.of("blocking", true));
        verifyServersAreInState(Arrays.asList(SERVERS_IN_SERVER_GROUP), RUNNING, 150000);
    }

    /**
     * Test if drop down menu at Runtime -> Server Groups -> Server contains all items.
     * HAL-1582
     */
    @Test
    public void testMenuItems() {
        StringBuilder missingItems = new StringBuilder();
        FinderFragment fragment;
        fragment = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(Ids.DOMAIN_BROWSE_BY, "server-groups")
                .append(Ids.SERVER_GROUP, SERVER_GROUP));
        ItemFragment itemFragment = fragment.column(Ids.SERVER_GROUP).selectItem(Ids.SERVER_GROUP);
        for (String menuItem: baseItems) {
            if (!itemFragment.dropdown().containsItem(menuItem)) {
                if (!missingItems.toString().isEmpty()) {
                    missingItems.append(", ");
                }
                missingItems.append(menuItem);
            }
        }
        for (String menuItem: startedItems) {
            if (!itemFragment.dropdown().containsItem(menuItem)) {
                if (!missingItems.toString().isEmpty()) {
                    missingItems.append(", ");
                }
                missingItems.append(menuItem);
            }
        }
        changeStatus("Suspend");
        for (String menuItem: suspendedItems) {
            if (!itemFragment.dropdown().containsItem(menuItem)) {
                if (!missingItems.toString().isEmpty()) {
                    missingItems.append(", ");
                }
                missingItems.append(menuItem);
            }
        }
        changeStatus("Stop");
        for (String menuItem: stoppedItems) {
            if (!itemFragment.dropdown().containsItem(menuItem)) {
                if (!missingItems.toString().isEmpty()) {
                    missingItems.append(", ");
                }
                missingItems.append(menuItem);
            }
        }
        Assert.assertTrue("Menu should contains values " + missingItems.toString() + ". See HAL-1582.", missingItems.toString().isEmpty());
    }

    private void changeStatus(String status) {
        console.finder(NameTokens.RUNTIME, SERVER_GROUP_FINDER_PATH)
                .column(Ids.SERVER_GROUP)
                .selectItem(SERVER_GROUP)
                .dropdown()
                .click(status);
        console.confirmationDialog().confirm();
        console.verifySuccess();
        verifyServersAreInState(Arrays.asList(SERVERS_IN_SERVER_GROUP), STOPPED, 15000);
    }


    private void verifyServersAreInState(Collection<String> servers, String state, int timeout) {
        servers.parallelStream().forEach(server -> {
            try {
                verifyServerHasState(ConfigUtils.getDefaultHost(), server, state, timeout);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void verifyServerHasState(String host, String server, String state, int timeout)
            throws InterruptedException, IOException {
        String currentState;
        for (long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout);
             System.currentTimeMillis() < endTime; Thread.sleep(200L)) {
            currentState =
                    operations.readAttribute(Address.host(host).and("server", server), "server-state").stringValue();
            if (currentState.equals(state)) {
                return;
            }
        }
        Assert.fail("Server: [" + server + "] on host:[" + host + "] "
                + "could not get into the desired server-state:[" + state + "] after a timeout of " + timeout + " seconds");
    }

}