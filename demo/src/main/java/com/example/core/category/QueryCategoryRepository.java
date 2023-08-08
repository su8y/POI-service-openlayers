package com.example.core.category;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryCategoryRepository {
    List<?> findCategoryByCurrentValue(Category currentValue);

}
