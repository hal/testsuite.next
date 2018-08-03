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

import com.google.common.base.Splitter;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.hal.testsuite.dmr.ModelNodeUtils;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static java.util.stream.Collectors.toList;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Helper class to verify resource existence and attribute values in model.
 * Created by pjelinek on Nov 16, 2015
 */
public class ResourceVerifier {

    private static final String EQ = "=";
    private static final String END_STR = "' !";
    private static final Logger log = LoggerFactory.getLogger(ResourceVerifier.class);
    private static final int DEFAULT_TIMEOUT = Integer.parseInt(ConfigUtils.get("propagate.to.model.timeout", "500"));

    private Address address;
    private OnlineManagementClient client;
    private int timeout;
    private Operations ops;
    private ResourceDescription resourceDescription;

    public ResourceVerifier(Address address, OnlineManagementClient client) {
        this(address, client, DEFAULT_TIMEOUT);
    }

    /**
     * @param timeout - how long to wait for GUI change to be propagated to model in milliseconds
     */
    public ResourceVerifier(Address address, OnlineManagementClient client, int timeout) {
        this.address = address;
        this.client = client;
        this.timeout = timeout;
        this.ops = new Operations(client);
    }


    // ------------------------------------------------------ exists / not exists

    /**
     * Verifies resource exists in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyExists(String errorMessageSuffix) throws Exception {
        waitFor(() -> ops.exists(address));

        assertTrue("Resource '" + address + "' should exist!" + errorMessageSuffix,
                ops.exists(address));
        return this;
    }

    /** Verifies resource exists in model. */
    public ResourceVerifier verifyExists() throws Exception {
        return verifyExists("");
    }

    /**
     * Verifies resource doesn't exist in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyDoesNotExist(String errorMessageSuffix) throws Exception {
        waitFor(() -> !ops.exists(address));

        assertFalse("Resource '" + address + "' should NOT exist! " + errorMessageSuffix,
                ops.exists(address));
        return this;
    }

    /** Verifies resource doesn't exist in model. */
    public ResourceVerifier verifyDoesNotExist() throws Exception {
        return verifyDoesNotExist("");
    }


    // ------------------------------------------------------ attributes

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, ModelNode expectedValue,
            String errorMessageSuffix) throws Exception {
        boolean nested = attributeName.contains(".");
        String baseAttributeName = nested
                ? attributeName.substring(0, attributeName.indexOf('.'))
                : attributeName;
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, baseAttributeName);
            return actualResult.isSuccess() && actualResult.hasDefinedValue()
                    && expectedValue.equals(actualResult.value());
        });

        ModelNodeResult actualResult = ops.readAttribute(address, baseAttributeName);
        actualResult.assertDefinedValue(errorMessageSuffix);
        ModelNode actualNode = actualResult.value();

        if (nested) {
            String path = attributeName.substring(attributeName.indexOf('.') + 1);
            for (String name : Splitter.on('.').omitEmptyStrings().split(path)) {
                assertTrue(actualNode.hasDefined(name));
                actualNode = actualNode.get(name);
            }
        }
        assertEquals("Attribute value is different in model! " + errorMessageSuffix,
                expectedValue, actualNode);
        return this;
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, ModelNode expectedValue) throws Exception {
        return verifyAttribute(attributeName, expectedValue, "");
    }

    /**
     * Verifies the value of attribute in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyAttribute(String attributeName, String expectedValue, String errorMessageSuffix)
            throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue), errorMessageSuffix);
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, String expectedValue) throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue));
    }

    /**
     * Verifies the value of attribute in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyAttribute(String attributeName, boolean expectedValue, String errorMessageSuffix)
            throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue), errorMessageSuffix);
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, boolean expectedValue) throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue));
    }

    /**
     * Verifies the value of attribute in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyAttribute(String attributeName, int expectedValue, String errorMessageSuffix)
            throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue), errorMessageSuffix);
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, int expectedValue) throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue));
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, double expectedValue) throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue));
    }

    /**
     * Verifies the value of attribute in model.
     *
     * @param errorMessageSuffix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyAttribute(String attributeName, long expectedValue, String errorMessageSuffix)
            throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue), errorMessageSuffix);
    }

    /** Verifies the value of attribute in model. */
    public ResourceVerifier verifyAttribute(String attributeName, long expectedValue) throws Exception {
        return verifyAttribute(attributeName, new ModelNode(expectedValue));
    }

    public ResourceVerifier verifyAttributeNotEqual(String attributeName, ModelNode notExpectedValue)
            throws Exception {
        waitFor(() -> !attributeEquals(attributeName, notExpectedValue));

        assertFalse(attributeName + " should not have value " + notExpectedValue,
                attributeEquals(attributeName, notExpectedValue));
        return this;
    }

    /**
     * Verifies the value of attribute in model is undefined.
     *
     * @param errorMessagePrefix is intended to be used for e.g. passing related tracked issue.
     */
    public ResourceVerifier verifyAttributeIsUndefined(String attributeName, String errorMessagePrefix)
            throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, attributeName);
            return actualResult.isSuccess() && !actualResult.hasDefined(RESULT);
        });

        ops.readAttribute(address, attributeName).assertNotDefinedValue(errorMessagePrefix);
        return this;
    }

    /** Verifies the value of attribute in model is undefined. */
    public ResourceVerifier verifyAttributeIsUndefined(String attributeName) throws Exception {
        return verifyAttributeIsUndefined(attributeName, null);
    }

    private boolean attributeEquals(String attributeName, ModelNode expectedValue) throws IOException {
        ModelNodeResult actualResult = ops.readAttribute(address, attributeName);
        return actualResult.isSuccess() && actualResult.hasDefinedValue() && actualResult.value().equals(expectedValue);
    }


    // ------------------------------------------------------ lists

    /**
     * Verifies that list type attribute contains give value.
     *
     * @param value Value which should be present in the list.
     */
    public ResourceVerifier verifyListAttributeContainsValue(String attributeName, ModelNode value,
            String errorMessageSuffix) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, attributeName);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    isModelNodePresentInListAttributeValue(attributeName, value);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, attributeName);
        modelNodeResult.assertSuccess();

        assertTrue("Given value '" + value.toString() + "' is not present in list attribute '" + attributeName
                        + "' with value '" + modelNodeResult.value() + " '!" +
                        (errorMessageSuffix == null || errorMessageSuffix.isEmpty() ? "" : " " + errorMessageSuffix),
                isModelNodePresentInListAttributeValue(attributeName, value));
        return this;
    }

    /**
     * Verifies that list type attribute contains give value for a specific attribute.
     *
     * @param listAttributeName the attribute name of type LIST.
     * @param innerAttribute The inner attribute name.
     * @param value The value of the inner attribute.
     */
    public ResourceVerifier verifyListAttributeContainsSingleValue(String listAttributeName, String innerAttribute,
            String value) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttributeName);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttributeName);
        modelNodeResult.assertSuccess();

        String attrPair = innerAttribute + "/" + value;
        assertTrue(
                "Given attribute/value pair '" + attrPair + "' is not present in list attribute '" + listAttributeName
                        + "' with attribute value '" + modelNodeResult.value() + END_STR,
                isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value));
        return this;
    }

    /**
     * Verifies that list type attribute contains give value for a specific attribute.
     *
     * @param listAttributeName the attribute name of type LIST.
     * @param innerAttribute The inner attribute name.
     * @param value The value of the inner attribute.
     */
    public ResourceVerifier verifyListAttributeContainsSingleValue(String listAttributeName, String innerAttribute,
            ModelNode value) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttributeName);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttributeName);
        modelNodeResult.assertSuccess();

        String attrPair = innerAttribute + "/" + value;
        assertTrue(
                "Given attribute/value pair '" + attrPair + "' is not present in list attribute '" + listAttributeName
                        + "' with attribute value '" + modelNodeResult.value() + END_STR,
                isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value));
        return this;
    }

    /**
     * Verifies that if an attribute value exists for a given LIST attribute contained in a top LIST attribute.
     * As an usage example, see /subsystem=elytron/http-authentication-factory=* resource, there is an attribute
     * "mechanism-configurations" of type LIST, it contains "mechanism-realm-configurations" attribute of type LIST
     * of OBJECT, one of its attributes is "realm-name".
     * Then this method can verify the "realm-name" value.
     *
     * @param listAttribute the attribute name of type LIST.
     * @param objectAttribute the object attribute name
     * @param objectValue the object attribute value
     * @param innerListAttribute the inner attribute name of type LIST, contained in listAttribute
     * @param innerObjectAttribute The inner object attribute name.
     * @param innerObjectValue The inner object value
     */
    public ResourceVerifier verifyListAttributeContainsSingleValueOfList(String listAttribute, String objectAttribute, String objectValue,
            String innerListAttribute, String innerObjectAttribute, String innerObjectValue) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttribute);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    isSingleValuePresentInInnerListAttributeValue(listAttribute, objectAttribute, objectValue,
                            innerListAttribute, innerObjectAttribute, innerObjectValue);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttribute);
        modelNodeResult.assertSuccess();

        String pair = innerObjectAttribute + EQ + innerObjectValue;
        String path = listAttribute + "/" + objectAttribute + EQ + objectValue + "/" + innerListAttribute;
        String msg = "The attribute=value pair '" + pair + "' is not present in path: '" + path + " with attribute value '" + modelNodeResult
                .value() + END_STR;
        assertTrue(msg, isSingleValuePresentInInnerListAttributeValue(listAttribute, objectAttribute, objectValue,
                innerListAttribute, innerObjectAttribute, innerObjectValue));
        return this;
    }

    /**
     * Verifies that if an attribute value exists for a given LIST attribute contained in a top LIST attribute.
     * As an usage example, see /subsystem=elytron/http-authentication-factory=* resource, there is an attribute
     * "mechanism-configurations" of type LIST, it contains "mechanism-realm-configurations" attribute of type LIST
     * of OBJECT, one of its attributes is "realm-name".
     * Then this method can verify the "realm-name" value.
     *
     * @param listAttribute the attribute name of type LIST.
     * @param filterAttribute the object attribute name
     * @param filterValue the object attribute value
     * @param singletonAttribute the inner attribute name of type LIST, contained in listAttribute
     * @param objectValue The inner object value
     */
    public ResourceVerifier verifyListAttributeContainsObjectValue(String listAttribute, String filterAttribute, String filterValue,
            String singletonAttribute, ModelNode objectValue) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttribute);
            return actualResult.isSuccess() &&
                    actualResult.hasDefinedValue() &&
                    ModelNodeUtils.isObjectPresentInModelNodeList(actualResult.get(RESULT), filterAttribute,
                            filterValue, singletonAttribute, objectValue);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttribute);
        modelNodeResult.assertSuccess();

        String path = address + "." + listAttribute + "={" + filterAttribute + "=" + filterValue + "}";
        String errorMessage = "For path: " + path + ". The expected model in " + singletonAttribute + " is " + objectValue + ", but was not found.";
        boolean found = ModelNodeUtils.isObjectPresentInModelNodeList(modelNodeResult.get(RESULT), filterAttribute,
                filterValue, singletonAttribute, objectValue);
        assertTrue(errorMessage, found);
        return this;
    }

    /**
     * Verifies that list type attribute contains give value.
     *
     * @param value Value which should be present in the list.
     */
    public ResourceVerifier verifyListAttributeContainsValue(String listAttributeName, ModelNode value) throws Exception {
        return verifyListAttributeContainsValue(listAttributeName, value, null);
    }

    /**
     * Verifies that list type attribute contains give value.
     *
     * @param value Value which should be present in the list.
     */
    public ResourceVerifier verifyListAttributeContainsValue(String attributeName, String value) throws Exception {
        return verifyListAttributeContainsValue(attributeName, new ModelNode(value));
    }

    /**
     * Verifies that list type attribute contains give value.
     *
     * @param value Value which should be present in the list.
     */
    public ResourceVerifier verifyListAttributeDoesNotContainValue(String attributeName, ModelNode value,
            String errorMessageSuffix)
            throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, attributeName);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    !isModelNodePresentInListAttributeValue(attributeName, value);
        });

        final ModelNodeResult modelNodeResult = ops.readAttribute(address, attributeName);
        modelNodeResult.assertSuccess();

        assertFalse("Given value '" + value.toString() + "' should not be present in list attribute '" +
                        attributeName + " '!" +
                        (errorMessageSuffix == null || errorMessageSuffix.isEmpty() ? "" : " " + errorMessageSuffix),
                isModelNodePresentInListAttributeValue(attributeName, value));
        return this;
    }

    /**
     * Verifies that list type attribute does not contain a given value for a specific attribute.
     *
     * @param listAttributeName the attribute name of type LIST.
     * @param innerAttribute The inner attribute name.
     * @param value The value of the inner attribute.
     */
    public ResourceVerifier verifyListAttributeDoesNotContainSingleValue(String listAttributeName,
            String innerAttribute, String value) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttributeName);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    !isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttributeName);
        modelNodeResult.assertSuccess();

        String attrPair = innerAttribute + EQ + value;
        assertFalse("Given attribute=value pair '" + attrPair + "' is present in list attribute '" + listAttributeName
                        + "' with attribute value '" + modelNodeResult.value() + " '!",
                isSingleValuePresentInListAttributeValue(listAttributeName, innerAttribute, value));
        return this;
    }

    /**
     * Verifies that if an attribute value exists for a given LIST attribute contained in a top LIST attribute.
     * As an usage example, see /subsystem=elytron/http-authentication-factory=* resource, there is an attribute
     * "mechanism-configurations" of type LIST, it contains "mechanism-realm-configurations" attribute of type LIST
     * of OBJECT, one of its attributes is "realm-name".
     * Then this method can verify the "realm-name" value.
     *
     * @param listAttribute the attribute name of type LIST.
     * @param objectAttribute the object attribute name
     * @param objectValue the object attribute value
     * @param innerListAttribute the inner attribute name of type LIST, contained in listAttribute
     * @param innerObjectAttribute The inner object attribute name.
     * @param innerObjectValue The inner object value
     */
    public ResourceVerifier verifyListAttributeDoesNotContainsSingleValueOfList(String listAttribute, String objectAttribute, String objectValue,
            String innerListAttribute, String innerObjectAttribute, String innerObjectValue) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttribute);
            return actualResult.isSuccess() &&
                    actualResult.hasDefined(RESULT) &&
                    isSingleValuePresentInInnerListAttributeValue(listAttribute, objectAttribute, objectValue,
                            innerListAttribute, innerObjectAttribute, innerObjectValue);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttribute);
        modelNodeResult.assertSuccess();

        String pair = innerObjectAttribute + EQ + innerObjectValue;
        String path = listAttribute + "/" + objectAttribute + EQ + objectValue + "/" + innerListAttribute;
        String msg = "The attribute=value pair '" + pair + "' is not present in path: '" + path + " with attribute value '" + modelNodeResult
                .value() + END_STR;
        assertFalse(msg, isSingleValuePresentInInnerListAttributeValue(listAttribute, objectAttribute, objectValue,
                innerListAttribute, innerObjectAttribute, innerObjectValue));
        return this;
    }

    /**
     * Verifies that if an attribute value exists for a given LIST attribute contained in a top LIST attribute.
     * As an usage example, see /subsystem=elytron/http-authentication-factory=* resource, there is an attribute
     * "mechanism-configurations" of type LIST, it contains "mechanism-realm-configurations" attribute of type LIST
     * of OBJECT, one of its attributes is "realm-name".
     * Then this method can verify the "realm-name" value.
     *
     * @param listAttribute the attribute name of type LIST.
     * @param filterAttribute the object attribute name
     * @param filterValue the object attribute value
     * @param singletonAttribute the inner attribute name of type LIST, contained in listAttribute
     */
    public ResourceVerifier verifyListAttributeObjectIsUndefined(String listAttribute, String filterAttribute,
            String filterValue, String singletonAttribute) throws Exception {
        waitFor(() -> {
            ModelNodeResult actualResult = ops.readAttribute(address, listAttribute);
            return actualResult.isSuccess() &&
                    actualResult.hasDefinedValue() &&
                    ModelNodeUtils.isObjectUndefinedInModelNodeList(actualResult.get(RESULT), filterAttribute,
                            filterValue, singletonAttribute);
        });

        ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttribute);
        modelNodeResult.assertSuccess();

        String path = address + "." + listAttribute + "={" + filterAttribute + "=" + filterValue + "}";
        String errorMessage = "For path: " + path + ". The attribute " + singletonAttribute + " exists, but is expteced to be undefined.";
        boolean found = ModelNodeUtils.isObjectUndefinedInModelNodeList(modelNodeResult.get(RESULT), filterAttribute,
                filterValue, singletonAttribute);
        assertTrue(errorMessage, found);
        return this;
    }



    /**
     * Verifies that list type attribute contains give value.
     *
     * @param value Value which should be present in the list.
     */
    public ResourceVerifier verifyListAttributeDoesNotContainValue(String attributeName, ModelNode value)
            throws Exception {
        return verifyListAttributeDoesNotContainValue(attributeName, value, null);
    }

    private boolean isModelNodePresentInListAttributeValue(String attributeName, ModelNode value) throws IOException {
        final ModelNodeResult modelNodeResult = ops.readAttribute(address, attributeName);
        return ModelNodeUtils.isValuePresentInModelNodeList(modelNodeResult.value(), value);
    }

    private boolean isSingleValuePresentInListAttributeValue(String listAttributeName, String innerAttribute,
            String value) throws IOException {
        final ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttributeName);
        return ModelNodeUtils.isValuePresentInModelNodeList(modelNodeResult.value(), innerAttribute, value);
    }

    private boolean isSingleValuePresentInListAttributeValue(String listAttributeName, String innerAttribute,
            ModelNode value) throws IOException {
        final ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttributeName);
        return ModelNodeUtils.isValuePresentInModelNodeList(modelNodeResult.value(), innerAttribute, value);
    }

    private boolean isSingleValuePresentInInnerListAttributeValue(String listAttribute, String objectAttribute, String objectValue,
            String innerListAttribute, String innerObjectAttribute, String innerObjectValue) throws IOException {
        final ModelNodeResult modelNodeResult = ops.readAttribute(address, listAttribute);
        if (modelNodeResult.isSuccess()) {
            ModelNode listInnerNode = modelNodeResult.get(RESULT);
            return ModelNodeUtils.isValuePresentInModelNodeListOfList(listInnerNode, objectAttribute, objectValue,
                    innerListAttribute, innerObjectAttribute, innerObjectValue);
        }
        return false;

    }


    // ------------------------------------------------------ default value / nillable

    /**
     * Verifies that all simple attributes which have default values equal their default values and all attributes
     * which are marked as nillable and don't have a default value are undefined.
     */
    public ResourceVerifier verifyReset() throws Exception {
        verifyDefaultValues();
        verifyNillable();
        return this;
    }

    /** Verifies that all attributes which have default values equal their default values. */
    private ResourceVerifier verifyDefaultValues() throws Exception {
        ResourceDescription resourceDescription = resourceDescription();
        List<String> attributesWithDefaultValues = resourceDescription.getAttributes().stream()
                .filter(property -> property.getValue().hasDefined(DEFAULT))
                .map(Property::getName)
                .collect(toList());
        for (String name : attributesWithDefaultValues) {
            ModelType attributeType = resourceDescription.getAttribute(name).get(TYPE).asType();
            ModelNode defaultValue = resourceDescription.defaultValue(name);
            if (attributeType != defaultValue.getType()) {
                // some resource descriptions are malformed :-(
                // skip these, since verifyAttribute() cannot cope with it: 0 != 0L
                log.warn("Malformed resource description for {} in {}: " +
                                "Attribute type is {}, but default value uses {}! " +
                                "Will skip this attribute in verifyDefaultValues()",
                        name, address, attributeType, defaultValue.getType());
                continue;
            }
            verifyAttribute(name, defaultValue, String.format("Attribute '%s' in '%s'", name, address));
        }
        return this;
    }

    /** Verifies that all attributes which are marked as nillable and don't have a default value are undefined */
    private ResourceVerifier verifyNillable() throws Exception {
        List<String> nillableAttributes = resourceDescription().getAttributes().stream()
                .filter(property -> {
                    ModelNode attributeDescription = property.getValue();
                    boolean nillable = attributeDescription.hasDefined(NILLABLE) &&
                            attributeDescription.get(NILLABLE).asBoolean();
                    boolean readOnly = attributeDescription.hasDefined(ACCESS_TYPE) &&
                            READ_ONLY.equals(attributeDescription.get(ACCESS_TYPE).asString());
                    boolean alternatives = attributeDescription.hasDefined(ALTERNATIVES) &&
                            !attributeDescription.get(ALTERNATIVES).asList().isEmpty();
                    boolean hasDefault = attributeDescription.hasDefined(DEFAULT);
                    ModelType type = attributeDescription.get(TYPE).asType();
                    boolean simpleValueType = attributeDescription.hasDefined(VALUE_TYPE) &&
                            attributeDescription.get(VALUE_TYPE).getType() != ModelType.OBJECT;
                    boolean nillableType = (type == ModelType.EXPRESSION ||
                            type == ModelType.LIST ||
                            type == ModelType.OBJECT ||
                            type == ModelType.PROPERTY ||
                            type == ModelType.STRING) &&
                            simpleValueType;
                    return nillable && !readOnly && !alternatives && !hasDefault && nillableType;
                })
                .map(Property::getName)
                .collect(toList());
        for (String attribute : nillableAttributes) {
            verifyAttributeIsUndefined(attribute, String.format("Attribute '%s' in '%s'", attribute, address));
        }
        return this;
    }

    private ResourceDescription resourceDescription() {
        if (resourceDescription == null) {
            resourceDescription = new ResourceDescription(address, client);
        }
        return resourceDescription;
    }


    // ------------------------------------------------------ internals

    private void waitFor(PropagationChecker checker) throws Exception {
        long start = System.currentTimeMillis();
        while (!checker.finallyPropagated() && System.currentTimeMillis() <= start + timeout) {
            log.debug("Not yet propagated therefore waiting.");
            Library.letsSleep(100);
        }
    }

    @FunctionalInterface
    private interface PropagationChecker {

        boolean finallyPropagated() throws Exception;
    }
}
