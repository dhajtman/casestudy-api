package com.casestudy.api.config;

import com.casestudy.api.model.Ordered;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class OrderedSerializer {

    public static class OrderedJsonSerializer
            extends JsonSerializer<Ordered> {

        @Override
        public void serialize(Ordered ordered, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("productSerialized", ordered.getProduct());
            jsonGenerator.writeEndObject();
        }
    }

    public static class OrderedJsonDeserializer
            extends JsonDeserializer<Ordered> {

        @Override
        public Ordered deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {

            TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
            TextNode productSerialized = (TextNode) treeNode.get(
                    "productSerialized");
            return Ordered.builder().product(productSerialized.asText()).build();
        }
    }
}
