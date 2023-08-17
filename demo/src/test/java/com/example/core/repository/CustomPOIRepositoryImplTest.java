package com.example.core.repository;

import com.example.core.poi.Poi;
import com.example.core.poi.repository.POIRepository;
import com.example.core.poi.POISearchParam;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CustomPoiRepositoryImplTest {
    @Autowired
    POIRepository poiRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    GeometryFactory geometryFactory;
    @Test
    public void test1(){

        Query nativeQuery = entityManager.createNativeQuery("select * from POI where ST_within(coordinates,  st_geomfromtext('POLYGON((" +
                "126.72372610106636 36.976204779780645," +
                "127.54426748290231 36.976204779780645," +
                "127.54426748290231 37.865074892291375," +
                "126.72372610106636 37.865074892291375," +
                "126.72372610106636 36.976204779780645" +
                "))',4326))")
                .unwrap(org.hibernate.query.NativeQuery.class)
                ;


        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(126.72372610106636d ,36.976204779780645d);
        coordinates[1] = new Coordinate(127.54426748290231d, 36.976204779780645d);
        coordinates[2] = new Coordinate(127.54426748290231, 37.865074892291375);
        coordinates[3] = new Coordinate(126.72372610106636, 37.865074892291375);
        coordinates[4] = new Coordinate(126.72372610106636, 36.976204779780645);
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        polygon.setSRID(4326);
        List<Geometry> event = entityManager.createQuery(
                        "select e.coordinates " +
                                "from Poi e " +
                                "where within(e.coordinates, :window) = true", Geometry.class)
                .setParameter("window", polygon)
                .getResultList();
        System.out.println(event.size());
    }
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

        Page<Poi> bySearchParam = poiRepository.findByQuerySearchParam(poiSearchParam);

    }

}