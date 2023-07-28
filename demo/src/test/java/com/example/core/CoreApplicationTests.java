package com.example.core;

import com.example.core.model.POI;
import com.example.core.repository.POIMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class CoreApplicationTests {
    @Autowired
    POIMapper poiRepository;

    @Test
    void contextLoads() {
        List<POI> books = poiRepository.selectAll();
        Assertions.assertThat(books).isNotEmpty();
    }

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

}
