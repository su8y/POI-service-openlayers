package com.example.core.repository;

import com.example.core.model.POI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POIRepository extends JpaRepository<POI,Long> {
    List<POI> findByName(String name);

}
