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
package org.jboss.hal.testsuite.test.configuration.deploymentscanner;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.DeploymentScannerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_CREATE;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_DELETE;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_READ;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_UPDATE_INVALID;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.DS_UPDATE_RESET;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.deploymentScannerAddress;
import static org.jboss.hal.testsuite.fixtures.DeploymentScannerFixtures.path;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class DeploymentScannerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(deploymentScannerAddress(DS_READ), Values.of(PATH, path(DS_READ)));
        operations.add(deploymentScannerAddress(DS_UPDATE), Values.of(PATH, path(DS_UPDATE)));
        operations.add(deploymentScannerAddress(DS_UPDATE_INVALID), Values.of(PATH, path(DS_UPDATE_INVALID)));
        operations.add(deploymentScannerAddress(DS_UPDATE_RESET), Values.of(PATH, path(DS_UPDATE_RESET)));
        operations.add(deploymentScannerAddress(DS_DELETE), Values.of(PATH, path(DS_DELETE)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(deploymentScannerAddress(DS_CREATE));
        operations.removeIfExists(deploymentScannerAddress(DS_READ));
        operations.removeIfExists(deploymentScannerAddress(DS_UPDATE));
        operations.removeIfExists(deploymentScannerAddress(DS_UPDATE_INVALID));
        operations.removeIfExists(deploymentScannerAddress(DS_UPDATE_RESET));
        operations.removeIfExists(deploymentScannerAddress(DS_DELETE));
    }

    @Page private DeploymentScannerPage page;
    @Inject private CrudOperations crud;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() {
        page.navigate();
        form = page.getForm();
        table = page.getTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(deploymentScannerAddress(DS_CREATE), table, form -> {
            form.text(NAME, DS_CREATE);
            form.text(PATH, path(DS_CREATE));
        });
    }

    @Test
    public void read() {
        table.select(DS_READ);
        assertEquals(path(DS_READ), form.value(PATH));
    }

    @Test
    public void update() throws Exception {
        table.select(DS_UPDATE);
        crud.update(deploymentScannerAddress(DS_UPDATE), form, PATH, Random.name() + "/" + Random.name());
    }

    @Test
    public void updateInvalidRelativeTo() throws Exception {
        table.select(DS_UPDATE_INVALID);
        form.edit();
        form.text(RELATIVE_TO, "invalid");
        form.save();

        console.verifyError();
        new ResourceVerifier(deploymentScannerAddress(DS_UPDATE), client)
                .verifyAttributeIsUndefined(RELATIVE_TO);
    }

    @Test
    public void reset() throws Exception {
        table.select(DS_UPDATE_RESET);
        crud.reset(deploymentScannerAddress(DS_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(deploymentScannerAddress(DS_DELETE), table, DS_DELETE);
    }
}
