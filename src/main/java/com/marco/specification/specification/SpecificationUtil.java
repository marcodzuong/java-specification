package com.marco.specification.specification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public class SpecificationUtil {

    /**
     * idNames is default sort
     */
    public static <DOMAIN> Page<DOMAIN> query(Page<DOMAIN> page, JpaSpecificationExecutor<DOMAIN> repository, String... idNames) {
        Pageable pageable = null;
        if (page.getSort() != null) {
            pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), page.getSort());
        } else {
            pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, idNames);
        }
        Specification<DOMAIN> specification = DynamicSpecifications.bySearchFilter(page.getFilters());
        org.springframework.data.domain.Page<DOMAIN> pageResult = repository.findAll(specification, pageable);
        page.setTotal(Integer.parseInt(String.valueOf(pageResult.getTotalElements())));
        page.setRecords(pageResult.getContent());
        return page;
    }

    public static <DOMAIN> Page<DOMAIN> query(Page<DOMAIN> page, JpaSpecificationExecutor<DOMAIN> repository) {
        return query(page, repository, "id");
    }

    public static <DOMAIN> List<DOMAIN> query(List<SearchFilterWrapper> filters, Sort sort, JpaSpecificationExecutor<DOMAIN> repository) {
        Specification<DOMAIN> specification = DynamicSpecifications.bySearchFilter(filters);
        if (sort == null) {
            return repository.findAll(specification);
        }
        return repository.findAll(specification, sort);

    }

    public static <DOMAIN> List<DOMAIN> query(SearchFilterWrapper filter, Sort sort, JpaSpecificationExecutor<DOMAIN> repository) {
        if (filter != null) {
            return query(List.of(filter), sort, repository);
        } else {
            return query(new ArrayList<>(), sort, repository);
        }
    }

    public static <DOMAIN> List<DOMAIN> query(SearchFilterWrapper filter, JpaSpecificationExecutor<DOMAIN> repository) {
        return query(filter, null, repository);
    }


}
