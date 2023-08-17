package com.example.core.poi.repository;

import com.example.core.poi.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POIRepository extends JpaRepository<Poi, Long>, QueryPOIRepository {
    List<Poi> findByName(String name);



}
