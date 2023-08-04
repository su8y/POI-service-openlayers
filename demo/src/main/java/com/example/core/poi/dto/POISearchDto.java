package com.example.core.poi.dto;

import com.example.core.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class POISearchDto {
    private String inputText;
    private Double[] leftTop;

    private Double[] rightBottom;
    private Category category;
}
