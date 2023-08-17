package com.example.core.poi.dto;

import com.example.core.auth.Member;
import com.example.core.category.Category;
import com.example.core.file.UploadImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PoiDTO {
    private Long id;
    private String name;
    private String telNo;
    private String description;
    private Member member;
    @JsonIgnore
    private Point coordinates;
    private Category category;
    private Double lon;
    private Double lat;

    @Builder.Default
    private List<UploadImage> images = new ArrayList<>();

}
