package com.example.springbootrest.specification;

import com.example.springbootrest.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class FilterSpecification implements Specification<Product>{
    private SearchCriteria criteria;

    public FilterSpecification(SearchCriteria criteria){
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(">")){
            return criteriaBuilder.greaterThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }

        else if (criteria.getOperation().equalsIgnoreCase("<")){
            return criteriaBuilder.lessThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }

        else if(criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }

        return null;
    }
}
