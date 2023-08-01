package com.example.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Waypoint {
    @SequenceGenerator(name = "waypoint_id_seq",
            sequenceName = "waypoint_id_seq",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "waypeoint_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "start_position", columnDefinition = "geometry(Point,4326)")
    private Point startPosition;

}
