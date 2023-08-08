package com.example.core.poi;

import com.example.core.category.Category;
import com.example.core.category.QCategory;
import com.example.core.poi.dto.Poi;
import com.example.core.poi.dto.QPoi;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.core.category.QCategory.*;
import static com.example.core.poi.dto.QPoi.*;


@RequiredArgsConstructor
public class QueryPOIRepositoryImpl implements QueryPOIRepository {
    private final JPAQueryFactory factory;

    @Override
    public Page<Poi> findByQuerySearchParam(POISearchParam searchParam) {
        String inputText = searchParam.getInputText();
        Pageable pageable = searchParam.getPageable();
        Polygon polygon = searchParam.getPolygon();
        Category selectedCategory = searchParam.getSelectedCategory();
        JPAQuery<?> where =
                factory.from(poi)
                        .select(poi)
                        .leftJoin(poi.category, category)
                        .where(withinBbox(polygon),
                                equalsInputText(inputText),
                                selectedCategory(selectedCategory)
                        );

        List<Poi> fetch = (List<Poi>) where.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> countQuery = where.select(poi.count());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    public BooleanExpression equalsInputText(String inputText) {
        if (!StringUtils.hasText(inputText)) return null;
        return poi.name.like(inputText);
    }

    public BooleanExpression withinBbox(Polygon polygon) {
        return poi.coordinates.within(polygon);
    }

    public BooleanExpression selectedCategory(Category selectedCategory) {
        if (selectedCategory.getLargeClassId() == null) return null;

        BooleanExpression booleanExpression = category.largeClassId.eq(selectedCategory.getLargeClassId());
        if (selectedCategory.getMiddleClassId() != null)
            booleanExpression = booleanExpression.and(category.middleClassId.eq(selectedCategory.getMiddleClassId()));
        if (selectedCategory.getSmallClassId() != null)
            booleanExpression = booleanExpression.and(category.smallClassId.eq(selectedCategory.getSmallClassId()));
        if (selectedCategory.getBottomClassId() != null)
            booleanExpression = booleanExpression.and(category.detailClassId.eq(selectedCategory.getDetailClassId()));
        if (selectedCategory.getBottomClassId() != null)
            booleanExpression = booleanExpression.and(category.bottomClassId.eq(selectedCategory.getBottomClassId()));
        return booleanExpression;
    }
}
