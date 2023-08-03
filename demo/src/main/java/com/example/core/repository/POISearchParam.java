package com.example.core.repository;

import com.example.core.model.Category;
import com.example.core.model.common.Space;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POISearchParam {
    private List<List<List<Double>>> currentPosition;
    private Category selectedCategory;
    private Pageable pageable;
    private Polygon polygon;

}
