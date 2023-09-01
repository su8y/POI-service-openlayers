package com.example.core.jackson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class JtsTest {
    @Test
    @DisplayName("JTS TEST")
    void jtsTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        GeometryFactory gf = new GeometryFactory();
        Coordinate coordinate = new Coordinate();
        coordinate.x=1.2345;
        coordinate.y=1.2345;
        String s = mapper.writeValueAsString(coordinate);
        System.out.println(s);

        Point point = gf.createPoint(new Coordinate(1.2345678, 2.3456789));
        String geojson = mapper.writeValueAsString(point);
        System.out.println(geojson);

        Point point1 = mapper.readValue(geojson, Point.class);
        System.out.println(point1 != null);
    }
}
