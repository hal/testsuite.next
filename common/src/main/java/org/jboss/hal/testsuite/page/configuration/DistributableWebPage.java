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
package org.jboss.hal.testsuite.page.configuration;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.CSS.bootstrapSelect;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.DISTRIBUTABLE_WEB)
public class DistributableWebPage extends BasePage {

    @FindBy(id = "dw-configuration-form") private FormFragment configurationForm;
    @FindBy(id = "dw-hotrod-session-management-table" + WRAPPER) private TableFragment hotrodSessionManagementTable;
    @FindBy(id = "dw-hotrod-session-management-form") private FormFragment hotrodSessionManagementForm;
    @FindBy(id = "dw-hotrod-sso-management-table" + WRAPPER) private TableFragment hotrodSSOManagementTable;
    @FindBy(id = "dw-hotrod-sso-management-form") private FormFragment hotrodSSOManagementForm;
    @FindBy(id = "dw-infinispan-session-management-table" + WRAPPER) private TableFragment infinispanSessionManagementTable;
    @FindBy(id = "dw-infinispan-session-management-container") private TabsFragment infinispanSessionManagementTabs;
    @FindBy(id = "dw-infinispan-session-management-form") private FormFragment infinispanSessionManagementForm;
    @FindBy(id = "dw-infinispan-sso-management-table" + WRAPPER) private TableFragment infinispanSSOManagementTable;
    @FindBy(id = "dw-infinispan-sso-management-form") private FormFragment infinispanSSOManagementForm;
    @FindBy(id = "dw-routing-infinispan-form") private FormFragment routingForm;
    // @FindBy(id="dw-routing-select") private Select

    public FormFragment getConfigurationForm() {
        return configurationForm;
    }

    public TableFragment getHotrodSessionManagementTable() {
        return hotrodSessionManagementTable;
    }

    public FormFragment getHotrodSessionManagementForm() {
        return hotrodSessionManagementForm;
    }

    public TableFragment getHotrodSSOManagementTable() {
        return hotrodSSOManagementTable;
    }

    public FormFragment getHotrodSSOManagementForm() {
        return hotrodSSOManagementForm;
    }

    public TableFragment getInfinispanSessionManagementTable() {
        return infinispanSessionManagementTable;
    }

    public TabsFragment getInfinispanSessionManagementTabs() {
        return infinispanSessionManagementTabs;
    }

    public SelectFragment getSwitchAffinity() {
        WebElement element = getRootContainer().findElement(By.cssSelector("#dw-infinispan-session-management-affinity-tab .header-form ." + bootstrapSelect));
        if (element != null) {
            return Graphene.createPageFragment(SelectFragment.class, element);
        }
        return null;
    }

    public FormFragment getInfinispanSessionManagementForm() {
        return infinispanSessionManagementForm;
    }

    public TableFragment getInfinispanSSOManagementTable() {
        return infinispanSSOManagementTable;
    }

    public FormFragment getInfinispanSSOManagementForm() {
        return infinispanSSOManagementForm;
    }

    public FormFragment getRoutingForm() {
        return routingForm;
    }

    public SelectFragment getSwitchRouting() {
        WebElement element = getRootContainer().findElement(By.cssSelector("section[data-vn-item-for=dw-routing-item] .header-form ." + bootstrapSelect));
        if (element != null) {
            return Graphene.createPageFragment(SelectFragment.class, element);
        }
        return null;
    }
}
