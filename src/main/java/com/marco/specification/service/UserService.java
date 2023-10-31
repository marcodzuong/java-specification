package com.marco.specification.service;

import com.marco.specification.entities.User;
import com.marco.specification.repository.UserRepository;
import com.marco.specification.specification.Page;
import com.marco.specification.specification.SearchFilter;
import com.marco.specification.specification.SearchFilterWrapper;
import com.marco.specification.specification.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {

    @Autowired
    private UserRepository repository;

    public Page<User> query(Integer size, Integer page, String userName) {
        try {
            Page<User> userPage = Page.defaultPage(size, page);
            SearchFilterWrapper search = new SearchFilterWrapper();
            search.addFilter(SearchFilter.build("userName", SearchFilter.Operator.LIKE, userName));
            userPage.setFilters(List.of(search));
            return SpecificationUtil.query(userPage, repository,"id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
