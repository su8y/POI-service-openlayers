package com.example.core.repository;

import com.example.core.model.Category;
import com.example.core.model.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.example.core.model.QCategory.category;


@Component
@RequiredArgsConstructor
public class QueryCategoryRepositoryImpl implements QueryCategoryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Category> findCategoryByCurrentValue() {
        JPAQuery<?> from = queryFactory.from(category);
        JPAQuery<Category> where = from.select(Projections.fields(Category.class, category.largeClassId, category.largeClassName))
                .distinct();

        return where.fetch();
    }

    public List<Category> findCategoryByCurrentValue(Category currentValue) {
        JPAQuery<?> from = queryFactory.from(category);
        JPAQuery<Category> where = from.select(category)
                .where(
                        isEqualsLargeClass(currentValue.getLargeClassId()),
                        isEqualsMiddleClass(currentValue.getMiddleClassId()),
                        isEqualsSmallClass(currentValue.getSmallClassId()),
                        isEqualsDetailClass(currentValue.getDetailClassId()),
                        isEqualsBottomClass(currentValue.getBottomClassId())
                )
                .distinct();

        return where.fetch();
    }

    public BooleanExpression isEqualsLargeClass(Integer id) {
        if (id == null || id < 0) return null;
        return category.largeClassId.eq(id);
    }

    public BooleanExpression isEqualsMiddleClass(Integer id) {
        if (id == null || id < 0) return null;
        return category.middleClassId.eq(id);
    }

    public BooleanExpression isEqualsSmallClass(Integer id) {
        if (id == null || id < 0) return null;
        return category.smallClassId.eq(id);
    }

    public BooleanExpression isEqualsDetailClass(Integer id) {
        if (id == null || id < 0) return null;
        return category.detailClassId.eq(id);
    }

    public BooleanExpression isEqualsBottomClass(Integer id) {
        if (id == null || id < 0) return null;
        return category.bottomClassId.eq(id);
    }

}
