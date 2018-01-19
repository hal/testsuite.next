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
import java.util.Optional;

import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class wrapping common helper utils for model nodes */
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

    /**
     * Checks whether model node list contains an item member equals to the attribute value
     *
     * @param list      List to be checked
     * @param attribute Attribute name of the inner list
     * @param value     Value to find
     *
     * @return true if value was found, false otherwise
     */
    public static boolean isValuePresentInModelNodeList(ModelNode list, String attribute, String value)
            throws IOException {
        return list.asList().stream()
                .peek(modelNode -> log.debug(
                        "Searching for attribute '{}' whose value is '{}' is contained in the list member '{}'.",
                        attribute, value, modelNode.toString()))
                .filter(modelNode -> modelNode.hasDefined(attribute))
                .anyMatch(modelNode -> modelNode.get(attribute).asString().equals(value));
    }

    /**
     * Checks whether model node list contains an item member equals to the attribute value
     *
     * @param list      List to be checked
     * @param attribute Attribute name of the inner list
     * @param value     Value to find
     *
     * @return true if value was found, false otherwise
     */
    public static boolean isValuePresentInModelNodeList(ModelNode list, String attribute, ModelNode value)
            throws IOException {
        return list.asList().stream()
                .peek(modelNode -> log.debug(
                        "Searching for attribute '{}' whose value is '{}' is contained in the list member '{}'.",
                        attribute, value, modelNode.toString()))
                .filter(modelNode -> modelNode.hasDefined(attribute))
                .anyMatch(modelNode -> modelNode.get(attribute).equals(value));
    }

    /**
     * Verifies that if an attribute value exists for a given LIST attribute contained in a top LIST attribute.
     * As an usage example, see /subsystem=elytron/http-authentication-factory=* resource, there is an attribute
     * "mechanism-configurations" of type LIST, it contains "mechanism-realm-configurations" attribute of type LIST
     * of OBJECT, one of its attributes is "realm-name".
     * Then this method can verify the "realm-name" value.
     *
     * @param list list to be checked
     * @param objectAttribute the object attribute name
     * @param objectValue the object attribute value
     * @param innerListAttribute the inner attribute name of type LIST, contained in listAttribute
     * @param innerObjectAttribute The inner object attribute name.
     * @param innerObjectValue The inner object value
     *
     * @return true if value was found, false otherwise
     */
    public static boolean isValuePresentInModelNodeListOfList(ModelNode list, String objectAttribute,
            String objectValue, String innerListAttribute, String innerObjectAttribute, String innerObjectValue)
            throws IOException {
        String pair = innerObjectAttribute + "=" + innerObjectValue;
        String path = objectAttribute + "=" + objectValue + "/" + innerListAttribute;
        Optional<ModelNode> optional = list.asList().stream()
                .peek(modelNode -> log.debug("Searching for attribute=value '{}' in path '{}' for model {}.", pair,
                        path, modelNode.toString()))
                .filter(modelNode -> modelNode.get(objectAttribute).asString().equals(objectValue))
                .map(modelNode -> modelNode.get(innerListAttribute))
                .findFirst();
        if (optional.isPresent()) {
            ModelNode collect = optional.get();
            return collect.asList().stream()
                    .anyMatch(modelNode -> modelNode.get(innerObjectAttribute).asString().equals(innerObjectValue));
        }
        return false;
    }

    /**
     * Verifies if a object value exists for a given OBJECT attribute contained in a top LIST attribute, filtered by
     * filterAttribute=filterValue, then it gets the objectName and compares the inner object with the value object
     * parameter. An example is /subsystem=elytron/jdbc-realm=*, the principal-query of type LIST, contains an sql
     * attribute and bcrypt-mapper of type OBJECT.
     *
     * @param list list to be checked
     * @param filterAttribute the attribute name to filter
     * @param filterValue the attribute value to filter
     * @param objectName the inner attribute name of type OBJECT, contained in listAttribute
     * @param value The inner object value
     *
     * @return true if value was found, false otherwise
     */
    public static boolean isObjectPresentInModelNodeList(ModelNode list, String filterAttribute,
            String filterValue, String objectName, ModelNode value)
            throws IOException {
        boolean found = false;
        String pair = filterAttribute + "/" + filterValue;
        Optional<ModelNode> optional = list.asList().stream()
                .peek(modelNode -> log.debug("Filtering by '{}' in complex attribute '{}' for model {}.", pair,
                        objectName, modelNode.toString()))
                .filter(m -> m.hasDefined(filterAttribute) && m.get(filterAttribute).asString().equals(filterValue))
                .findFirst();
        if (optional.isPresent()) {
            ModelNode modelFound = optional.get().get(objectName);
            found = value.equals(modelFound);
        }
        return found;
    }

    /**
     * Verifies if an attribute is undefined in a top LIST attribute, filtered by filterAttribute=filterValue, then it
     * checks if the objectName attribute is defined. An example is /subsystem=elytron/jdbc-realm=*, the
     * principal-query of type LIST, contains an sql attribute and bcrypt-mapper of type OBJECT.
     *
     * @param list list to be checked
     * @param filterAttribute the attribute name to filter
     * @param filterValue the attribute value to filter
     * @param objectName the inner attribute name of type OBJECT, contained in listAttribute
     *
     * @return true if undefined
     */
    public static boolean isObjectUndefinedInModelNodeList(ModelNode list, String filterAttribute,
            String filterValue, String objectName)
            throws IOException {
        boolean isUndefined = false;
        Optional<ModelNode> optional = list.asList().stream()
                .filter(m -> m.hasDefined(filterAttribute) && m.get(filterAttribute).asString().equals(filterValue))
                .findFirst();
        if (optional.isPresent()) {
            isUndefined = !optional.get().hasDefined(objectName);
        }
        return isUndefined;
    }

    private ModelNodeUtils() {
    }
}
