package org.jboss.hal.testsuite.page.runtime;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.MANAGEMENT_OPERATIONS)
public class ManagementOperationsPage extends BasePage {

    @FindBy(id = "toolbar-actions-active-operation-refresh")
    private WebElement reloadButton;

    @FindBy(id = "toolbar-actions-active-operation-cancel-non-progressing-operation")
    private WebElement cancelNonProgressingOperationButton;

    public boolean isVisibleActiveOperation(long operationId) {
        return !getRootContainer().findElements(By.id(getActiveOperationId(operationId))).isEmpty();
    }

    public ManagementOperationsPage cancelActiveOperation(long operationId) {
        String
            activeOperationId = getActiveOperationId(operationId),
            cancelButtonId = Ids.build(activeOperationId, activeOperationId, "cancel");
        By cancelButtonSelector = By.id(cancelButtonId);
        WebElement cancelButton = getRootContainer().findElement(cancelButtonSelector);
        cancelButton.click();
        console.confirmationDialog().confirm();
        Graphene.waitGui().until().element(By.id(activeOperationId)).text().contains("Cancelled: true");

        return this;
    }

    public ManagementOperationsPage reloadManagementOperationsList() {
        reloadButton.click();
        return this;
    }

    public ManagementOperationsPage cancelNonProgressingOperation() {
        cancelNonProgressingOperationButton.click();
        return this;
    }

    private String getActiveOperationId(long operationId) {
        return Ids.build(Ids.ACTIVE_OPERATION, String.valueOf(Math.abs(operationId)));
    }
}
