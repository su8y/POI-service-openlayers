package com.example.core.route.dto;

import com.example.core.route.Route;
import com.example.core.route.args.LineStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * request :
 * "title" : "{your title},
 * "description" : "{your description}",
 * "path" : {
 * "type" :"LineString",
 * "coordinates": [[x1,y1], ...]
 * },
 * "memberId": "{loginUserId}",
 * "waypoints": {
 * "type" : "MultiPolygon",
 * "coordinates": [[x1,y1], ...]
 * }
 * }
 */
@Data
public class SaveRouteReqeust {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @JsonDeserialize(using = LineStringDeserializer.class)
    private LineString path;
    private LocalDateTime careteAt;
    private String memberId;
    private List<Coordinate> waypoints;
    private String json;

    public Route toEntity() {
        return Route.builder()
                .id(id)
                .title(title)
                .build();

    }
}
