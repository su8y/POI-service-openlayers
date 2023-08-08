package com.example.core.category;

import com.example.core.poi.dto.Poi;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "category_code")
    private Integer categoryCode;
    @Column(name = "lclascd")
    private Integer largeClassId;
    @Column(name = "lclasdc")
    private String largeClassName;
    @Column(name = "mlsfccd")
    private Integer middleClassId;
    @Column(name = "mlsfcdc")
    private String middleClassName;
    @Column(name = "sclascd")
    private Integer smallClassId;
    @Column(name = "sclasdc")
    private String smallClassName;
    @Column(name = "dclascd")
    private Integer detailClassId;
    @Column(name = "dclasdc")
    private String detailClassName;
    @Column(name = "bclascd")
    private Integer bottomClassId;
    @Column(name = "bclasdc")
    private String bottomClassName;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category")
    @JsonIgnore
    @Builder.Default
    private List<Poi> poiList = new ArrayList<Poi>();

}
