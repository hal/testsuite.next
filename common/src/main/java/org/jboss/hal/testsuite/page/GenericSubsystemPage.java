package org.jboss.hal.testsuite.page;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class GenericSubsystemPage {

    private static final String ADDRESS = "address";
    private static final String ROOT_SEPARATOR = "%255C0";
    private static final String SUBSYSTEM_SEPARATOR = "%255C2";

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @FindBy(id = Ids.ROOT_CONTAINER)
    private WebElement rootContainer;

    @FindBy(id = Ids.MODEL_BROWSER + "-resource-tab-container")
    private TabsFragment resourceTabContainer;

    @FindBy(id = "model-browser-" + Ids.MODEL_BROWSER_ROOT + "-form")
    private FormFragment dataForm;

    public void navigate() {
        browser.navigate().refresh();
        console.navigate(getPlaceRequest());
    }

    public PlaceRequest getPlaceRequest() {
        return new PlaceRequest.Builder().nameToken(NameTokens.GENERIC_SUBSYSTEM)
            .with(ADDRESS, placeToGenericSubsystemUrl()).build();
    }

    private String placeToGenericSubsystemUrl() {
        return ROOT_SEPARATOR
            + "subsystem"
            + SUBSYSTEM_SEPARATOR
            + assertPlace().value();
    }

    public FormFragment getDataForm() {
        resourceTabContainer.select(Ids.build(Ids.MODEL_BROWSER,"resource","data","tab"));
        return dataForm;
    }

    public WebElement getRootContainer() {
        return rootContainer;
    }

    private Place assertPlace() {
        Place place = this.getClass().getAnnotation(Place.class);
        if (place == null) {
            throw new IllegalArgumentException(
                String.format("The page object '%s' that you are navigating to is not annotated with @Place",
                    this.getClass().getSimpleName()));
        }
        return place;
    }

}
