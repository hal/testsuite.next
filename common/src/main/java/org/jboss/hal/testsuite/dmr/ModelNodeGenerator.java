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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;

/**
 * Intended to help with creating {@link ModelNode} objects to be used e.g. in {@link ResourceVerifier}
 * Created by pjelinek on May 27, 2016
 */
public class ModelNodeGenerator {

    /**
     * @return {@link ModelNode} of type {@link ModelType} {@code OBJECT} including map of just one property from the parameter
     */
    public ModelNode createObjectNodeWithPropertyChild(String firstChildPropertyKey, String firstChildPropertyValue) {
        Map<String, ModelNode> propertyMap = new HashMap<>(1);
        propertyMap.put(firstChildPropertyKey, new ModelNode(firstChildPropertyValue));
        return createObjectNodeWithPropertyChildren(propertyMap);
    }

    /**
     * @return {@link ModelNode} of type {@link ModelType} {@code OBJECT} including map of properties from the parameter
     */
    public ModelNode createObjectNodeWithPropertyChildren(Map<String, ModelNode> childPropertiesMap) {
        ModelNode parent = new ModelNode();
        childPropertiesMap.forEach((propertyKey, propertyValue) -> parent.get(propertyKey).set(propertyValue));
        return parent;
    }

    /** Builder for creating {@link ModelNode} of type {@link ModelType#OBJECT} including map of properties */
    public static final class ModelNodePropertiesBuilder {

        private Map<String, ModelNode> propertyMap = new LinkedHashMap<>();

        public ModelNodePropertiesBuilder() { }

        public ModelNodePropertiesBuilder addProperty(String key, ModelNode value) {
            this.propertyMap.put(key, value);
            return this;
        }

        public ModelNodePropertiesBuilder addProperty(String key, String value) {
            return addProperty(key, new ModelNode(value));
        }

        public ModelNodePropertiesBuilder addUndefinedProperty(String key) {
            return addProperty(key, new ModelNode());
        }

        public ModelNode build() {
            if (propertyMap.isEmpty()) {
                throw new IllegalStateException("You have to add any property first!");
            }
            return new ModelNodeGenerator().createObjectNodeWithPropertyChildren(propertyMap);
        }

    }


    /** Builder for creating {@link ModelNode} of type {@link ModelType#LIST} */
    public static final class ModelNodeListBuilder {

        private List<ModelNode> nodeList = new ArrayList<>();

        private boolean isEmpty = false;

        public ModelNodeListBuilder() { }

        public ModelNodeListBuilder(ModelNode node) {
            this.nodeList.add(node);
        }

        public ModelNodeListBuilder empty() {
            this.isEmpty = true;
            return this;
        }

        public ModelNodeListBuilder addNode(ModelNode node) {
            this.nodeList.add(node);
            return this;
        }

        public ModelNodeListBuilder addAll(String... values) {
            for (String value : values) {
                this.nodeList.add(new ModelNode(value));
            }
            return this;
        }

        public ModelNode build() {
            ModelNode parent = new ModelNode();
            if (isEmpty) {
                parent.add();
                parent.remove(0);
                return parent; // empty list
            }
            if (nodeList.isEmpty()) {
                throw new IllegalStateException("No child node yet set! You have to either set list as empty or add any"
                        + " ModelNode first!");
            }
            nodeList.forEach(parent::add);
            return parent;
        }
    }
}
