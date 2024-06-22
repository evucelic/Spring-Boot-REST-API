package com.example.springbootrest.specification;

import com.example.springbootrest.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class FilterSpecification implements Specification<Product>{
    private final List<SearchCriteria> criteriaList;

    public FilterSpecification(List<SearchCriteria> criteriaList){
        this.criteriaList = criteriaList;
    }
    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder.conjunction();

        for (SearchCriteria criteria : criteriaList){
            if ("category".equalsIgnoreCase(criteria.getKey())){
                if (criteria.getOperation().equalsIgnoreCase(":")){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue().toString()));
                }
            }
            else if ("price".equalsIgnoreCase(criteria.getKey())){
                if (criteria.getOperation().equalsIgnoreCase(">")){
                    predicate  = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                }

                else if (criteria.getOperation().equalsIgnoreCase("<")){
                    predicate  = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                }
            }
        }

        return predicate;
    }
}
