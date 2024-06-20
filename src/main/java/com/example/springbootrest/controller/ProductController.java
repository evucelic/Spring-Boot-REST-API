package com.example.springbootrest.controller;

import com.example.springbootrest.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "https://dummyjson.com";

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        String url = BASE_URL + "/products";
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);
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

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductsById(@PathVariable Long id){
        String url = BASE_URL + "/products/" + id;
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode productNode = responseEntity.getBody();

        if (productNode == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Product product = objectMapper.treeToValue(productNode, Product.class);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
