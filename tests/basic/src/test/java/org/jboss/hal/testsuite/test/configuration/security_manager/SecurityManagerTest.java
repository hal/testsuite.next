package org.jboss.hal.testsuite.test.configuration.security_manager;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SecurityManagerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LIST_ADD_OPERATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.security_manager.SecurityManagerFixtures.*;

@RunWith(Arquillian.class)
public class SecurityManagerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final SnapshotBackup snapshot = new SnapshotBackup();
    private static final ModelNodeGenerator gen = new ModelNodeGenerator();

    @Page private SecurityManagerPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @BeforeClass
    public static void setUpResources() throws IOException {
        snapshot.backup();
        ops.invoke(LIST_ADD_OPERATION, DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, Values.of(NAME, MINIMUM_PERMISSIONS)
                .and(VALUE, gen.createObjectNodeWithPropertyChild(CLASS, MIN_UPDATE_CLASS))).assertSuccess();
        ops.invoke(LIST_ADD_OPERATION, DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, Values.of(NAME, MINIMUM_PERMISSIONS)
                .and(VALUE, gen.createObjectNodeWithPropertyChild(CLASS, MIN_DELETE_CLASS))).assertSuccess();
        ops.invoke(LIST_ADD_OPERATION, DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, Values.of(NAME, MAXIMUM_PERMISSIONS)
                .and(VALUE, gen.createObjectNodeWithPropertyChild(CLASS, MAX_UPDATE_CLASS))).assertSuccess();
        ops.invoke(LIST_ADD_OPERATION, DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, Values.of(NAME, MAXIMUM_PERMISSIONS)
                .and(VALUE, gen.createObjectNodeWithPropertyChild(CLASS, MAX_DELETE_CLASS))).assertSuccess();
    }

    @AfterClass
    public static void cleanUpResources() throws IOException {
        try {
            snapshot.restore();
        } finally {
            client.close();
        }
    }

    @Before
    public void navigate() {
        page.navigate();
    }

    @Test
    public void minPermissionsTryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MIN_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMinPermissionsTable();

        crud.createWithErrorAndCancelDialog(table, ANY_STRING, CLASS);
    }

    @Test
    public void minPermissionCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MIN_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMinPermissionsTable();

        crud.create(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, table,
                form -> form.text(CLASS, MIN_CREATE_CLASS),
                ver -> ver.verifyListAttributeContainsValue(MINIMUM_PERMISSIONS,
                        gen.createObjectNodeWithPropertyChild(CLASS, MIN_CREATE_CLASS)));
    }

    @Test
    public void minPermissionsTryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MIN_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMinPermissionsTable();
        FormFragment form = page.getMinPermissionsForm();
        table.bind(form);
        table.select(MIN_UPDATE_CLASS);

        crud.updateWithError(form, f -> f.clear(CLASS), CLASS);
    }

    @Test
    public void minPermissionsUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MIN_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMinPermissionsTable();
        FormFragment form = page.getMinPermissionsForm();
        table.bind(form);
        table.select(MIN_UPDATE_CLASS);

        crud.update(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, form, f -> {
            f.text(ACTIONS, MIN_UPDATE_2_ACTIONS);
            f.text(CLASS, MIN_UPDATE_2_CLASS);
            f.text(NAME, MIN_UPDATE_2_NAME);
        }, ver -> ver.verifyListAttributeContainsValue(MINIMUM_PERMISSIONS,
                new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ACTIONS, MIN_UPDATE_2_ACTIONS)
                    .addProperty(CLASS, MIN_UPDATE_2_CLASS)
                    .addProperty(NAME, MIN_UPDATE_2_NAME)
                    .build()));
    }

    @Test
    public void minPermissionsDelete() throws Exception {
        console.verticalNavigation().selectPrimary(MIN_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMinPermissionsTable();

        crud.delete(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, table, MIN_DELETE_CLASS,
                ver -> ver.verifyListAttributeDoesNotContainValue(MINIMUM_PERMISSIONS,
                        gen.createObjectNodeWithPropertyChild(CLASS, MIN_DELETE_CLASS)));
    }

    @Test
    public void maxPermissionsTryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MAX_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMaxPermissionsTable();

        crud.createWithErrorAndCancelDialog(table, ANY_STRING, CLASS);
    }

    @Test
    public void maxPermissionCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MAX_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMaxPermissionsTable();

        crud.create(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, table,
                form -> form.text(CLASS, MAX_CREATE_CLASS),
                ver -> ver.verifyListAttributeContainsValue(MAXIMUM_PERMISSIONS,
                        gen.createObjectNodeWithPropertyChild(CLASS, MAX_CREATE_CLASS)));
    }

    @Test
    public void maxPermissionsTryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MAX_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMaxPermissionsTable();
        FormFragment form = page.getMaxPermissionsForm();
        table.bind(form);
        table.select(MAX_UPDATE_CLASS);

        crud.updateWithError(form, f -> f.clear(CLASS), CLASS);
    }

    @Test
    public void maxPermissionsUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MAX_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMaxPermissionsTable();
        FormFragment form = page.getMaxPermissionsForm();
        table.bind(form);
        table.select(MAX_UPDATE_CLASS);

        crud.update(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, form, f -> {
            f.text(ACTIONS, MAX_UPDATE_2_ACTIONS);
            f.text(CLASS, MAX_UPDATE_2_CLASS);
            f.text(NAME, MAX_UPDATE_2_NAME);
        }, ver -> ver.verifyListAttributeContainsValue(MAXIMUM_PERMISSIONS,
                new ModelNodeGenerator.ModelNodePropertiesBuilder()
                    .addProperty(ACTIONS, MAX_UPDATE_2_ACTIONS)
                    .addProperty(CLASS, MAX_UPDATE_2_CLASS)
                    .addProperty(NAME, MAX_UPDATE_2_NAME)
                    .build()));
    }

    @Test
    public void maxPermissionsDelete() throws Exception {
        console.verticalNavigation().selectPrimary(MAX_PERMISSIONS_MENU_ITEM);
        TableFragment table = page.getMaxPermissionsTable();

        crud.delete(DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS, table, MAX_DELETE_CLASS,
                ver -> ver.verifyListAttributeDoesNotContainValue(MAXIMUM_PERMISSIONS,
                        gen.createObjectNodeWithPropertyChild(CLASS, MAX_DELETE_CLASS)));
    }
}
