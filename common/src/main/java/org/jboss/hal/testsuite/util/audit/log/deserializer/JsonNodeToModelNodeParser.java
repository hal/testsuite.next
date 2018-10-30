package org.jboss.hal.testsuite.util.audit.log.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;

public class JsonNodeToModelNodeParser {

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

    public static ModelNode toBigDecimal(JsonNode jsonNode) {
        if (!jsonNode.isBigDecimal()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to big decimal");
        }
        return new ModelNode(jsonNode.decimalValue());
    }

    public static ModelNode toBigInteger(JsonNode jsonNode) {
        if (!jsonNode.isBigInteger()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to big integer");
        }
        return new ModelNode(jsonNode.bigIntegerValue());
    }

    public static ModelNode toBoolean(JsonNode jsonNode) {
        if (!jsonNode.isBoolean()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to boolean");
        }
        return new ModelNode(jsonNode.booleanValue());
    }

    public static ModelNode toDouble(JsonNode jsonNode) {
        if (!jsonNode.isDouble()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to double");
        }
        return new ModelNode(jsonNode.doubleValue());
    }

    public static ModelNode toInt(JsonNode jsonNode) {
        if (!jsonNode.isInt()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to int");
        }
        return new ModelNode(jsonNode.intValue());
    }

    public static ModelNode toLong(JsonNode jsonNode) {
        if (!jsonNode.isLong()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to long");
        }
        return new ModelNode(jsonNode.longValue());
    }

    public static ModelNode toString(JsonNode jsonNode) {
        if (!jsonNode.isTextual()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to string");
        }
        return new ModelNode(jsonNode.textValue());
    }

    public static ModelNode toList(JsonNode jsonNode) {
        if (!jsonNode.isArray()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to list");
        }
        ModelNodeGenerator.ModelNodeListBuilder listResultBuilder = new ModelNodeGenerator.ModelNodeListBuilder();
        Function<JsonNode, ModelNode> transformer = getTransformerFor(jsonNode);
        jsonNode.iterator().forEachRemaining(node -> listResultBuilder.addNode(transformer.apply(node)));
        return listResultBuilder.build();
    }

    public static Function<JsonNode, ModelNode> getTransformerFor(JsonNode jsonNode) {
        return converters.entrySet().stream().filter(entry -> entry.getKey().test(jsonNode))
            .map(Map.Entry::getValue).findFirst().orElse(undefinedNode -> new ModelNode());
    }

    public static ModelNode toObject(JsonNode jsonNode) {
        if (!jsonNode.isObject()) {
            throw new IllegalArgumentException("Cannot convert" + jsonNode + "to object");
        }
        ModelNodeGenerator.ModelNodePropertiesBuilder modelNodeObjectBuilder =
            new ModelNodeGenerator.ModelNodePropertiesBuilder();
        jsonNode.fields().forEachRemaining(entry -> {
            modelNodeObjectBuilder.addProperty(entry.getKey(),
                getTransformerFor(entry.getValue()).apply(entry.getValue()));
        });
        return modelNodeObjectBuilder.build();
    }

    public static void main(String[] args) {
        String text
            = "{\n"
            + "    \"type\" : \"core\",\n"
            + "    \"r/o\" : false,\n"
            + "    \"booting\" : false,\n"
            + "    \"version\" : \"14.0.0.CR1-SNAPSHOT\",\n"
            + "    \"user\" : \"anonymous\",\n"
            + "    \"domainUUID\" : null,\n"
            + "    \"access\" : \"HTTP\",\n"
            + "    \"remote-address\" : \"/172.80.0.1\",\n"
            + "    \"success\" : true,\n"
            + "    \"ops\" : [{\n"
            + "        \"agree-to-terms-of-service\" : true,\n"
            + "        \"staging\" : true,\n"
            + "        \"operation\" : \"create-account\",\n"
            + "        \"address\" : [\n"
            + "            {\n"
            + "                \"subsystem\" : \"elytron\"\n"
            + "            },\n"
            + "            {\n"
            + "                \"certificate-authority-account\" : \"ccc\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"operation-headers\" : {\n"
            + "            \"access-mechanism\" : \"HTTP\",\n"
            + "            \"caller-type\" : \"user\"\n"
            + "        }\n"
            + "    }]\n"
            + "}\n"
            + "{\n"
            + "    \"type\" : \"core\",\n"
            + "    \"r/o\" : false,\n"
            + "    \"booting\" : false,\n"
            + "    \"version\" : \"14.0.0.CR1-SNAPSHOT\",\n"
            + "    \"user\" : \"anonymous\",\n"
            + "    \"domainUUID\" : null,\n"
            + "    \"access\" : \"HTTP\",\n"
            + "    \"remote-address\" : \"/172.80.0.1\",\n"
            + "    \"success\" : true,\n"
            + "    \"ops\" : [{\n"
            + "        \"agree-to-terms-of-service\" : true,\n"
            + "        \"staging\" : true,\n"
            + "        \"operation\" : \"create-account\",\n"
            + "        \"address\" : [\n"
            + "            {\n"
            + "                \"subsystem\" : \"elytron\"\n"
            + "            },\n"
            + "            {\n"
            + "                \"certificate-authority-account\" : \"ccc\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"operation-headers\" : {\n"
            + "            \"access-mechanism\" : \"HTTP\",\n"
            + "            \"caller-type\" : \"user\"\n"
            + "        }\n"
            + "    }]\n"
            + "}\n";
        Pattern pattern =
            Pattern.compile("(^" + Pattern.quote("{") + "$.*?^" + Pattern.quote("}") + "$)",
                Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        System.out.println(matcher.find());
    }
}
