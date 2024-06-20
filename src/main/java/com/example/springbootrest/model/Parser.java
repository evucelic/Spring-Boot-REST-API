package com.example.springbootrest.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class Parser extends JsonDeserializer<Product> {

    @Override
    public Product deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        Long id = node.get("id").asLong();
        String category = node.get("category").asText();
        String name = node.get("title").asText();
        Double price = node.get("price").asDouble();
        String description = node.get("description").asText();
        String imageUrl = node.get("thumbnail").asText();

        return new Product(id,imageUrl, name, price, description, category);
    }
}
