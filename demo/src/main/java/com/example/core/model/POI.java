package com.example.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "poi")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class POI {
    @Id
    @SequenceGenerator(name = "poi_id_seq",
            sequenceName = "poi_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "poi_id_seq")
    private Long id;
    private String name;
    private String telNo;
    private String description;

    @Column(name = "coordinates", columnDefinition = "geometry(Point,4326)")
    private org.locationtech.jts.geom.Point coordinates;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private Category category;


    @Transient
    Double lon;
    @Transient
    Double lat;

    public void setCategory(Category _category) {
        this.category = _category;
        _category.getPoiList().add(this);
    }

    public void setCoordinates(Point point) {
        this.coordinates = point;
    }
}
