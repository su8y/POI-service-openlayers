package com.example.core.poi.repository;

import com.example.core.poi.dto.Poi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POIRepository extends JpaRepository<Poi, Long>, QueryPOIRepository {
    List<Poi> findByName(String name);



}
