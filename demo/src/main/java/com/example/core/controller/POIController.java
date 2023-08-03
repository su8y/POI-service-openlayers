package com.example.core.controller;

import com.example.core.model.Category;
import com.example.core.model.POI;
import com.example.core.model.common.Space;
import com.example.core.repository.POISearchParam;
import com.example.core.service.POIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/pois")
@RequiredArgsConstructor
public class POIController {
    private final POIService poiService;

    @GetMapping
    public Page<POI> getList(
            @RequestParam("currentPosition") List<List<List<Double>>> currentPosition
//            Category currentCategoryValue,
//            @PageableDefault Pageable page
    ) {
        GeometryFactory geometryFactory = new GeometryFactory();

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
        Polygon polygon = geometryFactory.createPolygon(shell, null);
//        Page<POI> poiList = poiService.findByCurrentPosition(polygon, currentCategoryValue, page);

        return null;
    }

    @PostMapping
    public String getList(@RequestBody(required = true) POISearchParam searchParam) {
//        List<List<List<Double>>> currentPosition = searchParam.getCurrentPosition();
//
//        GeometryFactory geometryFactory = new GeometryFactory();
//        // 좌표를 Point 객체로 변환
//        List<Coordinate> pointCoordinates = new ArrayList<>();
//        for (List<List<Double>> ringCoordinates : currentPosition) {
//            for (List<Double> coordinate : ringCoordinates) {
//                double x = coordinate.get(0);
//                double y = coordinate.get(1);
//                pointCoordinates.add(new Coordinate(x, y));
//            }
//        }
//
//        // Point 객체들을 LinearRing으로 변환
//        LinearRing shell = geometryFactory.createLinearRing(pointCoordinates.toArray(new Coordinate[0]));
//        // Polygon 객체 생성
//        Polygon polygon = geometryFactory.createPolygon(shell, null);
//        searchParam.setSpace(new Space("Polygon",po) );

        return "string";
    }
}
