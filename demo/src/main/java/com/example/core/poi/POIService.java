package com.example.core.poi;

import com.example.core.category.Category;
import com.example.core.file.FileService;
import com.example.core.file.UploadImage;
import com.example.core.poi.dto.Poi;
import com.example.core.category.CategoryRepository;
import org.geolatte.geom.V;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class POIService {
    private final FileService fileService;
    private final POIRepository poiRepository;
    private final POIMapper poiMapper;
    private final CategoryRepository categoryRepository;
    private final GeometryFactory geometryFactory;

    public void createPOI(Poi poi, Integer categoryCode, List<MultipartFile> images) {
        //Add category
        Category proxyCategory = categoryRepository.getReferenceById(categoryCode);
        poi.setCategory(proxyCategory);

        //set coordinate
        Point point = geometryFactory.createPoint(new Coordinate(poi.getLon(), poi.getLat()));
        point.setSRID(4326);
        poi.setCoordinates(point);

        Poi save = poiRepository.save(poi);
        //add upload-image
        try {
            List<UploadImage> uploadImages = fileService.uploadImages(images);
            uploadImages.forEach(uploadImage -> {
                uploadImage.setTargetId(save.getId());
                uploadImage.setType("POI");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Poi> findByName(String name) {
        return poiRepository.findByName(name);
    }

    public List<Poi> findAllPoi() {
        return poiMapper.selectAll();
    }

    public void removePOI(Long id) {
        Poi referenceById = poiRepository.getReferenceById(id);
        poiRepository.delete(referenceById);
    }

    public Page<Poi> findByCurrentPosition(POISearchParam poiSearchParam) {
        Page<Poi> bySearchParam = poiRepository.findByQuerySearchParam(poiSearchParam);
        return bySearchParam;
    }
}
