package com.example.core.poi;

import com.example.core.category.Category;
import com.example.core.poi.dto.Poi;
import com.example.core.category.CategoryRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class POIService {

    private final POIRepository poiRepository;
    private final POIMapper poiMapper;
    private final CategoryRepository categoryRepository;
    private final GeometryFactory geometryFactory;
    public void createPOI(Poi poi, Integer categoryCode) {
        Category proxyCategory = categoryRepository.getReferenceById(categoryCode);
        poi.setCategory(proxyCategory);
        Point point = geometryFactory.createPoint(new Coordinate(poi.getLon(), poi.getLat()));
        poi.setCoordinates(point);
        poiRepository.save(poi);
    }

    public List<Poi> findByName(String name) {
        return poiRepository.findByName(name);
    }

    public List<Poi> findAllPOI() {
        return poiMapper.selectAll();
    }

    public void removePOI(Long id) {
        Poi referenceById = poiRepository.getReferenceById(id);
        poiRepository.delete(referenceById);
    }

    public Page<Poi> findByCurrentPosition(POISearchParam poiSearchParam) {
        Page<Poi> bySearchParam = poiRepository.findByQuerySearchParam(poiSearchParam);

        log.info("bySearchParam size {}",bySearchParam.getSize());
        return bySearchParam;
    }
}
