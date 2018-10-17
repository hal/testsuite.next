package org.jboss.hal.testsuite.util.audit.log.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DecimalNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class JsonNodeToModelNodeParser {

    private static final Map<Predicate<JsonNode>, Function<JsonNode, ModelNode>> converters = new HashMap<>();

    static {
        converters.put(JsonNode::isBigDecimal, JsonNodeToModelNodeParser::toBigDecimal);
        converters.put(JsonNode::isBigInteger, JsonNodeToModelNodeParser::toBigInteger);
        converters.put(JsonNode::isBoolean, JsonNodeToModelNodeParser::toBoolean);
        converters.put(JsonNode::isDouble, JsonNodeToModelNodeParser::toDouble);
        converters.put(JsonNode::isInt, JsonNodeToModelNodeParser::toInt);
        converters.put(JsonNode::isLong, JsonNodeToModelNodeParser::toLong);
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

    public static ModelNode toList(JsonNode jsonNode) {
        if (!jsonNode.isArray()) {
            throw new IllegalArgumentException("Cannot convert " + jsonNode + "to list");
        }
        ModelNodeGenerator.ModelNodeListBuilder listResultBuilder = new ModelNodeGenerator.ModelNodeListBuilder();
        Function<JsonNode, ModelNode> transformer =
            converters.entrySet().stream().filter(entry -> entry.getKey().test(jsonNode))
                .map(Map.Entry::getValue).findFirst().orElse(jsonNode1 -> new ModelNode());
        jsonNode.iterator().forEachRemaining(node -> listResultBuilder.addNode(transformer.apply(node)));
        return listResultBuilder.build();
    }

    /*public static ModelNode toObject(JsonNode jsonNode) {
        if (!jsonNode.isObject()) {
            throw new IllegalArgumentException("Cannot convert" + jsonNode + "to object");
        }
        ModelNodeGenerator.ModelNodePropertiesBuilder modelNodeObjectBuilder =
            new ModelNodeGenerator.ModelNodePropertiesBuilder();
    }*/

    public static void main(String[] args) {
        String text = "{\n"
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
            + "        \"operation\" : \"composite\",\n"
            + "        \"address\" : [],\n"
            + "        \"steps\" : [{\n"
            + "            \"name\" : \"connection-listener-property\",\n"
            + "            \"value\" : {\"some\" : \"prop\"},\n"
            + "            \"operation\" : \"write-attribute\",\n"
            + "            \"address\" : [\n"
            + "                {\n"
            + "                    \"subsystem\" : \"datasources\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"data-source\" : \"ExampleDS\"\n"
            + "                }\n"
            + "            ]\n"
            + "        }],\n"
            + "        \"operation-headers\" : {\n"
            + "            \"access-mechanism\" : \"HTTP\",\n"
            + "            \"caller-type\" : \"user\"\n"
            + "        }\n"
            + "    }]\n"
            + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(text);
            JsonNode addressesNode = node.get("ops").get(0).get("steps").get(0).get("address");
            Iterable<JsonNode> iterable = addressesNode::iterator;
            List<Address> addressList = StreamSupport.stream(iterable.spliterator(), false).flatMap(item -> {
                Iterable<Map.Entry<String, JsonNode>> keyValue = () -> item.fields();
                return StreamSupport.stream(keyValue.spliterator(), false);
            }).map(entry -> Address.of(entry.getKey(), entry.getValue().textValue())).collect(Collectors.toList());
            System.out.println(addressList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
