package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place("undertow-buffer-cache")
public class UndertowBufferCachePage extends BasePage {

    @FindBy(id = "buffer-cache-table_wrapper")
    private TableFragment bufferCacheTable;

    @FindBy(id = "buffer-cache-form")
    private FormFragment bufferCacheForm;

    public TableFragment getBufferCacheTable() {
        return bufferCacheTable;
    }

    public FormFragment getBufferCacheForm() {
        return bufferCacheForm;
    }

}
