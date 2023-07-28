package com.example.core.repository;

import com.example.core.model.POI;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface POIMapper {
    List<POI> selectAll();
}
