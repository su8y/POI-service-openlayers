package com.example.core.poi.dto;

import com.example.core.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.locationtech.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "poi")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Poi {
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

    @Column(name = "coordinates", columnDefinition = "geometry(point,4326)")
    @JsonIgnore
    private Point coordinates;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_code")
    private Category category;


    Double lon;
    Double lat;

    public void setCategory(Category _category) {
        this.category = _category;
        category.getPoiList().add(this);
    }

    public void setCoordinates(Point point) {
        this.coordinates = point;
    }
}
