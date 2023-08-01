package com.example.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
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
    @Builder.Default
    private List<POI> poiList = new ArrayList<POI>();

}