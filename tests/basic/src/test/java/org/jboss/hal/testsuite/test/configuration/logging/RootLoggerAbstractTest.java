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
package org.jboss.hal.testsuite.test.configuration.logging;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.Before;
import org.junit.Test;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LEVEL;

public abstract class RootLoggerAbstractTest {

    protected abstract Address getLoggerAddress();
    protected abstract void navigateToPage();
    protected abstract LoggingConfigurationPage getPage();

    @Inject protected Console console;
    @Inject private CrudOperations crud;

    @Before
    public void navigate() throws Exception {
        navigateToPage();
    }

    @Test
    public void updateRootLogger() throws Exception {
        FormFragment form = getPage().getRootLoggerForm();
        crud.update(getLoggerAddress(), form,
                f -> f.select(LEVEL, "ERROR"),
                resourceVerifier -> resourceVerifier.verifyAttribute(LEVEL, "ERROR"));
    }

    @Test
    public void resetRootLogger() throws Exception {
        FormFragment form = getPage().getRootLoggerForm();
        crud.reset(getLoggerAddress(), form);
    }
}