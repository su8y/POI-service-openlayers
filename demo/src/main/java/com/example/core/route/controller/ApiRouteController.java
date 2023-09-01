package com.example.core.route.controller;

import com.example.core.naver.service.DirectionService;
import com.example.core.route.dto.RouteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("api/route")
@RestController
@RequiredArgsConstructor
public class ApiRouteController {
    private final DirectionService directionService;
    private final ResourceLoader resourceLoader;

    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object findRoute(
            @ModelAttribute @Validated RouteRequest routeRequest
    ) throws IOException {
        return directionService.requestRouteByRequestRoute(routeRequest);

        /**테스트를 위한 json File 데이터 */
//        Resource resource = new ClassPathResource("static/messages/routeResponseTest.json");
//        if(resource.getFile().isFile()){
//            System.out.println("File");
//        }
//        if(resource.isReadable()){
//            System.out.println("FILE2");
//        }
//        return new ObjectMapper().readValue(resource.getFile(), Object.class);
    }
}
