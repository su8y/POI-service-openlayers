package com.example.core.poi;

import com.example.core.poi.dto.Poi;
import org.springframework.data.domain.Page;

public interface QueryPOIRepository {
    Page<Poi> findByQuerySearchParam(POISearchParam searchParam);
}
