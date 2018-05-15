package org.jboss.hal.testsuite.test.runtime.multihosts;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.DOMAIN_BROWSE_BY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verifies disconnected host info shown in web console.
 * Requires domain containing disconnected host named 'disconnected-slave'.
 */
@Category(Domain.class)
@RunWith(Arquillian.class)
public class DisconnectedHostsTest {

    @Inject private Console console;
    private static final OnlineManagementClient client = ManagementClientProvider.withoutDefaultHost();
    private Operations ops = new Operations(client);

    private static final String
        CONNECTED_HOST_NAME = ConfigUtils.getDefaultHost(),
        CONNECTED_ICON_CLASS = "pficon-ok",
        DISCONNECTED_HOST_NAME = "disconnected-slave",
        DISCONNECTED_ICON_CLASS = "fa-chain-broken";

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }

    @Test
    public void showDisconnectedTest() throws IOException {

        // verify connected icon for master
        selectHostAssertIconClassReturnFinder(CONNECTED_HOST_NAME, CONNECTED_ICON_CLASS);

        // verify disconnected icon for slave
        FinderFragment finder = selectHostAssertIconClassReturnFinder(DISCONNECTED_HOST_NAME, DISCONNECTED_ICON_CLASS);

        // verify last (dis)connected times for slave
        Map<String, String> mainAttributesMap = finder.preview().getMainAttributes();
        List<ModelNode> eventList = getSlaveConnectionEventList();
        String lastRegisteredDatetimeFromModel = getLastDatetime(eventList, REGISTERED);
        String lastUnregisteredDatetimeFromModel = getLastDatetime(eventList, UNREGISTERED);

        assertEquals(lastRegisteredDatetimeFromModel, mainAttributesMap.get("Last Connected"));
        assertEquals(lastUnregisteredDatetimeFromModel, mainAttributesMap.get("Disconnected"));
    }

    private FinderFragment selectHostAssertIconClassReturnFinder(String hostName, String expectedIconClass) {
        FinderFragment finder = console.finder(NameTokens.RUNTIME, new FinderPath().append(DOMAIN_BROWSE_BY, HOSTS));
        String hostIconClasses = finder.column(HOST).selectItem(HOST + "-" + hostName).getIconClasses();
        String failMessage = "'" + hostName + "' host menu item should have '" + expectedIconClass + "' icon class!";
        assertTrue(failMessage, hostIconClasses.contains(expectedIconClass));
        return finder;
    }

    private List<ModelNode> getSlaveConnectionEventList() throws IOException {
        ModelNodeResult eventsResult = ops.readAttribute(Address.of(CORE_SERVICE, ModelDescriptionConstants.MANAGEMENT)
                .and(HOST_CONNECTION, DISCONNECTED_HOST_NAME), EVENTS);
        eventsResult.assertSuccess();
        List<ModelNode> eventList = eventsResult.value().asList();
        return eventList;
    }

    private String getLastDatetime(List<ModelNode> eventList, String eventType) {
        long maxTimestamp = eventList.stream().filter(event -> event.get(TYPE).asString().equals(eventType))
                .mapToLong(event -> event.get(TIMESTAMP).asLong()).max().getAsLong();
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(maxTimestamp),
                ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ", "
                + ldt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }
}
