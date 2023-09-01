package com.example.core.route.service;

import com.example.core.auth.Member;
import com.example.core.auth.MemberRepository;
import com.example.core.file.FileRepository;
import com.example.core.file.FileUtil;
import com.example.core.file.UploadImage;
import com.example.core.file.common.TargetType;
import com.example.core.route.Route;
import com.example.core.route.dto.ResponseRoute;
import com.example.core.route.dto.RouteSearchOption;
import com.example.core.route.dto.SaveRouteReqeust;
import com.example.core.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {
    private final RouteRepository routeRepository;
    private final GeometryFactory geometryFactory;
    private final MemberRepository memberRepository;
    private final FileUtil fileUtil;
    private final FileRepository fileRepository;

    public void save(SaveRouteReqeust saveRouteReqeust, String memberId, List<MultipartFile> files) {
        Member referenceMember = memberRepository.getReferenceById(memberId);
        Coordinate[] coordinates = saveRouteReqeust.getPath().getCoordinates();
        // 시작점 , 도착점
        Point startPosition = geometryFactory.createPoint(coordinates[0]);
        Point endPosition = geometryFactory.createPoint(coordinates[coordinates.length - 1]);
        MultiPoint multiPoint = geometryFactory.createMultiPoint(saveRouteReqeust.getWaypoints().toArray(new Coordinate[0]));
        Route route = Route.builderFromRequest(saveRouteReqeust)
                .waypoints(multiPoint)
                .member(referenceMember)
                .startPosition(startPosition)
                .endPosition(endPosition)
                .build();
        //route 영속화
        routeRepository.save(route);
        try {
            List<UploadImage> uploadImages = fileUtil.uploadImages(files);
            uploadImages.stream().forEach(image -> {
                image.setTargetId(route.getId());
                image.setType(TargetType.ROUTE);
                fileRepository.save(image);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<ResponseRoute> getRoutes(RouteSearchOption routeSearchOption) {
        try {
            return routeRepository.findRouteBySearchOption(routeSearchOption);
        } catch (UserPrincipalNotFoundException exception) {
            throw new RuntimeException("멤버를 찾을수없습니다");
        }
    }

    public void update(SaveRouteReqeust request, List<MultipartFile> addFileList, List<String> deleteFileNameList) throws IOException {
        if (addFileList != null && addFileList.size() > 0) {
            //Save file
            List<UploadImage> uploadImages = fileUtil.uploadImages(addFileList);
            // set Target Id & Type
            uploadImages.forEach(image -> {
                image.setTargetId(request.getId());
                image.setType(TargetType.ROUTE);
            });
            // Save Database
            fileRepository.saveAll(uploadImages);
        }
        if (deleteFileNameList != null && deleteFileNameList.size() > 0) {
            fileRepository.deleteAllById(deleteFileNameList);
            // 실제 삭제는 배치프로세스로 진행한다.
        }
        routeRepository.update(request);
    }

    public void deleteByUser(List<Long> routeIds, String memberId) {
        routeIds.forEach(routeId -> {
            routeRepository.deleteByIdAndMemberId(routeId, memberId);
            List<UploadImage> uploadImagesByTarget = fileRepository.findUploadImagesByTarget(routeId, TargetType.ROUTE);
            fileRepository.deleteAll(uploadImagesByTarget);
        });
    }

    public Route findRouteById(Long routeId, String memberId) {
        Optional<Route> byIdAndMemberId = routeRepository.findByIdAndMemberId(routeId, memberId);
        return byIdAndMemberId.orElseThrow();
    }
}
