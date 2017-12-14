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
package org.jboss.hal.testsuite.test.configuration.elytron;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronFactoriesTransformersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class FactoriesTransformersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String anyString = Random.name();
    private static ModelNode SERVER_FACTORIES = new ModelNode();
    private static Values PARAMS;

    static {
        SERVER_FACTORIES.add(PROV_HTTP_UPDATE);
        SERVER_FACTORIES.add(PROV_HTTP_UPDATE2);
        PARAMS = Values.of(HTTP_SERVER_MECH_FACTORIES, SERVER_FACTORIES);
    }

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), PARAMS);
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), PARAMS);
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE));
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE));
        operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
        operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_CREATE));
        operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE));
        operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE));
    }

    @Page private ElytronFactoriesTransformersPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- aggregate-http-server-mechanism-factory

    @Test
    public void aggregateHttpServerMechanismFactoryCreate() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crudOperations.create(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE), table, f -> {
            f.text(NAME, AGG_HTTP_CREATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE);
            f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE2);
        });
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreate() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        crudOperations.createWithError(table, AGG_HTTP_CREATE, HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryCreateMin() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, AGG_HTTP_CREATE);
        dialog.getForm().list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE3);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void aggregateHttpServerMechanismFactoryUpdate() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        FormFragment form = page.getAggregateHttpServerMechanismForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(PROV_HTTP_UPDATE).add(PROV_HTTP_UPDATE2).add(PROV_HTTP_UPDATE3);
        table.select(AGG_HTTP_UPDATE);
        crudOperations.update(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), form,
                f -> f.list(HTTP_SERVER_MECH_FACTORIES).add(PROV_HTTP_UPDATE3),
                changes -> changes.verifyAttribute(HTTP_SERVER_MECH_FACTORIES, expected));
    }

    @Test
    public void aggregateHttpServerMechanismFactoryTryUpdate() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        FormFragment form = page.getAggregateHttpServerMechanismForm();
        table.bind(form);

        table.select(AGG_HTTP_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(HTTP_SERVER_MECH_FACTORIES).removeTags(),
                HTTP_SERVER_MECH_FACTORIES);
    }

    @Test
    public void aggregateHttpServerMechanismFactoryDelete() throws Exception {
        secondNavigation(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getAggregateHttpServerMechanismTable();
        crudOperations.delete(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), table, AGG_HTTP_DELETE);
    }

    // --------------- configurable-http-server-mechanism-factory

    @Test
    public void configurableHttpServerMechanismFactoryCreate() throws Exception {
        secondNavigation(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crudOperations.create(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE), table, f -> {
            f.text(NAME, CONF_HTTP_CREATE);
            f.text(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE);
        });
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryCreate() throws Exception {
        secondNavigation(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();

        crudOperations.createWithError(table, CONF_HTTP_CREATE, HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryUpdate() throws Exception {
        secondNavigation(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_UPDATE);
        crudOperations.update(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), form,
                HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE2);
    }

    @Test
    public void configurableHttpServerMechanismFactoryTryUpdate() throws Exception {
        secondNavigation(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        FormFragment form = page.getConfigurableHttpServerMechanismForm();
        table.bind(form);

        table.select(CONF_HTTP_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(HTTP_SERVER_MECH_FACTORY),
                HTTP_SERVER_MECH_FACTORY);
    }

    @Test
    public void configurableHttpServerMechanismFactoryDelete() throws Exception {
        secondNavigation(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM);
        TableFragment table = page.getConfigurableHttpServerMechanismTable();
        crudOperations.delete(aggregateHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE), table, CONF_HTTP_DELETE);
    }

    // --------------- helper methods

    private void secondNavigation(String name) {
        console.verticalNavigation().selectSecondary(HTTP_FACTORIES_ITEM, name);
    }

}
