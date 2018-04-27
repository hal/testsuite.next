package org.jboss.hal.testsuite.test.configuration.web.services;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class WebServicesFinderTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    @Test
    public void view() {
        console.finder(NameTokens.CONFIGURATION, new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS)))
            .column(Ids.CONFIGURATION_SUBSYSTEM).selectItem(NameTokens.WEBSERVICES).view();
        console.verify(new PlaceRequest.Builder().nameToken(NameTokens.WEBSERVICES).build());
    }
}
