package com.example.core.poi.dto;

import com.example.core.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class POIRequestDto {
    @NotEmpty
    String name;
    @NotEmpty
    String telNo;
    String description;
    Double lon;
    Double lat;

    Category category;
}
