package com.example.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@javax.persistence.Embeddable
@NoArgsConstructor
public class Point {
    private Double lon;

    private Double lat;

    public Point(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
