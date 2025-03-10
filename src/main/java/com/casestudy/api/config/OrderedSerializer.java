package com.casestudy.api.config;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.model.OrderedNew;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.function.Function;

@JsonComponent
public class OrderedSerializer {

    public static class OrderedJsonSerializer
            extends JsonSerializer<OrderedNew> {

        @Override
        public void serialize(OrderedNew ordered, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {

            Function<OrderedNew, String> function = OrderedNew::getProduct;
            String string = function.apply(ordered);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("productSerialized", string);
            jsonGenerator.writeEndObject();
        }
    }

    public static class OrderedJsonDeserializer
            extends JsonDeserializer<OrderedNew> {

        @Override
        public OrderedNew deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {

            TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
            TextNode productSerialized = (TextNode) treeNode.get("productSerialized");
            return OrderedNew.builder().product(productSerialized.asText()).build();
        }
    }
}
