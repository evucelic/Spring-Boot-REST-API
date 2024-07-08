package com.example.springbootrest.datasource;

import com.example.springbootrest.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandomDataSource implements DataSource{
    @Override
    public List<Product> fetchProducts() {
        return List.of();
    }
}
