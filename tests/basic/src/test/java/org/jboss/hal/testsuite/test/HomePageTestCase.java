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
package org.jboss.hal.testsuite.test;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.page.HomePage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class HomePageTestCase {

    @Drone private WebDriver browser;
    @Page private HomePage page;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void modules() throws Exception {
        assertTrue(containsModule("Deployments"));
        assertTrue(containsModule("Configuration"));
        assertTrue(containsModule("Runtime"));
        assertTrue(containsModule("Access Control"));
        assertTrue(containsModule("Patching"));

        By selector = ByJQuery.selector(".eap-home-module-header > h2:contains('Need Help?')");
        assertTrue(page.getRootContainer().findElement(selector).isDisplayed());
    }

    private boolean containsModule(String name) {
        return page.getModules().stream().anyMatch(e -> e.getText().equals(name));
    }
}
