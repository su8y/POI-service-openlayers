package com.example.core.poi;

import com.example.core.poi.dto.Poi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface POIMapper {
    List<Poi> selectAll();
}