package com.example.springbootrest.service;

import com.example.springbootrest.model.Product;
import com.example.springbootrest.repository.ProductRepository;
import com.example.springbootrest.specification.FilterSpecification;
import com.example.springbootrest.specification.SearchCriteria;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://dummyjson.com";


    @Autowired
    public ProductService(ProductRepository productRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void syncFromWebsite(){
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

        productRepository.saveAll(products);
    }
    @PostConstruct
    public void syncOnServerStart(){
        syncFromWebsite();
    }

    public List<Product> searchProducts(List<SearchCriteria> criteriaList){
        Specification<Product> specification = Specification.where(null);

        for (SearchCriteria criteria : criteriaList){
            specification = specification.and(new FilterSpecification(criteria));
        }

        return productRepository.findAll(specification);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
