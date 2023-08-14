package com.example.core.poi.dto;

import com.example.core.category.Category;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

@Data
public class POIResponseDto {
    private Long id;
    private String name;
    private String telNo;
    private String description;
    private Long categoryCode;

    private Coordinate coordinate;
    private Category category;

}
