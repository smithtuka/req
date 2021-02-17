package com.galbern.req.sandbox;


import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EmployeeSpecification implements Specification<Employee> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        if(criteria.getOperation().equalsIgnoreCase(">"))
            return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());

            else if(criteria.getOperation().equalsIgnoreCase("<"))
                return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());

        else if(criteria.getOperation().equalsIgnoreCase(":")){

            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
