package com.example.core;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
class CoreApplicationTests {


    @Test
    @DisplayName("LocalDatabase is On Test")
    void DBConnectionTest(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project", "postgres", "postgres");
            Assertions.assertThat(connection).isNotNull();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @DisplayName("JTS TEST")
    void jtsTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(1.2345678, 2.3456789));
        String geojson = mapper.writeValueAsString(point);
        System.out.println(geojson);

        Point point1 = mapper.readValue(geojson, Point.class);
        System.out.println(point1 != null);
    }

}
