package com.example.core.poi.dto;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

@Data
public class POIResponseDto {
    private Long id;
    private String name;
    private Coordinate coordinate;
    private Long categoryCode;

    private String telNo;
    private String description;
}
