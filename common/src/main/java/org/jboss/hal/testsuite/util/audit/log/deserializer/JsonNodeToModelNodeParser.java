package org.jboss.hal.testsuite.util.audit.log.deserializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;

public class JsonNodeToModelNodeParser {

    private JsonNodeToModelNodeParser() { }

    private static final Map<Predicate<JsonNode>, Function<JsonNode, ModelNode>> converters = new HashMap<>();

    static {
        converters.put(JsonNode::isBigDecimal, JsonNodeToModelNodeParser::toBigDecimal);
        converters.put(JsonNode::isBigInteger, JsonNodeToModelNodeParser::toBigInteger);
        converters.put(JsonNode::isBoolean, JsonNodeToModelNodeParser::toBoolean);
        converters.put(JsonNode::isDouble, JsonNodeToModelNodeParser::toDouble);
        converters.put(JsonNode::isInt, JsonNodeToModelNodeParser::toInt);
        converters.put(JsonNode::isLong, JsonNodeToModelNodeParser::toLong);
        converters.put(JsonNode::isArray, JsonNodeToModelNodeParser::toList);
        converters.put(JsonNode::isObject, JsonNodeToModelNodeParser::toObject);
        converters.put(JsonNode::isTextual, JsonNodeToModelNodeParser::toString);
    }

    private static String errorMessage(JsonNode jsonNode, String targetType) {
        return String.format("Cannot convert %s to %s", jsonNode, targetType);
    }

    public static ModelNode toBigDecimal(JsonNode jsonNode) {
        if (!jsonNode.isBigDecimal()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, BigDecimal.class.getName()));
        }
        return new ModelNode(jsonNode.decimalValue());
    }

    public static ModelNode toBigInteger(JsonNode jsonNode) {
        if (!jsonNode.isBigInteger()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, BigInteger.class.getName()));
        }
        return new ModelNode(jsonNode.bigIntegerValue());
    }

    public static ModelNode toBoolean(JsonNode jsonNode) {
        if (!jsonNode.isBoolean()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, Boolean.class.getName()));
        }
        return new ModelNode(jsonNode.booleanValue());
    }

    public static ModelNode toDouble(JsonNode jsonNode) {
        if (!jsonNode.isDouble()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, Double.class.getName()));
        }
        return new ModelNode(jsonNode.doubleValue());
    }

    public static ModelNode toInt(JsonNode jsonNode) {
        if (!jsonNode.isInt()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, Integer.class.getName()));
        }
        return new ModelNode(jsonNode.intValue());
    }

    public static ModelNode toLong(JsonNode jsonNode) {
        if (!jsonNode.isLong()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, Long.class.getName()));
        }
        return new ModelNode(jsonNode.longValue());
    }

    public static ModelNode toString(JsonNode jsonNode) {
        if (!jsonNode.isTextual()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, String.class.getName()));
        }
        return new ModelNode(jsonNode.textValue());
    }

    public static ModelNode toList(JsonNode jsonNode) {
        if (!jsonNode.isArray()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, List.class.getName()));
        }
        ModelNodeGenerator.ModelNodeListBuilder listResultBuilder = new ModelNodeGenerator.ModelNodeListBuilder();
        Function<JsonNode, ModelNode> transformer = getTransformerFor(jsonNode);
        jsonNode.iterator().forEachRemaining(node -> listResultBuilder.addNode(transformer.apply(node)));
        return listResultBuilder.build();
    }

    public static ModelNode toObject(JsonNode jsonNode) {
        if (!jsonNode.isObject()) {
            throw new IllegalArgumentException(errorMessage(jsonNode, Object.class.getName()));
        }
        ModelNodeGenerator.ModelNodePropertiesBuilder modelNodeObjectBuilder =
            new ModelNodeGenerator.ModelNodePropertiesBuilder();
        jsonNode.fields().forEachRemaining(entry -> {
            modelNodeObjectBuilder.addProperty(entry.getKey(),
                getTransformerFor(entry.getValue()).apply(entry.getValue()));
        });
        return modelNodeObjectBuilder.build();
    }

    public static Function<JsonNode, ModelNode> getTransformerFor(JsonNode jsonNode) {
        return converters.entrySet().stream().filter(entry -> entry.getKey().test(jsonNode))
            .map(Map.Entry::getValue).findFirst().orElse(undefinedNode -> new ModelNode());
    }
}
