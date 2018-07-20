package org.jboss.hal.testsuite.page.runtime.ejb;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.EJB3_RUNTIME)
public class EJBPage extends BasePage {

    @FindBy(id = Ids.EJB3_DEPLOYMENT + "-singleton-form")
    private FormFragment singletonEJBForm;

    @FindBy(id = Ids.EJB3_DEPLOYMENT + "-stateless-form")
    private FormFragment statelessEJBForm;

    @FindBy(id = Ids.EJB3_DEPLOYMENT + "-stateful-form")
    private FormFragment statefulEJBForm;

    @FindBy(id = Ids.EJB3_DEPLOYMENT + "-mdb-form")
    private FormFragment messageDrivenBeanEJBForm;

    public FormFragment getSingletonEJBForm() {
        return singletonEJBForm;
    }

    public FormFragment getStatelessEJBForm() {
        return statelessEJBForm;
    }

    public FormFragment getStatefulEJBForm() {
        return statefulEJBForm;
    }

    public FormFragment getMessageDrivenBeanEJBForm() {
        return messageDrivenBeanEJBForm;
    }
}
