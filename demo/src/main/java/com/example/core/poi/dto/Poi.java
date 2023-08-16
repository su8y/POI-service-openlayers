package com.example.core.poi.dto;

import com.example.core.auth.Member;
import com.example.core.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Column(name = "coordinates", columnDefinition = "geometry(point,4326)")
    @JsonIgnore
    private Point coordinates;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private Category category;


    Double lon;
    Double lat;

    public void setMember(Member member){
        this.member = member;
    }

    public void setCategory(Category _category) {
        this.category = _category;
        category.getPoiList().add(this);
    }

    public void setCoordinates(Point point) {
        this.coordinates = point;
    }

    public static Poi factory(POIRequestDto dto) {
        return Poi.builder()
                .description(dto.getDescription())
                .name(dto.getName())
                .telNo(dto.getTelNo())
                .lon(dto.getCoordinate()[0])
                .lat(dto.getCoordinate()[1])
                .build();
    }
}
