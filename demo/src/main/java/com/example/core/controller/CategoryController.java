package com.example.core.controller;

import com.example.core.model.Category;
import com.example.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping("category")
    public List<?> getList(Category currentValue) {
        return categoryRepository.findCategoryByCurrentValue(currentValue);
    }
}
