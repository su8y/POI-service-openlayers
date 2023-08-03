package com.example.core.repository;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomPOIRepositoryImplTest {
    @Autowired
    POIRepository poiRepository;
    @Test
    public void test(){
        POISearchParam poiSearchParam = new POISearchParam();

        List<List<List<Double>>> currentPosition = new ArrayList<>(new ArrayList<>(new ArrayList<>()));
        currentPosition.add(new ArrayList<>());
        currentPosition.get(0).add(new ArrayList<>());
        currentPosition.get(0).add(new ArrayList<>());
        currentPosition.get(0).add(new ArrayList<>());
        currentPosition.get(0).add(new ArrayList<>());
        currentPosition.get(0).add(new ArrayList<>());
        currentPosition.get(0).get(0).add(127.0981807037416);
        currentPosition.get(0).get(0).add(37.50374848525439);
        currentPosition.get(0).get(1).add(127.11812560997635);
        currentPosition.get(0).get(1).add(37.50374848525439);
        currentPosition.get(0).get(2).add(127.11812560997635);
        currentPosition.get(0).get(2).add(37.516310152239384);
        currentPosition.get(0).get(3).add(127.0981807037416);
        currentPosition.get(0).get(3).add(37.516310152239384);
        currentPosition.get(0).get(4).add(127.0981807037416);
        currentPosition.get(0).get(4).add(37.50374848525439);
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
        poiSearchParam.setPolygon(polygon);

//        searchParam.setSpace(new Space("Polygon",po) );

        poiRepository.findBySearchParam(poiSearchParam);

    }

}