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
package org.jboss.hal.testsuite.page;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;

public class Places {

    private static final String PATH_PARAM = "path";

    public static PlaceRequest finderPlace(String token, FinderPath path) {
        return new PlaceRequest.Builder().nameToken(token).with(PATH_PARAM, path.toString()).build();
    }

    private Places() {
    }
}
