package com.example.core.controller;

import com.example.core.model.Category;
import com.example.core.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = CategoryController.class)
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class CategoryControllerTest {
    @Autowired
    MockMvc mvc;
    @Mock
    CategoryRepository categoryRepository;


    @Test
    public void categorytest() throws Exception {
        Category buildCategory = Category.builder().largeClassId(7).build();

        when(categoryRepository.findCategoryByCurrentValue(buildCategory)).thenReturn(null);
        mvc.perform(get("http://localhost:8080/category"))
                .andDo(print())
                .andExpect(content().string("null"));

    }

}