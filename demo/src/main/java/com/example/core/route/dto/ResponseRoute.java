package com.example.core.route.dto;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResponseRoute {
    private String resultCode;
    private String message;
    private LocalDateTime currentDateTime;
    Long id;
    String title;
    String description;
    String memberId;
    @JsonSerialize(using = GeometrySerializer.class)
    Point startPosition;
    @JsonSerialize(using = GeometrySerializer.class)
    Point endPosition;
    @JsonSerialize(using = GeometrySerializer.class)
    LineString path;
    @JsonSerialize(using = GeometrySerializer.class)
    MultiPoint waypoints;
    String json;
}
