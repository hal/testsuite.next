package org.jboss.hal.testsuite.page.runtime.elytron;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.PagesFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_RUNTIME_STORES)
public class ElytronRuntimeStoresPage extends BasePage {

    @FindBy(id = KEY_STORE + "-" + TABLE + WRAPPER)
    private TableFragment keyStoreTable;

    @FindBy(id = KEY_STORE + "-" + ALIAS + "-" + TABLE + WRAPPER)
    private TableFragment keyStoreAliasTable;

    @FindBy(id = KEY_STORE + "-" + Ids.PAGES)
    private PagesFragment aliasesPage;

    public TableFragment getKeyStoreTable() {
        return keyStoreTable;
    }

    public TableFragment getKeyStoreAliasTable() {
        return keyStoreAliasTable;
    }
}
