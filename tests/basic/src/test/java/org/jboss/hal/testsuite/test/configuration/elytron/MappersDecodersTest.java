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
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
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
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.PREFIX;

@RunWith(Arquillian.class)
public class MappersDecodersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String ANY_STRING = Random.name();
    private static final String ACTION = "action";
    private static final String PERM_CREATE_CLASS = "org.wildfly.security.auth.permission.LoginPermission";
    private static final String PERM_UPDATE_CLASS = "org.wildfly.security.auth.permission.ChangeRoleMapperPermission";
    private static final String PERM_DELETE_CLASS = "org.wildfly.security.auth.permission.RunAsPrincipalPermission";
    private static final String ASTERISK = "*";


    @BeforeClass
    public static void beforeTests() throws Exception {

        // role mappers
        operations.add(addPrefixRoleMapperAddress(ADD_PRE_UPDATE), Values.of(PREFIX, ANY_STRING)).assertSuccess();
        operations.add(addPrefixRoleMapperAddress(ADD_PRE_DELETE), Values.of(PREFIX, ANY_STRING)).assertSuccess();
        operations.add(addSuffixRoleMapperAddress(ADD_SUF_UPDATE), Values.of(SUFFIX, ANY_STRING)).assertSuccess();
        operations.add(addSuffixRoleMapperAddress(ADD_SUF_DELETE), Values.of(SUFFIX, ANY_STRING)).assertSuccess();
        operations.add(aggregateRoleMapperAddress(AGG_ROLE_UPDATE), Values.ofList(ROLE_MAPPERS, ADD_PRE_UPDATE, ADD_SUF_UPDATE)).assertSuccess();
        operations.add(aggregateRoleMapperAddress(AGG_ROLE_DELETE), Values.ofList(ROLE_MAPPERS, ADD_PRE_UPDATE, ADD_SUF_UPDATE)).assertSuccess();
        operations.add(constantRoleMapperAddress(CON_ROLE_UPDATE), Values.ofList(ROLES, ANY_STRING)).assertSuccess();
        operations.add(constantRoleMapperAddress(CON_ROLE_DELETE), Values.ofList(ROLES, ANY_STRING)).assertSuccess();
        operations.add(logicalRoleMapperAddress(LOG_ROLE_DELETE), Values.of(LOGICAL_OPERATION, AND)).assertSuccess();
        operations.add(logicalRoleMapperAddress(LOG_ROLE_UPDATE), Values.of(LOGICAL_OPERATION, AND)).assertSuccess();

        // permission mappers
        ModelNode permissionDelete = new ModelNodeGenerator.ModelNodePropertiesBuilder()
                .addProperty(CLASS_NAME, PERM_DELETE_CLASS).addProperty(TARGET_NAME, ASTERISK).build();
        ModelNode permissionUpdate = new ModelNodeGenerator.ModelNodePropertiesBuilder()
                .addProperty(CLASS_NAME, PERM_UPDATE_CLASS).addProperty(TARGET_NAME, ASTERISK).build();
        Values constantParams = Values.ofList(PERMISSIONS, permissionUpdate, permissionDelete);
        operations.add(constantPermissionMapperAddress(CON_PERM_UPDATE), constantParams).assertSuccess();
        operations.add(constantPermissionMapperAddress(CON_PERM_UPDATE2)).assertSuccess();
        operations.add(constantPermissionMapperAddress(CON_PERM_DELETE)).assertSuccess();
        Values logicalParams = Values.of(LEFT, CON_PERM_UPDATE).and(LOGICAL_OPERATION, AND).and(RIGHT, CON_PERM_UPDATE2);
        operations.add(logicalPermissionMapperAddress(LOG_PERM_UPDATE), logicalParams).assertSuccess();
        operations.add(logicalPermissionMapperAddress(LOG_PERM_DELETE), logicalParams).assertSuccess();
        operations.add(simplePermissionMapperAddress(SIM_PERM_UPDATE)).assertSuccess();
        operations.add(simplePermissionMapperAddress(SIM_PERM_DELETE)).assertSuccess();

        // principal decoder
        Values consPriDecoderParam = Values.of(CONSTANT, ANY_STRING);
        operations.add(constantPrincipalDecoderAddress(CONS_PRI_UPDATE), consPriDecoderParam).assertSuccess();
        operations.add(constantPrincipalDecoderAddress(CONS_PRI_UPDATE2), consPriDecoderParam).assertSuccess();
        operations.add(constantPrincipalDecoderAddress(CONS_PRI_UPDATE3), consPriDecoderParam).assertSuccess();
        operations.add(constantPrincipalDecoderAddress(CONS_PRI_DELETE), consPriDecoderParam).assertSuccess();
        Values aggPriDecoderParam = Values.ofList(PRINCIPAL_DECODERS, CONS_PRI_UPDATE, CONS_PRI_UPDATE2);
        operations.add(aggregatePrincipalDecoderAddress(AGG_PRI_UPDATE), aggPriDecoderParam).assertSuccess();
        operations.add(aggregatePrincipalDecoderAddress(AGG_PRI_DELETE), aggPriDecoderParam).assertSuccess();
        operations.add(concatenatingPrincipalDecoderAddress(CONC_PRI_UPDATE), aggPriDecoderParam).assertSuccess();
        operations.add(concatenatingPrincipalDecoderAddress(CONC_PRI_DELETE), aggPriDecoderParam).assertSuccess();
        operations.add(x500PrincipalDecoderAddress(X500_PRI_UPDATE), Values.of(OID, ANY_STRING)).assertSuccess();
        operations.add(x500PrincipalDecoderAddress(X500_PRI_DELETE), Values.of(OID, ANY_STRING)).assertSuccess();

        // role decoder
        operations.add(simpleRoleDecoderAddress(SIMP_ROLE_UPDATE), Values.of(ATTRIBUTE, ANY_STRING)).assertSuccess();
        operations.add(simpleRoleDecoderAddress(SIMP_ROLE_DELETE), Values.of(ATTRIBUTE, ANY_STRING)).assertSuccess();

    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            // role mappers
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
            // permission mappers
            operations.remove(logicalPermissionMapperAddress(LOG_PERM_UPDATE));
            operations.remove(logicalPermissionMapperAddress(LOG_PERM_DELETE));
            operations.remove(logicalPermissionMapperAddress(LOG_PERM_CREATE));
            operations.remove(constantPermissionMapperAddress(CON_PERM_UPDATE));
            operations.remove(constantPermissionMapperAddress(CON_PERM_UPDATE2));
            operations.remove(constantPermissionMapperAddress(CON_PERM_DELETE));
            operations.remove(constantPermissionMapperAddress(CON_PERM_CREATE));
            operations.remove(simplePermissionMapperAddress(SIM_PERM_UPDATE));
            operations.remove(simplePermissionMapperAddress(SIM_PERM_DELETE));
            operations.remove(simplePermissionMapperAddress(SIM_PERM_CREATE));
            // principal decoder
            operations.remove(aggregatePrincipalDecoderAddress(AGG_PRI_DELETE));
            operations.remove(aggregatePrincipalDecoderAddress(AGG_PRI_UPDATE));
            operations.remove(aggregatePrincipalDecoderAddress(AGG_PRI_CREATE));
            operations.remove(concatenatingPrincipalDecoderAddress(CONC_PRI_CREATE));
            operations.remove(concatenatingPrincipalDecoderAddress(CONC_PRI_UPDATE));
            operations.remove(concatenatingPrincipalDecoderAddress(CONC_PRI_DELETE));
            operations.remove(constantPrincipalDecoderAddress(CONS_PRI_DELETE));
            operations.remove(constantPrincipalDecoderAddress(CONS_PRI_UPDATE));
            operations.remove(constantPrincipalDecoderAddress(CONS_PRI_UPDATE2));
            operations.remove(constantPrincipalDecoderAddress(CONS_PRI_UPDATE3));
            operations.remove(constantPrincipalDecoderAddress(CONS_PRI_CREATE));
            operations.remove(x500PrincipalDecoderAddress(X500_PRI_UPDATE));
            operations.remove(x500PrincipalDecoderAddress(X500_PRI_DELETE));
            operations.remove(x500PrincipalDecoderAddress(X500_PRI_CREATE));
            // role decoder
            operations.remove(simpleRoleDecoderAddress(SIMP_ROLE_DELETE));
            operations.remove(simpleRoleDecoderAddress(SIMP_ROLE_CREATE));
            operations.remove(simpleRoleDecoderAddress(SIMP_ROLE_UPDATE));

            new Administration(client).reloadIfRequired();
        } finally {
            client.close();
        }
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
    public void addPrefixRoleMapperTryCreate() {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_PREFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddPrefixRoleMapperTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, ADD_PRE_CREATE), PREFIX);
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
    public void addPrefixRoleMapperTryUpdate() {
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
    public void addSuffixRoleMapperTryCreate() {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, ADD_SUFFIX_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAddSuffixRoleMapperTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, ADD_SUF_CREATE), SUFFIX);
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
    public void addSuffixRoleMapperTryUpdate() {
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
    public void aggregateRoleMapperTryCreate() {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, AGGREGATE_ROLE_MAPPER_ITEM);
        TableFragment table = page.getAggregateRoleMapperTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, AGG_ROLE_CREATE), ROLE_MAPPERS);
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
    public void aggregateRoleMapperTryUpdate() {
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
    public void constantRoleMapperTryCreate() {
        console.verticalNavigation().selectSecondary(ROLE_MAPPERS_ITEM, CONSTANT_ROLE_MAPPER_ITEM);
        TableFragment table = page.getConstantRoleMapperTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CON_ROLE_CREATE), ROLES);
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
    public void constantRoleMapperTryUpdate() {
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

        crud.create(logicalRoleMapperAddress(LOG_ROLE_CREATE), table, f -> f.text(NAME, LOG_ROLE_CREATE));
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

    // --------------- logical-permission-mapper

    @Test
    public void logicalPermissionMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, LOGICAL_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getLogicalPermissionMapperTable();

        crud.create(logicalPermissionMapperAddress(LOG_PERM_CREATE), table, f -> {
            f.text(NAME, LOG_PERM_CREATE);
            f.text(LEFT, CON_PERM_UPDATE);
            f.text(RIGHT, CON_PERM_UPDATE2);
            // f.select(LOGICAL_OPERATION, AND);
        });
    }

    @Test
    public void logicalPermissionMapperTryCreate() {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, LOGICAL_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getLogicalPermissionMapperTable();

        crud.createWithErrorAndCancelDialog(table, f -> {
            f.text(NAME, LOG_PERM_CREATE);
            f.text(RIGHT, CON_PERM_UPDATE2);
        }, LEFT);
    }

    @Test
    public void logicalPermissionMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, LOGICAL_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getLogicalPermissionMapperTable();
        FormFragment form = page.getLogicalPermissionMapperForm();
        table.bind(form);
        table.select(LOG_PERM_UPDATE);

        crud.update(logicalPermissionMapperAddress(LOG_PERM_UPDATE), form, f -> f.select(LOGICAL_OPERATION, OR),
                vg -> vg.verifyAttribute(LOGICAL_OPERATION, OR));
    }

    @Test
    public void logicalPermissionMapperTryUpdate() {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, LOGICAL_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getLogicalPermissionMapperTable();
        FormFragment form = page.getLogicalPermissionMapperForm();
        table.bind(form);
        table.select(LOG_PERM_UPDATE);

        crud.updateWithError(form, f -> f.clear(LEFT), LEFT);
    }

    @Test
    public void logicalPermissionMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, LOGICAL_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getLogicalPermissionMapperTable();

        crud.delete(logicalPermissionMapperAddress(LOG_PERM_DELETE), table, LOG_PERM_DELETE);
    }

    // --------------- constant-permission-mapper

    @Test
    public void constantPermissionMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, CONSTANT_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getConstantPermissionMapperTable();

        crud.create(constantPermissionMapperAddress(CON_PERM_CREATE), table, CON_PERM_CREATE);
    }

    @Test
    public void constantPermissionMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, CONSTANT_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getConstantPermissionMapperTable();

        crud.delete(constantPermissionMapperAddress(CON_PERM_DELETE), table, CON_PERM_DELETE);
    }

    @Test
    public void constantPermissionMapperPermissionsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, CONSTANT_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getConstantPermissionMapperTable();
        TableFragment permissionsTable = page.getConstantPermissionMapperPermissionsTable();

        table.action(CON_PERM_UPDATE, Names.PERMISSIONS);
        waitGui().until().element(permissionsTable.getRoot()).is().visible();

        crud.create(constantPermissionMapperAddress(CON_PERM_UPDATE), permissionsTable,
                f -> f.text(CLASS_NAME, PERM_CREATE_CLASS),
                vc -> vc.verifyListAttributeContainsSingleValue(PERMISSIONS, CLASS_NAME, PERM_CREATE_CLASS));
    }

    @Test
    public void constantPermissionMapperPermissionsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, CONSTANT_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getConstantPermissionMapperTable();
        TableFragment permissionsTable = page.getConstantPermissionMapperPermissionsTable();

        table.action(CON_PERM_UPDATE, Names.PERMISSIONS);
        waitGui().until().element(permissionsTable.getRoot()).is().visible();

        FormFragment form = page.getConstantPermissionMapperPermissionsForm();
        permissionsTable.bind(form);
        permissionsTable.select(PERM_UPDATE_CLASS);

        crud.update(constantPermissionMapperAddress(CON_PERM_UPDATE), form,
                f -> f.text(ACTION, ANY_STRING),
                vc -> vc.verifyListAttributeContainsSingleValue(PERMISSIONS, ACTION, ANY_STRING));
    }

    @Test
    public void constantPermissionMapperPermissionsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, CONSTANT_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getConstantPermissionMapperTable();
        TableFragment permissionsTable = page.getConstantPermissionMapperPermissionsTable();

        table.action(CON_PERM_UPDATE, Names.PERMISSIONS);
        waitGui().until().element(permissionsTable.getRoot()).is().visible();

        crud.delete(constantPermissionMapperAddress(CON_PERM_UPDATE), permissionsTable, PERM_DELETE_CLASS,
                vc -> vc.verifyListAttributeDoesNotContainSingleValue(PERMISSIONS, CLASS_NAME, PERM_DELETE_CLASS));
    }

    // --------------- simple-permission-mapper

    @Test
    public void simplePermissionMapperCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, SIMPLE_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getSimplePermissionMapperTable();

        crud.create(simplePermissionMapperAddress(SIM_PERM_CREATE), table, SIM_PERM_CREATE);
    }

    @Test
    public void simplePermissionMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, SIMPLE_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getSimplePermissionMapperTable();
        FormFragment form = page.getSimplePermissionMapperForm();
        table.bind(form);
        table.select(SIM_PERM_UPDATE);

        crud.update(simplePermissionMapperAddress(SIM_PERM_UPDATE), form, f -> f.select(MAPPING_MODE, OR),
                vg -> vg.verifyAttribute(MAPPING_MODE, OR));
    }

    @Test
    public void simplePermissionMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, SIMPLE_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getSimplePermissionMapperTable();

        crud.delete(simplePermissionMapperAddress(SIM_PERM_DELETE), table, SIM_PERM_DELETE);
    }

    @Test
    public void simplePermissionMapperPermissionsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PERMISSION_MAPPER_ITEM, SIMPLE_PERMISSION_MAPPER_ITEM);
        TableFragment table = page.getSimplePermissionMapperTable();
        TableFragment permissionsTable = page.getSimplePMPermissionMappingsTable();

        table.action(SIM_PERM_UPDATE, Names.PERMISSION_MAPPINGS);
        waitGui().until().element(permissionsTable.getRoot()).is().visible();

        ModelNode principals = new ModelNode();
        principals.add(PERM_MAP_CREATE);
        crud.create(simplePermissionMapperAddress(SIM_PERM_UPDATE), permissionsTable,
                f -> f.list(PRINCIPALS).add(PERM_MAP_CREATE),
                vc -> vc.verifyListAttributeContainsSingleValue(PERMISSION_MAPPINGS, PRINCIPALS, principals));
    }

    // --------------- aggregate-principal-decoder

    @Test
    public void aggregatePrincipalDecoderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, AGGREGATE_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getAggregatePrincipalDecoderTable();

        crud.create(aggregatePrincipalDecoderAddress(AGG_PRI_CREATE), table,
                f -> {
                    f.text(NAME, AGG_PRI_CREATE);
                    f.list(PRINCIPAL_DECODERS).add(CONS_PRI_UPDATE).add(CONS_PRI_UPDATE2);
                },
                vg -> vg.verifyListAttributeContainsValue(PRINCIPAL_DECODERS, CONS_PRI_UPDATE2));
    }

    @Test
    public void aggregatePrincipalDecoderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, AGGREGATE_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getAggregatePrincipalDecoderTable();
        FormFragment form = page.getAggregatePrincipalDecoderForm();
        table.bind(form);
        table.select(AGG_PRI_UPDATE);

        ModelNode principalDecoders = new ModelNode();
        principalDecoders.add(CONS_PRI_UPDATE);
        principalDecoders.add(CONS_PRI_UPDATE2);
        principalDecoders.add(CONS_PRI_UPDATE3);
        crud.update(aggregatePrincipalDecoderAddress(AGG_PRI_UPDATE), form,
                f -> f.list(PRINCIPAL_DECODERS).add(CONS_PRI_UPDATE3),
                vg -> vg.verifyAttribute(PRINCIPAL_DECODERS, principalDecoders));
    }

    @Test
    public void aggregatePrincipalDecoderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, AGGREGATE_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getAggregatePrincipalDecoderTable();

        crud.delete(aggregatePrincipalDecoderAddress(AGG_PRI_DELETE), table, AGG_PRI_DELETE);
    }

    // --------------- concatenating-principal-decoder

    @Test
    public void concatenatingPrincipalDecoderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONCATENATING_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConcatenatingPrincipalDecoderTable();

        crud.create(concatenatingPrincipalDecoderAddress(CONC_PRI_CREATE), table,
                f -> {
                    f.text(NAME, CONC_PRI_CREATE);
                    f.list(PRINCIPAL_DECODERS).add(CONS_PRI_UPDATE).add(CONS_PRI_UPDATE2);
                },
                vg -> vg.verifyListAttributeContainsValue(PRINCIPAL_DECODERS, CONS_PRI_UPDATE2));
    }

    @Test
    public void concatenatingPrincipalDecoderTryCreate() {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONCATENATING_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConcatenatingPrincipalDecoderTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CONC_PRI_CREATE),PRINCIPAL_DECODERS);
    }

    @Test
    public void concatenatingPrincipalDecoderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONCATENATING_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConcatenatingPrincipalDecoderTable();
        FormFragment form = page.getConcatenatingPrincipalDecoderForm();
        table.bind(form);
        table.select(CONC_PRI_UPDATE);

        crud.update(concatenatingPrincipalDecoderAddress(CONC_PRI_UPDATE), form, JOINER);
    }

    @Test
    public void concatenatingPrincipalDecoderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONCATENATING_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConcatenatingPrincipalDecoderTable();

        crud.delete(concatenatingPrincipalDecoderAddress(CONC_PRI_DELETE), table, CONC_PRI_DELETE);
    }

    // --------------- constant-principal-decoder

    @Test
    public void constantPrincipalDecoderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONSTANT_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConstantPrincipalDecoderTable();

        crud.create(constantPrincipalDecoderAddress(CONS_PRI_CREATE), table,
                f -> {
                    f.text(NAME, CONS_PRI_CREATE);
                    f.text(CONSTANT, ANY_STRING);
                });
    }

    @Test
    public void constantPrincipalDecoderTryCreate() {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONSTANT_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConstantPrincipalDecoderTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CONS_PRI_CREATE), CONSTANT);
    }

    @Test
    public void constantPrincipalDecoderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONSTANT_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConstantPrincipalDecoderTable();
        FormFragment form = page.getConstantPrincipalDecoderForm();
        table.bind(form);
        table.select(CONS_PRI_UPDATE);

        crud.update(constantPrincipalDecoderAddress(CONS_PRI_UPDATE), form, CONSTANT);
    }

    @Test
    public void constantPrincipalDecoderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, CONSTANT_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getConstantPrincipalDecoderTable();

        crud.delete(constantPrincipalDecoderAddress(CONS_PRI_DELETE), table, CONS_PRI_DELETE);
    }

    // --------------- x500-attribute-principal-decoder

    @Test
    public void x500AttributePrincipalDecoderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, X500_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getX500PrincipalDecoderTable();

        crud.create(x500PrincipalDecoderAddress(X500_PRI_CREATE), table,
                f -> {
                    f.text(NAME, X500_PRI_CREATE);
                    f.text(OID, ANY_STRING);
                });
    }

    @Test
    public void x500AttributePrincipalDecoderTryCreate() {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, X500_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getX500PrincipalDecoderTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, X500_PRI_CREATE), OID);
    }

    @Test
    public void x500AttributePrincipalDecoderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, X500_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getX500PrincipalDecoderTable();
        FormFragment form = page.getX500PrincipalDecoderForm();
        table.bind(form);
        table.select(X500_PRI_UPDATE);

        crud.update(x500PrincipalDecoderAddress(X500_PRI_UPDATE), form, JOINER);
    }

    @Test
    public void x500AttributePrincipalDecoderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(PRINCIPAL_DECODER_ITEM, X500_PRINCIPAL_DECODER_ITEM);
        TableFragment table = page.getX500PrincipalDecoderTable();

        crud.delete(x500PrincipalDecoderAddress(X500_PRI_DELETE), table, X500_PRI_DELETE);
    }

    // --------------- simple-role-decoder

    @Test
    public void simpleRoleDecoderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_DECODER_ITEM, SIMPLE_ROLE_DECODER_ITEM);
        TableFragment table = page.getSimpleRoleDecoderTable();

        crud.create(simpleRoleDecoderAddress(SIMP_ROLE_CREATE), table,
                f -> {
                    f.text(NAME, SIMP_ROLE_CREATE);
                    f.text(ATTRIBUTE, ANY_STRING);
                });
    }

    @Test
    public void simpleRoleDecoderTryCreate() {
        console.verticalNavigation().selectSecondary(ROLE_DECODER_ITEM, SIMPLE_ROLE_DECODER_ITEM);
        TableFragment table = page.getSimpleRoleDecoderTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, SIMP_ROLE_CREATE), ATTRIBUTE);
    }

    @Test
    public void simpleRoleDecoderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_DECODER_ITEM, SIMPLE_ROLE_DECODER_ITEM);
        TableFragment table = page.getSimpleRoleDecoderTable();
        FormFragment form = page.getSimpleRoleDecoderForm();
        table.bind(form);
        table.select(SIMP_ROLE_UPDATE);

        crud.update(simpleRoleDecoderAddress(SIMP_ROLE_UPDATE), form, ATTRIBUTE);
    }

    @Test
    public void simpleRoleDecoderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(ROLE_DECODER_ITEM, SIMPLE_ROLE_DECODER_ITEM);
        TableFragment table = page.getSimpleRoleDecoderTable();

        crud.delete(simpleRoleDecoderAddress(SIMP_ROLE_DELETE), table, SIMP_ROLE_DELETE);
    }




}
