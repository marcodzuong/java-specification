package com.marco.specification.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author MarcoDuong
 */
public class DynamicSpecifications {
    public static <T> Specification<T> bySearchFilter(final List<SearchFilterWrapper> filters) {
        return new SimpleSpecification<>(filters);
    }
}
