package com.example.core.repository;

import com.example.core.category.CategoryRepository;
import com.example.core.config.TestQueryDslConfig;
import com.example.core.category.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
public class CategoryRepositoryTest {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * largecategory = 1  >> count = 206
     * middlecategory = 2 >> count = 71
     */
    @Test
    @DisplayName(value = "카테고리별 하위 카테고리 찾기 테스트")
    public void findCategoryTest() {
        //given
        Category currentCategory = new Category();
        currentCategory.setLargeClassId(1);
        List<?> categoryByCurrentValue;

        //1
        categoryByCurrentValue = categoryRepository.findCategoryByCurrentValue(currentCategory);
        Assertions.assertThat(categoryByCurrentValue.size()).isNotNull();

        //2
        currentCategory.setMiddleClassId(2);
        categoryByCurrentValue = categoryRepository.findCategoryByCurrentValue(currentCategory);
        Assertions.assertThat(categoryByCurrentValue.size()).isNotNull();

        //3
        currentCategory.setSmallClassId(3);
        currentCategory.setDetailClassId(3);
        currentCategory.setBottomClassId(2);
        categoryByCurrentValue = categoryRepository.findCategoryByCurrentValue(currentCategory);

        Assertions.assertThat(categoryByCurrentValue.size()).isEqualTo(1);
    }

    /**
     * 전체 카테고리 수 = 1538
     */
    @Test
    @DisplayName(value = "카테고리 상위카테고리 없을때 테스트")
    public void findCategoryTestByNull() {
//        List<Category> categoryByCurrentValue = categoryRepository.findCategoryByCurrentValue(new Category());

//        Assertions.assertThat(categoryByCurrentValue.size()).isEqualTo(1538);
    }
}
