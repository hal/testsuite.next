/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.hal.testsuite.page.configuration;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.MICROPROFILE_CONFIG)
public class MicroprofileConfigPage extends BasePage {

    @FindBy(id = "microprofile-config-source-table_wrapper")
    private TableFragment configSourcesTable;

    @FindBy(id = "microprofile-config-source-provider-table_wrapper")
    private TableFragment configProvidersTable;

    @FindBy(id = "microprofile-config-source-form")
    private FormFragment configSourceForm;

    @FindBy(id = "microprofile-config-source-provider-form")
    private FormFragment configProviderForm;

    @Inject
    private Console console;

    public TableFragment navigateToConfigSourcesTable() {
        navigate();
        console.verticalNavigation().selectPrimary("microprofile-config-source-item");
        console.waitNoNotification();
        return configSourcesTable;
    }

    public TableFragment navigateToConfigProvidersTable() {
        navigate();
        console.verticalNavigation().selectPrimary("microprofile-config-source-provider-item");
        console.waitNoNotification();
        return configProvidersTable;
    }

    public FormFragment openConfigSourceForm(String configSourceName) {
        navigateToConfigSourcesTable().select(configSourceName);
        return configSourceForm;
    }

    public FormFragment openConfigProviderForm(String configProviderName) {
        navigateToConfigProvidersTable().select(configProviderName);
        return configProviderForm;
    }
}
