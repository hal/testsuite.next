/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.test.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static java.util.Arrays.asList;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SUBSYSTEM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class SubsystemsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static List<String> subsystems = new ArrayList<>();
    // this is from org.jboss.hal.client.configuration.subsystem.SubsystemColumn
    private static final List<String> EMPTY_SUBSYSTEMS = asList("bean-validation", "ee-security", "jaxrs", "jdr",
            "microprofile-opentracing-smallrye", "jsr77", "pojo", "sar");

    @BeforeClass
    public static void beforeClass() throws Exception {
        subsystems = operations.readChildrenNames(Address.root(), SUBSYSTEM).stringListValue();
    }

    @Inject
    private Console console;
    private ColumnFragment column;
    private List<String> initialVisibleSubsystems;

    @Drone
    private WebDriver browser;

    @Before
    public void setUp() throws Exception {
        browser.navigate().refresh();
        column = console.finder(NameTokens.CONFIGURATION,
            new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS)))
            .column(Ids.CONFIGURATION_SUBSYSTEM);
        initialVisibleSubsystems = getVisibleSubsystems();
    }

    private List<String> getVisibleSubsystems() {
        return Collections.unmodifiableList(
            column.getItems().stream().map(item -> item.getAttribute("id")).collect(Collectors.toList()));
    }

    @Test
    public void numberOfSubsystems() {
        assertEquals(subsystems.size() - EMPTY_SUBSYSTEMS.size(), column.getItems().size());
    }

    @Test
    public void emptySubsystemsNotDisplayed() {
        for (String subsys : EMPTY_SUBSYSTEMS) {
            assertFalse(column.containsItem(subsys));
        }
    }

    @Test
    public void filter() {
        String filter = "io";
        column.filter(filter);

        long filtered = subsystems.stream()
            .filter(subsystem -> subsystem.toLowerCase().contains(filter)
                && EMPTY_SUBSYSTEMS.indexOf(subsystem) < 0)
            .count();
        long visible = column.getItems().stream()
            .filter(WebElement::isDisplayed)
            .count();
        assertEquals(filtered, visible);
    }

    @Test
    public void pinSingle() {
        String subsystemToBePinned = initialVisibleSubsystems.get(Random.number(0, initialVisibleSubsystems.size()));
        pin(subsystemToBePinned);
        Assert.assertEquals("Newly pinned item should be on top of the subsystem's list", 0,
            getVisibleSubsystems().indexOf(subsystemToBePinned));
        unpin(subsystemToBePinned);
    }

    private void pin(String subsystemId) {
        WebElement element = column.selectItem(subsystemId).getRoot();
        console.scrollIntoView(element);
        new Actions(browser).moveToElement(element).perform();
        element.findElement(By.className("pin")).click();
    }

    private void unpin(String subsystemId) {
        WebElement element = column.selectItem(subsystemId).getRoot();
        console.scrollIntoView(element);
        new Actions(browser).moveToElement(element).perform();
        element.findElement(By.className("unpin")).click();
    }

    @Test
    public void pinMultiple() {
        List<String> subsystemsToBePinned =
            initialVisibleSubsystems.subList(initialVisibleSubsystems.size() - 3, initialVisibleSubsystems.size());
        subsystemsToBePinned.forEach(this::pin);
        Assert.assertEquals("Newly pinned subsystems should be on top of the subsystem list in chronological order",
            subsystemsToBePinned, getVisibleSubsystems().subList(0, subsystemsToBePinned.size()));
        subsystemsToBePinned.forEach(this::unpin);
    }

    @Test
    public void unpinSingle() {
        int pinnedSubsystemIndex = Random.number(0, initialVisibleSubsystems.size());
        String subsystemToPinned = initialVisibleSubsystems.get(pinnedSubsystemIndex);
        pin(subsystemToPinned);
        Assert.assertEquals("Pinned subsystem should be on top of the subsystem list before removal",
            0, getVisibleSubsystems().indexOf(subsystemToPinned));
        unpin(subsystemToPinned);
        Assert.assertEquals("Subsystem should return to the same position after unpin", pinnedSubsystemIndex,
            getVisibleSubsystems().indexOf(subsystemToPinned));
    }

    @Test
    public void unpinMultiple() {
        List<String> subsystemsToPinned = initialVisibleSubsystems.subList(initialVisibleSubsystems.size() - 3, initialVisibleSubsystems
            .size());
        subsystemsToPinned.forEach(this::pin);
        Assert.assertEquals("Pinned subsystems should be on top of the subsystem list before unpinning",
            subsystemsToPinned, getVisibleSubsystems().subList(0, subsystemsToPinned.size()));
        subsystemsToPinned.forEach(this::unpin);
        Assert.assertEquals("Subsystems should be in previous order after unpinning", initialVisibleSubsystems,
            getVisibleSubsystems());
    }
}
