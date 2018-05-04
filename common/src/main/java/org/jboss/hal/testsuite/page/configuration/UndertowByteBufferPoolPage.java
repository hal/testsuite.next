package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place("undertow-byte-buffer-pool")
public class UndertowByteBufferPoolPage extends BasePage {

    @FindBy(id = "byte-buffer-pool-table_wrapper")
    private TableFragment byteBufferPoolTable;

    @FindBy(id = "byte-buffer-pool-form")
    private FormFragment byteBufferPoolForm;

    public TableFragment getByteBufferPoolTable() {
        return byteBufferPoolTable;
    }

    public FormFragment getByteBufferPoolForm() {
        return byteBufferPoolForm;
    }

}
