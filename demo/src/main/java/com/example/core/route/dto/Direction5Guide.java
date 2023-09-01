package com.example.core.route.dto;

import com.example.core.route.model.GuideCode;
import lombok.Data;

@Data
public class Direction5Guide {
    Long pointIndex;
    GuideCode type;
    String instructions;
    Integer distance;
    Long duration;
}
