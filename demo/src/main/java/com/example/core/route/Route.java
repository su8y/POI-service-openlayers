package com.example.core.route;


import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.example.core.auth.Member;
import com.example.core.route.dto.SaveRouteReqeust;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "route")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    @SequenceGenerator(name = "route_id_seq",
            sequenceName = "route_id_seq",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_id_seq")
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
//    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(name = "start_position", columnDefinition = "geometry(Point,4326)")
    @JsonSerialize(using = GeometrySerializer.class)
    private Point startPosition;
    @Column(name = "end_position", columnDefinition = "geometry(Point,4326)")
    @JsonSerialize(using = GeometrySerializer.class)
    private Point endPosition;

    private String description;


    @Column(name = "path",columnDefinition = "geometry(LineString,4326)")
    @JsonSerialize(using = GeometrySerializer.class)
    private LineString path;

    @Column(name = "waypoints",columnDefinition = "geometry(MultiPoint,4326)")
    @JsonSerialize(using = GeometrySerializer.class)
    private MultiPoint waypoints;
    @Column(name="json",columnDefinition = "TEXT")
    private String json;



    public static RouteBuilder builderFromRequest(SaveRouteReqeust route){
        return Route.builder().path(route.getPath())
                .title(route.getTitle())
                .description(route.getDescription())
                .json(route.getJson())
                ;
    }
}
