package com.example.core.poi.repository;

import com.example.core.category.Category;
import com.example.core.poi.POISearchParam;
import com.example.core.poi.dto.POIResponseDto;
import com.example.core.poi.dto.Poi;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.core.category.QCategory.*;
import static com.example.core.poi.dto.QPoi.*;
import static com.example.core.auth.QMember.*;


@RequiredArgsConstructor
public class QueryPOIRepositoryImpl implements QueryPOIRepository {
    private final JPAQueryFactory factory;

    @Override
    public Page<Poi> findByQuerySearchParam(POISearchParam searchParam) {
        System.out.println("findByQuerySearchParam");
        String inputText = searchParam.getInputText();
        Pageable pageable = searchParam.getPageable();
        Polygon polygon = searchParam.getPolygon();
        Category selectedCategory = searchParam.getSelectedCategory();
        JPAQuery<?> where =
                factory.from(poi)
                        .select(poi)
                        .leftJoin(poi.category, category)
                        .fetchJoin()
                        .where(withinBbox(polygon),
                                equalsInputText(inputText),
                                selectedCategory(selectedCategory)
                        );

        List<Poi> fetch = (List<Poi>) where.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> countQuery = factory.from(poi)
                        .select(poi.count())
                        .leftJoin(poi.category, category)
                        .where(withinBbox(polygon),
                                equalsInputText(inputText),
                                selectedCategory(selectedCategory)
                        );


        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<POIResponseDto> findByManagePoiSearchParam(POISearchParam searchParam) {
        String memberId = searchParam.getMemberId();
        Category selectedCategory = searchParam.getSelectedCategory();
        Pageable pageable = searchParam.getPageable();

        JPAQuery<POIResponseDto> where = factory.from(poi)
                .select(Projections.fields(POIResponseDto.class,
                        poi.id,
                        poi.name,
                        poi.coordinates,
                        poi.description,
                        poi.description,
                        poi.telNo,
                        poi.category
                ))
                .leftJoin(poi.member, member)
                .leftJoin(poi.category, category)
                .where(
                        selectedCategory(selectedCategory),
                        equalsMemberId(memberId)
                );

        List<POIResponseDto> fetch = where.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        JPAQuery<Long> countQuery = factory.from(poi)
                .select(poi.count())
                .leftJoin(poi.member, member)
                .leftJoin(poi.category, category)
                .where(
                        selectedCategory(selectedCategory),
                        equalsMemberId(memberId)
                );

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    public BooleanExpression equalsMemberId(String memberId) {
        if (memberId == null || !StringUtils.hasText(memberId)) return null;
        return poi.member.id.like(memberId);
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
