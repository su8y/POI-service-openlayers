package com.example.core.route.controller;

import com.example.core.route.Route;
import com.example.core.route.dto.ResponseRoute;
import com.example.core.route.dto.RouteSearchOption;
import com.example.core.route.dto.SaveRouteReqeust;
import com.example.core.route.repository.RouteMapper;
import com.example.core.route.service.RouteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {
    private final ObjectMapper objectMapper;
    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @GetMapping(value = "{routeId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findDetail(
            @PathVariable(name = "routeId") Long routeId,
            Authentication authentication
    ) throws JsonProcessingException {
        Route routeById = routeService.findRouteById(routeId, authentication.getName());

        return ResponseEntity.ok(objectMapper.writeValueAsString(routeById));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> findRoutes(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "inputText", required = false) String inputText,
            Authentication authentication
    ) throws JsonProcessingException {
        RouteSearchOption routeSearchOption = RouteSearchOption.toRouteSearchOption(pageable, authentication.getName(), inputText);
        Page<ResponseRoute> route = routeService.getRoutes(routeSearchOption);

        return ResponseEntity.ok(objectMapper.writeValueAsString(route));
    }

    /**
     * @param request        <p>
     *                       "title" : "{your title}<br/>
     *                       "description" : "{your description}"<br/>
     *                       "path" : { "type" :"LineString", "coordinates": [[x1,y1], ...] },<br/>
     *                       "memberId": "{loginUserId}",<br/>
     *                       "waypoints": { "type" : "MultiPolygon", "coordinates": [[x1,y1], ...]<br/>
     *                       </P>
     * @param files
     * @param authentication
     * @return
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> saveRoute(
            @RequestPart(name = "request") SaveRouteReqeust request,
            @RequestPart(name = "files") List<MultipartFile> files,
            Authentication authentication
    ) {
        System.out.println(request.getJson());
        routeService.save(request, authentication.getName(), files);

        return ResponseEntity.ok(request.getTitle());
    }

    @DeleteMapping(value = "/{routeId}")
    public ResponseEntity<?> deleteRoute(@PathVariable(name = "routeId") List<Long> routeId, Authentication authentication) {
        routeService.deleteByUser(routeId, authentication.getName());

        return ResponseEntity.ok("삭제 완료");
    }

    @PatchMapping(value = "/{routeId}")
    public ResponseEntity<?> editRoute(
            @RequestPart("request") SaveRouteReqeust request,
            @RequestPart("deleteFile") List<String> deleteFileNameList,
            @RequestPart("addFile") List<MultipartFile> addFileList
    ) throws IOException {
        routeService.update(request, addFileList, deleteFileNameList);
        return ResponseEntity.ok(request.getId() + "변경성공");
    }


}