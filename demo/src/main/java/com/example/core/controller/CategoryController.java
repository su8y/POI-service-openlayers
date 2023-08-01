package com.example.core.controller;

import com.example.core.controller.dto.CategoryRequestDto;
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
    public List<Category> getList(Category currentValue) {
        List<Category> categoryByCurrentValue;
        if (currentValue.getLargeClassId() == null) categoryByCurrentValue= categoryRepository.findCategoryByCurrentValue();
        else  categoryByCurrentValue = categoryRepository.findCategoryByCurrentValue(currentValue);
        return categoryByCurrentValue;
    }
}
