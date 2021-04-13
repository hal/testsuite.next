package org.jboss.hal.testsuite.page.runtime;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.UNDERTOW_RUNTIME_DEPLOYMENT_VIEW)
public class UndertowRuntimePage extends BasePage {

    @FindBy(id = "undertow-deployment-session_wrapper")
    private TableFragment sessionsTable;

    public TableFragment getSessionsTable() {
        return sessionsTable;
    }
}
