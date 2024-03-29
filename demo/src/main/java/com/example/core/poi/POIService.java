package com.example.core.poi;

import com.example.core.auth.Member;
import com.example.core.auth.MemberRepository;
import com.example.core.category.Category;
import com.example.core.file.FileRepository;
import com.example.core.file.FileUtil;
import com.example.core.file.UploadImage;
import com.example.core.file.common.TargetType;
import com.example.core.poi.dto.POIResponseDto;
import com.example.core.category.CategoryRepository;
import com.example.core.poi.dto.PoiDTO;
import com.example.core.poi.repository.POIRepository;
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
    private final FileUtil fileUtil;
    private final POIRepository poiRepository;
    private final CategoryRepository categoryRepository;
    private final GeometryFactory geometryFactory;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    public void createPOI(Poi poi, Integer categoryCode, List<MultipartFile> images, String memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        poi.setMember(member);

        Category proxyCategory = categoryRepository.getReferenceById(categoryCode);
        poi.setCategory(proxyCategory);

        //set coordinate
        Point point = geometryFactory.createPoint(new Coordinate(poi.getLon(), poi.getLat()));
        point.setSRID(4326);
        poi.setCoordinates(point);

        Poi save = poiRepository.save(poi);
        //add upload-image
        try {
            List<UploadImage> uploadImages = fileUtil.uploadImages(images);
            uploadImages.forEach(uploadImage -> {
                uploadImage.setTargetId(save.getId());
                uploadImage.setType(TargetType.POI);
            });
            fileRepository.saveAll(uploadImages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Poi> findByName(String name) {
        return poiRepository.findByName(name);
    }

    public void removePOI(List<Long> id) {
        poiRepository.deleteAllById(id);
    }

    public Page<Poi> findByCurrentPosition(POISearchParam poiSearchParam) {
        Page<Poi> bySearchParam = poiRepository.findByQuerySearchParam(poiSearchParam);
        return bySearchParam;
    }


    public Page<POIResponseDto> getManagePoiList(POISearchParam poiSearchParam) {
        return poiRepository.findByManagePoiSearchParam(poiSearchParam);
    }

    public PoiDTO getDetailPoi(Long id) {
        Poi poi = poiRepository.findById(id).orElseThrow();

        PoiDTO dto = poi.toDTO();

        List<UploadImage> uploadImagesByTarget = fileRepository.findUploadImagesByTarget(dto.getId(), TargetType.POI);
        dto.setImages(uploadImagesByTarget);


        return dto;
    }

}
