package org.jboss.hal.testsuite.fragment.finder.preview;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.hal.testsuite.fragment.finder.FinderPreviewFragment;
import org.openqa.selenium.WebElement;

public class MicroProfileHealthPreviewFragment extends FinderPreviewFragment {

    @FindByJQuery("a.clickable > span.fa-refresh")
    private WebElement refreshButton;

    public String getCheckState(String checkName) {
        return getAttributeElementMap(checkName).get("Status").getText();
    }

    public String getCheckData(String checkName) {
        return getAttributeElementMap(checkName).get("Data").getText();
    }

    public void refresh() {
        refreshButton.click();
    }

}
