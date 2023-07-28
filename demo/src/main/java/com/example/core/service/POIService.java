package com.example.core.service;

import com.example.core.model.Category;
import com.example.core.model.POI;
import com.example.core.repository.CategoryRepository;
import com.example.core.repository.POIMapper;
import com.example.core.repository.POIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

    public void createPOI(POI poi, Integer categoryCode) {
        Category proxyCategory = categoryRepository.getReferenceById(categoryCode);
        poi.setCategory(proxyCategory);
        Point point = geometryFactory.createPoint(new Coordinate(poi.getLon(), poi.getLat()));
        poi.setCoordinates(point);
        poiRepository.save(poi);

    }

    public List<POI> findByName(String name) {
        return poiRepository.findByName(name);
    }

    public List<POI> findAllPOI() {
        return poiMapper.selectAll();
    }
    public void removePOI(Long id){
        POI referenceById = poiRepository.getReferenceById(id);
        poiRepository.delete(referenceById);
    }

}
