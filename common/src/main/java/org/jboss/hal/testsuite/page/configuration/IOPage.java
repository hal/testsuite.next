/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

@Place(NameTokens.IO)
public class IOPage extends BasePage {

    @FindBy(id = "io-worker-table_wrapper") private TableFragment workerTable;
    @FindBy(id = "io-worker-form") private FormFragment workerForm;

    @FindBy(id = "io-buffer-pool-table_wrapper") private TableFragment bufferPoolTable;
    @FindBy(id = "io-buffer-pool-form") private FormFragment bufferPoolForm;

    public TableFragment getWorkerTable() {
        return workerTable;
    }

    public FormFragment getWorkerForm() {
        return workerForm;
    }

    public TableFragment getBufferPoolTable() {
        return bufferPoolTable;
    }

    public FormFragment getBufferPoolForm() {
        return bufferPoolForm;
    }
}
