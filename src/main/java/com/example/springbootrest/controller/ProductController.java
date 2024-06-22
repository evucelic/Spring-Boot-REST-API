package com.example.springbootrest.controller;

import com.example.springbootrest.model.Product;
import com.example.springbootrest.repository.ProductRepository;
import com.example.springbootrest.service.ProductService;
import com.example.springbootrest.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/products/filter")
    public ResponseEntity<List<Product>> getProductsByFilter(@RequestParam List<String> key,
                                                             @RequestParam List<String> operation,
                                                             @RequestParam List<String> value) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        for (int i = 0; i < key.size(); i++) {
            criteriaList.add(new SearchCriteria(key.get(i), operation.get(i), value.get(i)));
        }
        return ResponseEntity.ok(productService.searchProductsByFilter(criteriaList));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> getProductsBySearch(@RequestParam String name) {
        SearchCriteria nameCriteria = new SearchCriteria("name", ":", name);
        return ResponseEntity.ok(productService.searchProductsByName(nameCriteria));
    }
}
