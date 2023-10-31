package com.marco.specification.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SearchFilterWrapper {

    private List<SearchFilter> filters;
    private SearchFilter.Join join = SearchFilter.Join.and;


    public void addFilter(SearchFilter filter) {
        if (filter == null) {
            return;
        }
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(filter);
    }

}
