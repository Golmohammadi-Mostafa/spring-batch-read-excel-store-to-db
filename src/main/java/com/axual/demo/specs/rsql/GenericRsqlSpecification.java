package com.axual.demo.specs.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRsqlSpecification<T> implements Specification<T> {
    private static final String POINT = ".";

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public GenericRsqlSpecification(final String property, final ComparisonOperator operator, final List<String> arguments) {
        super();
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    @Override
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        Path<String> propertyExpression = parseProperty(root);
        List<Object> args = castArguments(propertyExpression);
        final Object argument = args.get(0);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

            case EQUAL: {
                if (argument instanceof String) {
                    return builder.like(propertyExpression, argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNull(propertyExpression);
                } else {
                    return builder.equal(propertyExpression, argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(propertyExpression, argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(propertyExpression);
                } else {
                    return builder.notEqual(propertyExpression, argument);
                }
            }
            case GREATER_THAN: {
                return builder.greaterThan(propertyExpression, argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                return builder.greaterThanOrEqualTo(propertyExpression, argument.toString());
            }
            case LESS_THAN: {
                return builder.lessThan(propertyExpression, argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                return builder.lessThanOrEqualTo(propertyExpression, argument.toString());
            }
            case IN:
                return propertyExpression.in(args);
            case NOT_IN:
                return builder.not(propertyExpression.in(args));
        }
        return null;
    }

    private Path<String> parseProperty(Root<T> root) {
        Path<String> path;
        if (property.contains(POINT)) {
            String[] pathSteps = property.split("\\.");
            String step = pathSteps[0];
            path = root.get(step);
            for (int i = 1; i <= pathSteps.length - 1; i++) {
                path = path.get(pathSteps[i]);
            }
        } else {
            path = root.get(property);
        }
        return path;
    }

    private List<Object> castArguments(Path<?> propertyExpression) {
        Class type = propertyExpression.getJavaType();
        return arguments.stream().map(arg -> {
            if (Integer.TYPE.equals(type)) {
                return Integer.parseInt(arg);
            }
            if (Long.TYPE.equals(type)) {
                return Long.parseLong(arg);
            }
            if (type.isEnum()) {
                return Enum.valueOf(type, arg);
            } else return arg;
        }).collect(Collectors.toList());
    }
}
