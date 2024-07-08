package com.example.springbootrest.datasource;

import com.example.springbootrest.model.Product;

import java.util.List;

public interface DataSource {
    List<Product> fetchProducts();
}
