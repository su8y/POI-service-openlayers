package com.example.core.poi.repository;

import com.example.core.poi.POISearchParam;
import com.example.core.poi.dto.POIResponseDto;
import com.example.core.poi.dto.Poi;
import org.springframework.data.domain.Page;

public interface QueryPOIRepository {
    Page<Poi> findByQuerySearchParam(POISearchParam searchParam);
    Page<POIResponseDto> findByManagePoiSearchParam(POISearchParam searchParam);
}
