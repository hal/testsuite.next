package org.jboss.hal.testsuite.fragment.finder;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LogFilePreviewFragment extends FinderPreviewFragment {

    @Drone
    private WebDriver browser;

    @FindByJQuery(".log-file-preview")
    private WebElement preview;

    public String preview() {
        return preview.getText();
    }

}
