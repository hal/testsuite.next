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
package org.jboss.hal.testsuite.test.configuration.management;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jboss.dmr.ModelNode;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONSTANT_HEADERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HEADERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.fixtures.HttpManagementInterfaceFixtures.HTTP_INTERFACE_ADDRESS;

final class ConstantHeadersChecks {

    static boolean verifyConstantHeader(Operations operations, String path) throws IOException {
        return verifyConstantHeader(operations, path, null, null);
    }

    static boolean verifyConstantHeader(Operations operations, String path,
            String headerName, String headerValue) throws IOException {
        ModelNodeResult result = operations.readAttribute(HTTP_INTERFACE_ADDRESS, CONSTANT_HEADERS);
        if (result.isSuccess()) {
            List<ModelNode> constantHeaders = result.value().asList();
            Optional<ModelNode> optionalPath = constantHeaders.stream()
                    .filter(node -> path.equals(node.get(PATH).asString()))
                    .findFirst();
            if (optionalPath.isPresent()) {
                if (headerName != null && headerValue != null) {
                    List<ModelNode> headers = optionalPath.get().get(HEADERS).asList();
                    return headers.stream().anyMatch(node ->
                            headerName.equals(node.get(NAME).asString()) &&
                                    headerValue.equals(node.get(VALUE).asString()));
                }
                return true;
            }
        }
        return false;
    }

    static boolean verifyConstantHeaderDeleted(Operations operations, String path) throws IOException {
        ModelNodeResult result = operations.readAttribute(HTTP_INTERFACE_ADDRESS, CONSTANT_HEADERS);
        if (result.isSuccess()) {
            if (result.value().isDefined()) {
                List<ModelNode> constantsHeaders = result.value().asList();
                boolean pathFound = constantsHeaders.stream().anyMatch(node ->
                        path.equals(node.get(PATH).asString()));
                return !pathFound;
            } else {
                // undefined means no path found!
                return true;
            }
        }
        return false;
    }

    static boolean verifyConstantHeaderNameValueDeleted(Operations operations, String path,
            String headerName, String headerValue) throws IOException {
        ModelNodeResult result = operations.readAttribute(HTTP_INTERFACE_ADDRESS, CONSTANT_HEADERS);
        if (result.isSuccess()) {
            List<ModelNode> constantHeaders = result.value().asList();
            Optional<ModelNode> optionalPath = constantHeaders.stream()
                    .filter(node -> path.equals(node.get(PATH).asString()))
                    .findFirst();
            if (optionalPath.isPresent()) {
                List<ModelNode> headers = optionalPath.get().get(HEADERS).asList();
                boolean nameValueFound = headers.stream().anyMatch(node ->
                        headerName.equals(node.get(NAME).asString()) &&
                                headerValue.equals(node.get(VALUE).asString()));
                return !nameValueFound;
            }
        }
        return false;
    }

    private ConstantHeadersChecks() {
    }
}
