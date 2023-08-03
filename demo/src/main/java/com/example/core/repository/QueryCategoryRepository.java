package com.example.core.repository;

import com.example.core.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryCategoryRepository {
    List<?> findCategoryByCurrentValue(Category currentValue);

}
