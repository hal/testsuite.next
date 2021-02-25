package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_MANAGER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.SECURITY_MANAGER_MAXIMUM_PERMISSIONS;
import static org.jboss.hal.resources.Ids.SECURITY_MANAGER_MINIMUM_PERMISSIONS;

public final class SecurityManagerFixtures {

    private static final String
        MIN_PERM_PREFIX = "min",
        MAX_PERM_PREFIX = "max",
        TWO = "2";

    public static final String
        ACTIONS = "actions",
        ANY_STRING = Random.name(),
        MAXIMUM_PERMISSIONS = "maximum-permissions",
        MAX_PERMISSIONS_MENU_ITEM = SECURITY_MANAGER_MAXIMUM_PERMISSIONS + "-" + ITEM,
        MINIMUM_PERMISSIONS = "minimum-permissions",
        MIN_PERMISSIONS_MENU_ITEM = SECURITY_MANAGER_MINIMUM_PERMISSIONS + "-" + ITEM;

    public static final Address DEFAULT_DEPLOYMENT_PERMISSIONS_ADDRESS =
            Address.subsystem(SECURITY_MANAGER).and("org.jboss.hal.testsuite.test.deployment-permissions", "default");

    // -------------- minimum permissions

    public static final String MIN_CREATE_CLASS = Ids.build(MIN_PERM_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String MIN_UPDATE_CLASS = Ids.build(MIN_PERM_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String MIN_UPDATE_2_CLASS = Ids.build(MIN_PERM_PREFIX, TWO, CLASS, CrudConstants.UPDATE, Random.name());
    public static final String MIN_UPDATE_2_ACTIONS = Ids.build(MIN_PERM_PREFIX, TWO, ACTIONS, CrudConstants.UPDATE, Random.name());
    public static final String MIN_UPDATE_2_NAME = Ids.build(MIN_PERM_PREFIX, TWO, NAME, CrudConstants.UPDATE, Random.name());
    public static final String MIN_DELETE_CLASS = Ids.build(MIN_PERM_PREFIX, CrudConstants.DELETE, Random.name());

    // -------------- maximum permissions

    public static final String MAX_CREATE_CLASS = Ids.build(MAX_PERM_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String MAX_UPDATE_CLASS = Ids.build(MAX_PERM_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String MAX_UPDATE_2_CLASS = Ids.build(MAX_PERM_PREFIX, TWO, CLASS, CrudConstants.UPDATE, Random.name());
    public static final String MAX_UPDATE_2_ACTIONS = Ids.build(MAX_PERM_PREFIX, TWO, ACTIONS, CrudConstants.UPDATE, Random.name());
    public static final String MAX_UPDATE_2_NAME = Ids.build(MAX_PERM_PREFIX, TWO, NAME, CrudConstants.UPDATE, Random.name());
    public static final String MAX_DELETE_CLASS = Ids.build(MAX_PERM_PREFIX, CrudConstants.DELETE, Random.name());

    private SecurityManagerFixtures() { }
}
