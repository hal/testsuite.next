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
package org.jboss.hal.testsuite.fixtures;

import java.util.HashMap;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.HEADERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_INTERFACE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MANAGEMENT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MANAGEMENT_INTERFACE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.resources.Ids.HEADER;

public final class HttpManagementInterfaceFixtures {

    private static final String CONSTANT_HEADERS_PREFIX = "chp";
    public static final Address HTTP_INTERFACE_ADDRESS = Address.coreService(MANAGEMENT).and(MANAGEMENT_INTERFACE, HTTP_INTERFACE);

    // ------------------------------------------------------ constants headers path

    public static final String CONSTANT_HEADERS_PATH_CREATE = Ids.build(CONSTANT_HEADERS_PREFIX,
            CrudConstants.CREATE, Random.name());
    public static final String CONSTANT_HEADERS_PATH_READ = Ids.build(CONSTANT_HEADERS_PREFIX,
            CrudConstants.READ, Random.name());
    public static final String CONSTANT_HEADERS_PATH_UPDATE = Ids.build(CONSTANT_HEADERS_PREFIX,
            CrudConstants.UPDATE, Random.name());
    public static final String CONSTANT_HEADERS_PATH_DELETE = Ids.build(CONSTANT_HEADERS_PREFIX,
            CrudConstants.DELETE, Random.name());

    public static ModelNode constantHeaders(String path, String headerName, String headerValue) {
        HashMap<String, String> headerNameValues = new HashMap<>();
        headerNameValues.put(headerName, headerValue);
        return constantHeaders(path, headerNameValues);
    }

    public static ModelNode constantHeaders(String path, Map<String, String> headerNameValues) {
        ModelNode headers = new ModelNode();
        headerNameValues.forEach((name, value) -> {
            ModelNode nameValue = new ModelNode();
            nameValue.get(NAME).set(name);
            nameValue.get(VALUE).set(value);
            headers.add(nameValue);
        });

        ModelNode constantHeaders = new ModelNode();
        constantHeaders.get(PATH).set(path);
        constantHeaders.get(HEADERS).set(headers);
        return constantHeaders;
    }

    // ------------------------------------------------------ constants headers header

    public static final String HEADER_NAME_CREATE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, NAME,
            CrudConstants.CREATE, Random.name());
    public static final String HEADER_VALUE_CREATE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, VALUE,
            CrudConstants.CREATE, Random.name());
    public static final String HEADER_NAME_READ = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, NAME,
            CrudConstants.READ, Random.name());
    public static final String HEADER_VALUE_READ = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, VALUE,
            CrudConstants.READ, Random.name());
    public static final String HEADER_NAME_UPDATE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, NAME,
            CrudConstants.UPDATE, Random.name());
    public static final String HEADER_VALUE_UPDATE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, VALUE,
            CrudConstants.UPDATE, Random.name());
    public static final String HEADER_NAME_DELETE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, NAME,
            CrudConstants.DELETE, Random.name());
    public static final String HEADER_VALUE_DELETE = Ids.build(CONSTANT_HEADERS_PREFIX, HEADER, VALUE,
            CrudConstants.DELETE, Random.name());

    private HttpManagementInterfaceFixtures() {
    }
}
