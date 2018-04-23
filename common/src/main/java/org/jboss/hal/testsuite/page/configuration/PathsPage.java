package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.PATH)
public class PathsPage extends BasePage {

    @FindBy(id = "path-table_wrapper")
    private TableFragment pathsTable;

    @FindBy(id = "path-form")
    private FormFragment pathsForm;

    public TableFragment getPathsTable() {
        return pathsTable;
    }

    public FormFragment getPathsForm() {
        return pathsForm;
    }
}
