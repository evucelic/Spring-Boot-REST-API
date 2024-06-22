package com.example.springbootrest.service;
import com.example.springbootrest.model.Product;
import com.example.springbootrest.repository.ProductRepository;
import com.example.springbootrest.specification.FilterSpecification;
import com.example.springbootrest.specification.SearchCriteria;
import com.example.springbootrest.specification.SearchSpecification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
        String url = BASE_URL + "/products?limit=0";
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
        System.out.printf("Synced %d products from website%n", products.size());
    }
    @PostConstruct
    public void syncOnServerStart(){
        syncFromWebsite();
    }
    @Cacheable(value= "productsByFilter", key = "#criteriaList")
    public List<Product> searchProductsByFilter(List<SearchCriteria> criteriaList){

        if (criteriaList.isEmpty()) return productRepository.findAll();

        Set<String> keys = new HashSet<>();
        for (SearchCriteria criteria : criteriaList){
            if (!criteria.getKey().equalsIgnoreCase("category") && !criteria.getKey().equalsIgnoreCase("price"))
                throw new IllegalArgumentException("Invalid filter arguments");
            String keyOperationPair = criteria.getKey() + ":" + criteria.getOperation();
            if (!keys.add(keyOperationPair)) {
                throw new IllegalArgumentException("Only one instance of a (key,operation) pair should be present in the filter");
            }
        }

        Specification<Product> specification = new FilterSpecification(criteriaList);
        return productRepository.findAll(specification);
    }

    @Cacheable(value = "productsByName", key = "#nameCriteria")
    public List<Product> searchProductsByName(SearchCriteria nameCriteria){
        Specification<Product> specification = new SearchSpecification(nameCriteria);
        List<Product> products = productRepository.findAll(specification);
        System.out.printf("Found %d products with name containing '%s'%n", products.size(), nameCriteria.getValue());
        return products;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
