package com.example.core.poi;

import com.example.core.category.Category;
import lombok.Builder;
import org.locationtech.jts.geom.Polygon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class POISearchParam {
    private String inputText;
    private Category selectedCategory;
    private Pageable pageable;
    private Polygon polygon;
    private String memberId;
}
