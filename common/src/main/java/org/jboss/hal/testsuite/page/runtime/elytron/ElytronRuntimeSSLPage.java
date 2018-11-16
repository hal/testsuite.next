package org.jboss.hal.testsuite.page.runtime.elytron;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT;
import static org.jboss.hal.resources.Ids.FORM;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_RUNTIME_SSL)
public class ElytronRuntimeSSLPage extends BasePage {

    @FindBy(id = CERTIFICATE_AUTHORITY_ACCOUNT + "-" + TABLE + WRAPPER)
    private TableFragment certificateAuthorityAccountTable;

    @FindBy(id = CERTIFICATE_AUTHORITY_ACCOUNT + "-" + FORM)
    private FormFragment certificateAuthorityAccountForm;

    public FormFragment getCertificateAuthorityAccountForm() {
        return certificateAuthorityAccountForm;
    }

    public TableFragment getCertificateAuthorityAccountTable() {
        return certificateAuthorityAccountTable;
    }
}
