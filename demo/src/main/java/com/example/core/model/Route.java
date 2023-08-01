package com.example.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "start_position", columnDefinition = "geometry(Point,4326)")

    private Point startPosition;
    @Column(name = "end_position", columnDefinition = "geometry(Point,4326)")
    private Point endPosition;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Waypoint> waypointList = new ArrayList<>();
}
