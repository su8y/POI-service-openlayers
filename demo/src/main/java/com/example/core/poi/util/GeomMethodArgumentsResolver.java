package com.example.core.poi.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeomMethodArgumentsResolver implements HandlerMethodArgumentResolver {
    private final GeometryFactory geometryFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GeomArgsResolve.class) || parameter.getParameterType().equals(Polygon.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String parameter1 = req.getParameter(parameter.getParameterName());
        if (parameter1 == null) throw new IllegalArgumentException();
        log.info(parameter1);

        String[] split = parameter1.split(",");

        org.locationtech.jts.geom.Coordinate[] coordinates = new Coordinate[split.length / 2];

        for (int i = 0; i < split.length / 2; i++) {
            Double lon = Double.valueOf(split[i * 2]);
            Double lat = Double.valueOf(split[(i * 2) + 1]);
            org.locationtech.jts.geom.Coordinate coordinate = new org.locationtech.jts.geom.Coordinate(lon, lat);
            coordinates[i] = coordinate;
        }
        org.locationtech.jts.geom.Polygon polygon = geometryFactory.createPolygon(coordinates);
        polygon.setSRID(4326);

        return polygon;
    }
}
