package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.SECURITY_MANAGER_MAXIMUM_PERMISSIONS;
import static org.jboss.hal.resources.Ids.SECURITY_MANAGER_MINIMUM_PERMISSIONS;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

/**
 * Abstraction for Security Manager subsystem configuration page
 */
@Place(NameTokens.SECURITY_MANAGER)
public class SecurityManagerPage extends BasePage {

    @FindBy(id = SECURITY_MANAGER_MINIMUM_PERMISSIONS + "-" + TABLE +  WRAPPER) private TableFragment minPermissionsTable;
    @FindBy(id = SECURITY_MANAGER_MINIMUM_PERMISSIONS + "-" + FORM) private FormFragment minPermissionsForm;
    @FindBy(id = SECURITY_MANAGER_MAXIMUM_PERMISSIONS + "-" + TABLE +  WRAPPER) private TableFragment maxPermissionsTable;
    @FindBy(id = SECURITY_MANAGER_MAXIMUM_PERMISSIONS + "-" + FORM) private FormFragment maxPermissionsForm;

    public TableFragment getMinPermissionsTable() {
        return minPermissionsTable;
    }

    public FormFragment getMinPermissionsForm() {
        return minPermissionsForm;
    }

    public TableFragment getMaxPermissionsTable() {
        return maxPermissionsTable;
    }

    public FormFragment getMaxPermissionsForm() {
        return maxPermissionsForm;
    }
}
