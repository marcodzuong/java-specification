package com.marco.specification.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SimpleSpecification<T> implements Specification<T> {
    private final List<SearchFilterWrapper> filters;

    public SimpleSpecification(List<SearchFilterWrapper> operators) {
        this.filters = operators;
    }

    private Predicate getPredicate(Root<T> root, CriteriaBuilder cb, List<SearchFilter> filters) {
        if (filters != null && !filters.isEmpty()) {
            Predicate resultPre = null;
            for (int i = 0; i < filters.size(); i++) {
                SearchFilter op = filters.get(i);
                if (i == 0) {
                    resultPre = generatePredicate(root, cb, op);
                    continue;
                }
                Predicate pre = generatePredicate(root, cb, op);
                if (pre == null) {
                    continue;
                }
                switch (op.join) {
                    case and -> resultPre = cb.and(resultPre, pre);
                    case or -> resultPre = cb.or(resultPre, pre);
                    default -> {
                    }
                }
            }
            return resultPre;
        }
        return null;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (filters != null && !filters.isEmpty()) {
            Predicate result = null;
            for (int i = 0; i < filters.size(); i++) {
                if (i == 0) {
                    result = getPredicate(root, cb, filters.get(i).getFilters());
                    continue;
                }
                Predicate pre = getPredicate(root, cb, filters.get(i).getFilters());
                if (pre == null) {
                    continue;
                }
                switch (filters.get(i).getJoin()) {
                    case and -> result = cb.and(result, pre);
                    case or -> result = cb.or(result, pre);
                    default -> {
                    }
                }
            }
            if (result==null) return cb.conjunction();
            return result;
        }
        return cb.conjunction();
    }

    private Predicate generatePredicate(Root<T> root, CriteriaBuilder cb, SearchFilter op) {
        Object value = op.value;
        String[] names = StringUtils.split(op.fieldName, ".");
        if (names == null) return null;
        Path expression = root.get(names[0]);
        for (int i = 1; i < names.length; i++) {
            expression = expression.get(names[i]);
        }


        switch (op.operator) {
            case EQ:
                return cb.equal(expression, value);
            case NE:
                return cb.notEqual(expression, value);
            case GTE:
                return cb.greaterThanOrEqualTo(expression, (Comparable) value);
            case LTE:
                return cb.lessThanOrEqualTo(expression, (Comparable) value);
            case GT:
                return cb.greaterThan(expression, (Comparable) value);
            case LT:
                return cb.lessThan(expression, (Comparable) value);
            case IN:
                if (value.getClass().isArray()) {
                    return expression.in((Object[]) value);
                } else {
                    return expression.in((Collection) value);
                }
            case NOTIN:
                if (value instanceof Collection) {
                    return cb.not(expression.in((Collection) value));
                }
                return cb.not(expression.in(value));
            case LIKE:
                return cb.like(expression.as(String.class), "%" + value + "%");
            case LIKEL:
                return cb.like(expression.as(String.class), value + "%");
            case LIKER:
                return cb.like(expression.as(String.class), "%" + value);
            case ISNULL:
                return cb.isNull(expression);
            case ISNOTNULL:
                return cb.isNotNull(expression);
            case BETWEEN:
                if (value instanceof Date[]) {
                    Date[] dateArray = (Date[]) value;
                    return cb.between(expression, dateArray[0], dateArray[1]);
                }
            default:
                return null;
        }
    }
}
