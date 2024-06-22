package com.example.springbootrest.specification;

import com.example.springbootrest.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecification implements Specification<Product> {
    private final SearchCriteria nameCriteria;

    public SearchSpecification(SearchCriteria nameCriteria) {
        this.nameCriteria = nameCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if ("name".equalsIgnoreCase(nameCriteria.getKey()) && nameCriteria.getOperation().equalsIgnoreCase(":") && nameCriteria.getValue() != null) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + nameCriteria.getValue().toString().toLowerCase() + "%");
        }
        return null;
    }
}
