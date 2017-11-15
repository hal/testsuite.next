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
package org.jboss.hal.testsuite.creaper;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESULT;

/** Reads the resource description for a given address and offers methods to return information from it. */
class ResourceDescription {

    private static final Logger log = LoggerFactory.getLogger(ResourceDescription.class);

    private Address address;
    private ModelNode resourceDescription;

    /** Reads the resource descriptions of the specified resource. */
    ResourceDescription(Address address, OnlineManagementClient client) {
        this.address = address;
        Operations operations = new Operations(client);
        try {
            ModelNodeResult result = operations.invoke("read-resource-description", address);
            this.resourceDescription = result.get(RESULT);
        } catch (IOException e) {
            log.error("Cannot read resource description for {}: {}", address, e.getMessage(), e);
            this.resourceDescription = new ModelNode();
        }
    }

    List<Property> getAttributes() {
        return resourceDescription.get(ATTRIBUTES).asPropertyList();
    }

    ModelNode getAttribute(String attribute) throws NoSuchElementException {
        if (!resourceDescription.hasDefined(ATTRIBUTES)) {
            throw new NoSuchElementException(String.format("No attributes found in resource description of %s",
                    address));
        }

        ModelNode attributes = resourceDescription.get(ATTRIBUTES);
        if (!attributes.hasDefined(attribute)) {
            throw new NoSuchElementException(String.format("Attribute %s not found in resource description of %s",
                    attribute, address));
        }
        return attributes.get(attribute);
    }

    /**
     * Returns the default value of the specified attribute.
     *
     * <p>Throws a {@link java.util.NoSuchElementException} if the attribute is not found or doesn't have a default
     * value.</p>
     */
    ModelNode defaultValue(String attribute) throws NoSuchElementException {
        ModelNode attributeNode = getAttribute(attribute);
        if (!attributeNode.hasDefined(DEFAULT)) {
            throw new NoSuchElementException(String.format("No default value found for attribute %s in %s",
                    attribute, address));
        }
        return attributeNode.get(DEFAULT);
    }
}
