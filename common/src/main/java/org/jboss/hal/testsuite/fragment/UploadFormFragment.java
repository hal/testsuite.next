package org.jboss.hal.testsuite.fragment;

import java.io.File;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Ids;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class UploadFormFragment {

    @Root private WebElement root;
    @FindBy(css = "ul[class='upload-file-list']") private WebElement uploadedFiles;
    @FindBy(id = Ids.UPLOAD_FILE_INPUT) private WebElement fileInput;

    public void uploadFile(File fileToUpload) {
        fileInput.sendKeys(fileToUpload.getAbsolutePath());
        Graphene.waitGui().until().element(uploadedFiles).text().equalTo(fileToUpload.getName());
    }

    public static UploadFormFragment getUploadForm(WebElement serchContext) {
        WebElement formElement = serchContext.findElement(By.cssSelector("form." + CSS.upload));
        waitGui().until().element(formElement).is().visible();
        return Graphene.createPageFragment(UploadFormFragment.class, formElement);
    }

}
