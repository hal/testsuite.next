package org.jboss.hal.testsuite.fragment.finder;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ServerStatusPreviewFragment {
    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    @Inject
    private Console console;

    // TODO Replace with Ids.SERVER_RUNTIME_STATUS_* as soon as HAL 3.3.7.Final has been released!
    @FindByJQuery("#server-runtime-status-heap-committed [aria-valuenow]")
    private WebElement heapCommitted;

    @FindByJQuery("#server-runtime-status-heap-used [aria-valuenow]")
    private WebElement heapUsed;

    @FindByJQuery("#server-runtime-status-non-heap-committed [aria-valuenow]")
    private WebElement nonHeapCommitted;

    @FindByJQuery("#server-runtime-status-non-heap-used [aria-valuenow]")
    private WebElement nonHeapUsed;

    @FindByJQuery("#server-runtime-status-threads [aria-valuenow]")
    private WebElement threads;

    public WebElement getHeapCommitted() {
        return heapCommitted;
    }

    public WebElement getHeapUsed() {
        return heapUsed;
    }

    public WebElement getNonHeapCommitted() {
        return nonHeapCommitted;
    }

    public WebElement getNonHeapUsed() {
        return nonHeapUsed;
    }

    public WebElement getThreads() {
        return threads;
    }
}
