package com.example.core.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PolygonArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Polygon.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String parameterName = parameter.getParameterName();

        List<List<List<Double>>> currentPosition = (List<List<List<Double>>>) nativeRequest.getAttribute(parameterName);

        GeometryFactory geometryFactory = new GeometryFactory();
        System.out.println("parameterName = " + parameterName);
        System.out.println("currentPosition.get(0).get(0).size() = " + currentPosition.get(0).get(0).size());

        // 좌표를 Point 객체로 변환
        List<Coordinate> pointCoordinates = new ArrayList<>();
        for (List<List<Double>> ringCoordinates : currentPosition) {
            for (List<Double> coordinate : ringCoordinates) {
                double x = coordinate.get(0);
                double y = coordinate.get(1);
                pointCoordinates.add(new Coordinate(x, y));
            }
        }

        // Point 객체들을 LinearRing으로 변환
        LinearRing shell = geometryFactory.createLinearRing(pointCoordinates.toArray(new Coordinate[0]));
        // Polygon 객체 생성
        return geometryFactory.createPolygon(shell, null);
    }
}
