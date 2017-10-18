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
package org.jboss.hal.testsuite.dmr;

import java.io.IOException;

import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class wrapping common helper utils for model nodes
 */
public class ModelNodeUtils {

    private static final Logger log = LoggerFactory.getLogger(ModelNodeUtils.class);

    /**
     * Checks whether model node list contains given value
     *
     * @param list  List to be checked
     * @param value Value to find
     *
     * @return true if value was found, false otherwise
     */
    public static boolean isValuePresentInModelNodeList(ModelNode list, ModelNode value) throws IOException {
        return list.asList().stream()
                .peek(modelNode -> log.debug("Comparing '{}' with list member '{}'.", value.toString(),
                        modelNode.toString()))
                .anyMatch(modelNode -> modelNode.equals(value));
    }

    private ModelNodeUtils() {
    }
}
