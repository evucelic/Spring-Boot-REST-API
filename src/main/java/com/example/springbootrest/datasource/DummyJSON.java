package com.example.springbootrest.datasource;

import com.example.springbootrest.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DummyJSON implements DataSource{
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://dummyjson.com/products?limit=0";

    @Autowired
    public DummyJSON(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Product> fetchProducts() {
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(BASE_URL, JsonNode.class);
        JsonNode productsNode = Objects.requireNonNull(responseEntity.getBody()).get("products");
        List<Product> products = new ArrayList<>();

        for (JsonNode node : productsNode) {
            try {
                Product product = objectMapper.treeToValue(node, Product.class);
                products.add(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return products;
    }
}
