package org.jboss.hal.testsuite.page;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class AbstractPage {

    @Drone
    protected WebDriver browser;

    @Inject
    protected Console console;

    @FindBy(id = Ids.ROOT_CONTAINER)
    private WebElement rootContainer;

    public WebElement getRootContainer() {
        return rootContainer;
    }

    protected Place assertPlace() {
        Place place = this.getClass().getAnnotation(Place.class);
        if (place == null) {
            throw new IllegalArgumentException(
                String.format("The page object '%s' that you are navigating to is not annotated with @Place",
                    this.getClass().getSimpleName()));
        }
        return place;
    }

    /** Navigates to the name token specified in the {@code @Place} annotation. */
    public abstract void navigate();

}
