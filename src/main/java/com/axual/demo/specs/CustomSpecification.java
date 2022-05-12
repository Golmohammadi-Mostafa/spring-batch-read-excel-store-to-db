package com.axual.demo.specs;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CustomSpecification<T> implements Specification<T> {

    private final List<SearchCriteria> list;

    public CustomSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(root.get(criteria.getKey()), convertType(criteria.getValue())));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(root.get(criteria.getKey()), convertType(criteria.getValue())));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), convertType(criteria.getValue())));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), convertType(criteria.getValue())));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()).not());
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Comparable convertType(Object o) {
        if (Objects.isNull(o)) {
            return null;
        }
        if (o instanceof Date) {
            return (Date) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        if (o instanceof Long) {
            return (Long) o;
        }
        if (o instanceof BigInteger) {
            return (BigInteger) o;
        }
        if (o instanceof Float) {
            return (Float) o;
        }
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        return o.toString();
    }
}