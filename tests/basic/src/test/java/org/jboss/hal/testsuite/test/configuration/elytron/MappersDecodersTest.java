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
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronMappersDecodersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ROLES;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class MappersDecodersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String ANY_STRING = Random.name();

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(addPrefixRoleMapperAddress(ADD_PRE_UPDATE), Values.of(PREFIX, ANY_STRING));
        operations.add(addPrefixRoleMapperAddress(ADD_PRE_DELETE), Values.of(PREFIX, ANY_STRING));
        operations.add(addSuffixRoleMapperAddress(ADD_SUF_UPDATE), Values.of(SUFFIX, ANY_STRING));
        operations.add(addSuffixRoleMapperAddress(ADD_SUF_DELETE), Values.of(SUFFIX, ANY_STRING));
        operations.add(aggregateRoleMapperAddress(AGG_ROLE_UPDATE), Values.ofList(ROLE_MAPPERS, ADD_PRE_UPDATE, ADD_SUF_UPDATE));
        operations.add(aggregateRoleMapperAddress(AGG_ROLE_DELETE), Values.ofList(ROLE_MAPPERS, ADD_PRE_UPDATE, ADD_SUF_UPDATE));
        operations.add(constantRoleMapperAddress(CON_ROLE_UPDATE), Values.ofList(ROLES, ANY_STRING));
        operations.add(constantRoleMapperAddress(CON_ROLE_DELETE), Values.ofList(ROLES, ANY_STRING));
        operations.add(logicalRoleMapperAddress(LOG_ROLE_DELETE), Values.of(LOGICAL_OPERATION, AND));
        operations.add(logicalRoleMapperAddress(LOG_ROLE_UPDATE), Values.of(LOGICAL_OPERATION, AND));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(aggregateRoleMapperAddress(AGG_ROLE_CREATE));
        operations.remove(aggregateRoleMapperAddress(AGG_ROLE_UPDATE));
        operations.remove(aggregateRoleMapperAddress(AGG_ROLE_DELETE));
        operations.remove(addPrefixRoleMapperAddress(ADD_PRE_CREATE));
        operations.remove(addPrefixRoleMapperAddress(ADD_PRE_UPDATE));
        operations.remove(addPrefixRoleMapperAddress(ADD_PRE_DELETE));
        operations.remove(addSuffixRoleMapperAddress(ADD_SUF_CREATE));
        operations.remove(addSuffixRoleMapperAddress(ADD_SUF_UPDATE));
        operations.remove(addSuffixRoleMapperAddress(ADD_SUF_DELETE));
        operations.remove(constantRoleMapperAddress(CON_ROLE_CREATE));
        operations.remove(constantRoleMapperAddress(CON_ROLE_UPDATE));
        operations.remove(constantRoleMapperAddress(CON_ROLE_DELETE));
        operations.remove(logicalRoleMapperAddress(LOG_ROLE_DELETE));
        operations.remove(logicalRoleMapperAddress(LOG_ROLE_UPDATE));
        operations.remove(logicalRoleMapperAddress(LOG_ROLE_CREATE));
    }

    @Page private ElytronMappersDecodersPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- add-prefix-role-mapper

    @Test
    public void addPrefixRoleMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();

        crud.create(addPrefixRoleMapperAddress(ADD_PRE_CREATE), table, f -> {
            f.text(NAME, ADD_PRE_CREATE);
            f.text(PREFIX, ANY_STRING);
        });
    }

    @Test
    public void addPrefixRoleMapperTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();

        crud.createWithError(table, f -> f.text(NAME, ADD_PRE_CREATE), PREFIX);
    }

    @Test
    public void addPrefixRoleMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();
        FormFragment form = page.getAddPrefixRoleMapperForm();
        table.bind(form);
        table.select(ADD_PRE_UPDATE);
        crud.update(addPrefixRoleMapperAddress(ADD_PRE_UPDATE), form, f -> f.text(PREFIX, ANY_STRING),
                vg -> vg.verifyAttribute(PREFIX, ANY_STRING));
    }

    @Test
    public void addPrefixRoleMapperTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();
        FormFragment form = page.getAddPrefixRoleMapperForm();
        table.bind(form);
        table.select(ADD_PRE_UPDATE);
        crud.updateWithError(form, f -> f.clear(PREFIX), PREFIX);
    }

    @Test
    public void addPrefixRoleMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();

        crud.delete(addPrefixRoleMapperAddress(ADD_PRE_DELETE), table, ADD_PRE_DELETE);
    }

    // --------------- add-suffix-role-mapper

    @Test
    public void addSuffixRoleMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();

        crud.create(addSuffixRoleMapperAddress(ADD_SUF_CREATE), table, f -> {
            f.text(NAME, ADD_SUF_CREATE);
            f.text(SUFFIX, ANY_STRING);
        });
    }

    @Test
    public void addSuffixRoleMapperTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();

        crud.createWithError(table, f -> f.text(NAME, ADD_SUF_CREATE), SUFFIX);
    }

    @Test
    public void addSuffixRoleMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();
        FormFragment form = page.getAddSuffixRoleMapperForm();
        table.bind(form);
        table.select(ADD_SUF_UPDATE);
        crud.update(addSuffixRoleMapperAddress(ADD_SUF_UPDATE), form, f -> f.text(SUFFIX, ANY_STRING),
                vg -> vg.verifyAttribute(SUFFIX, ANY_STRING));
    }

    @Test
    public void addSuffixRoleMapperTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();
        FormFragment form = page.getAddSuffixRoleMapperForm();
        table.bind(form);
        table.select(ADD_SUF_UPDATE);
        crud.updateWithError(form, f -> f.clear(SUFFIX), SUFFIX);
    }

    @Test
    public void addSuffixRoleMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();

        crud.delete(addSuffixRoleMapperAddress(ADD_SUF_DELETE), table, ADD_SUF_DELETE);
    }


    // --------------- aggregate-role-mapper

    @Test
    public void aggregateRoleMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();

        crud.create(aggregateRoleMapperAddress(AGG_ROLE_CREATE), table, f -> {
            f.text(NAME, AGG_ROLE_CREATE);
            f.list(ROLE_MAPPERS).add(ADD_PRE_UPDATE).add(ADD_SUF_UPDATE);
        });
    }

    @Test
    public void aggregateRoleMapperTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();

        crud.createWithError(table, f -> f.text(NAME, AGG_ROLE_CREATE), ROLE_MAPPERS);
    }

    @Test
    public void aggregateRoleMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();
        FormFragment form = page.getAggregateRoleMapperForm();
        table.bind(form);
        table.select(AGG_ROLE_UPDATE);

        ModelNode expected = new ModelNode();
        expected.add(ADD_PRE_UPDATE);
        expected.add(ADD_SUF_UPDATE);
        expected.add(CON_ROLE_UPDATE);

        crud.update(aggregateRoleMapperAddress(AGG_ROLE_UPDATE), form, f -> f.list(ROLE_MAPPERS).add(CON_ROLE_UPDATE),
                vg -> vg.verifyAttribute(ROLE_MAPPERS, expected));
    }

    @Test
    public void aggregateRoleMapperTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();
        FormFragment form = page.getAggregateRoleMapperForm();
        table.bind(form);
        table.select(AGG_ROLE_UPDATE);
        crud.updateWithError(form, f -> f.list(ROLE_MAPPERS).removeTags(), ROLE_MAPPERS);
    }

    @Test
    public void aggregateRoleMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();

        crud.delete(aggregateRoleMapperAddress(AGG_ROLE_DELETE), table, AGG_ROLE_DELETE);
    }


    // --------------- constant-role-mapper

    @Test
    public void constantRoleMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();

        crud.create(constantRoleMapperAddress(CON_ROLE_CREATE), table, f -> {
            f.text(NAME, CON_ROLE_CREATE);
            f.list(ROLES).add(ANY_STRING);
        });
    }

    @Test
    public void constantRoleMapperTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();

        crud.createWithError(table, f -> f.text(NAME, CON_ROLE_CREATE), ROLES);
    }

    @Test
    public void constantRoleMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();
        FormFragment form = page.getConstantRoleMapperForm();
        table.bind(form);
        table.select(CON_ROLE_UPDATE);

        String roleRandom = Random.name();
        ModelNode expected = new ModelNode();
        expected.add(ANY_STRING);
        expected.add(roleRandom);

        crud.update(constantRoleMapperAddress(CON_ROLE_UPDATE), form, f -> f.list(ROLES).add(roleRandom),
                vg -> vg.verifyAttribute(ROLES, expected));
    }

    @Test
    public void constantRoleMapperTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();
        FormFragment form = page.getConstantRoleMapperForm();
        table.bind(form);
        table.select(CON_ROLE_UPDATE);
        crud.updateWithError(form, f -> f.list(ROLES).removeTags(), ROLES);
    }

    @Test
    public void constantRoleMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();

        crud.delete(constantRoleMapperAddress(CON_ROLE_DELETE), table, CON_ROLE_DELETE);
    }

    // --------------- logical-role-mapper

    @Test
    public void logicalRoleMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, LOGICAL_ROLE_MAPPER_ITEM);
        TableFragment table = page.getLogicalRoleMapperTable();

        crud.create(logicalRoleMapperAddress(LOG_ROLE_CREATE), table, f -> {
            f.text(NAME, LOG_ROLE_CREATE);
        });
    }

    @Test
    public void logicalRoleMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, LOGICAL_ROLE_MAPPER_ITEM);
        TableFragment table = page.getLogicalRoleMapperTable();
        FormFragment form = page.getLogicalRoleMapperForm();
        table.bind(form);
        table.select(LOG_ROLE_UPDATE);

        crud.update(logicalRoleMapperAddress(LOG_ROLE_UPDATE), form, f -> f.select(LOGICAL_OPERATION, OR),
                vg -> vg.verifyAttribute(LOGICAL_OPERATION, OR));
    }

    @Test
    public void logicalRoleMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, LOGICAL_ROLE_MAPPER_ITEM);
        TableFragment table = page.getLogicalRoleMapperTable();

        crud.delete(logicalRoleMapperAddress(LOG_ROLE_DELETE), table, LOG_ROLE_DELETE);
    }


}
