package com.example.core.repository;

import com.example.core.model.Category;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.core.model.QCategory.category;


@Component
@RequiredArgsConstructor
public class QueryCategoryRepositoryImpl implements QueryCategoryRepository {
    private final JPAQueryFactory queryFactory;


    public List<?> findCategoryByCurrentValue(Category currentValue) {
        JPAQuery<?> from = queryFactory.from(category);
        if (currentValue.getLargeClassId() == null) {
            from = from.select(Projections.fields(Category.class,category.largeClassId, category.largeClassName));
        } else if (currentValue.getMiddleClassId() == null) {
            from = from.select(Projections.fields(Category.class,category.middleClassId, category.middleClassName));
        } else if (currentValue.getSmallClassId() == null) {
            from = from.select(Projections.fields(Category.class,category.smallClassId, category.smallClassName));
        } else if (currentValue.getDetailClassId() == null) {
            from = from.select(Projections.fields(Category.class,category.detailClassId, category.detailClassName));
        } else if (currentValue.getBottomClassId() == null) {
            from = from.select(Projections.fields(Category.class,category.bottomClassId, category.bottomClassName));
        }
        JPAQuery<?> where = from.where(
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
