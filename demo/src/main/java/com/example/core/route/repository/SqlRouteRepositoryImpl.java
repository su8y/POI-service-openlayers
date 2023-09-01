package com.example.core.route.repository;

import com.example.core.route.dto.ResponseRoute;
import com.example.core.route.dto.RouteRequest;
import com.example.core.route.dto.RouteSearchOption;
import com.example.core.route.dto.SaveRouteReqeust;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import ognl.BooleanExpression;
import org.geolatte.geom.codec.Wkt;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.hateoas.PagedModel;
import org.springframework.util.StringUtils;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import static com.example.core.auth.QMember.member;
import static com.example.core.route.QRoute.route;

@RequiredArgsConstructor
public class SqlRouteRepositoryImpl implements SqlRouteRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseRoute> findRouteBySearchOption(RouteSearchOption routeSearchOption) throws UserPrincipalNotFoundException {
        List<ResponseRoute> result = jpaQueryFactory.from(route)
                .select(Projections.fields(ResponseRoute.class,
                        route.id,
                        route.title,
                        route.member.id.as("memberId"),
                        route.description,
                        route.startPosition,
                        route.endPosition,
                        route.path,
                        route.waypoints,
                        route.json
                )).where(
                        conditionalRouteSearchOption(routeSearchOption)
                ).join(route.member, member)
                .limit(routeSearchOption.getPageable().getPageSize())
                .offset(routeSearchOption.getPageable().getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.from(route)
                .select(route.count()).where(
                        conditionalRouteSearchOption(routeSearchOption)
                );
        return PageableExecutionUtils.getPage(result, routeSearchOption.getPageable(), countQuery::fetchOne);
    }

    @Override
    public void update(SaveRouteReqeust request) {
        // create UpdateClause equals route id
        JPAUpdateClause update = jpaQueryFactory.update(route).where(route.id.eq(request.getId()));
        // add set condition
        if (StringUtils.hasText(request.getTitle()))
            update.set(route.title, request.getTitle());
        if (StringUtils.hasText(request.getDescription()))
            update.set(route.description, request.getDescription());
        // execute
        update.execute();
    }

    private BooleanBuilder conditionalRouteSearchOption(RouteSearchOption routeSearchOption) throws UserPrincipalNotFoundException {
        BooleanBuilder builder = new BooleanBuilder();
        if (routeSearchOption.getMemberId() != null && routeSearchOption.getMemberId().isBlank())
            throw new UserPrincipalNotFoundException("user");
        builder.and(route.member.id.like(routeSearchOption.getMemberId()));
        if (StringUtils.hasText(routeSearchOption.getInputText())) {
            builder.and(route.title.contains(routeSearchOption.getInputText()));
        }
        return builder;
    }
}
