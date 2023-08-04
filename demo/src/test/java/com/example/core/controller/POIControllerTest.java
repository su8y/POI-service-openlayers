package com.example.core.controller;

import com.example.core.poi.POIController;
import com.example.core.poi.dto.POIRequestDto;
import com.example.core.model.Category;
import com.example.core.poi.dto.POISearchDto;
import com.example.core.poi.util.POIMethodArgumentsResolver;
import com.example.core.service.POIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
class POIControllerTest {
    private static MockMvc mvc;
    private static ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private POIController poiController;
    @Mock
    private POIService poiService;

    @BeforeAll
    public static void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new POIController(null))
                .setCustomArgumentResolvers(new POIMethodArgumentsResolver(objectMapper))
                .build();
    }

    @Test
    @DisplayName("POI 조회 파라미터 바인딩 테스트")
    public void findPoi() throws Exception {
        POISearchDto searchDto = new POISearchDto();
        searchDto.setInputText("hello");
        searchDto.setLeftTop(new Double[]{222.22222, 11111.111111});
        mvc.perform(get("/pois")
                        .param("searchDto", objectMapper.writeValueAsString(searchDto))
                )
                .andDo(print());

    }

    @Test
    @DisplayName("Object Mapper Test")
    public void om() throws JsonProcessingException {
        POISearchDto searchDto = new POISearchDto();
        searchDto.setInputText("hello");
        searchDto.setLeftTop(new Double[]{222.22222, 11111.111111});

        String s = objectMapper.writeValueAsString(searchDto);
        System.out.println("s = " + s);

        POISearchDto searchDto1 = objectMapper.readValue(s, POISearchDto.class);
        System.out.printf("%s", searchDto1.getInputText());
        System.out.println("searchDto1.getLeftTop() = " + searchDto1.getLeftTop()[0]);

    }

    @Test
    @DisplayName("POI 저장 테스트")
    public void createPoi() throws Exception {
//        //Mock파일생성
        MockMultipartFile images =
                new MockMultipartFile("images", /*name*/ "test.png", /*originalFilename*/ "image/jpeg", "<<jpeg data>>".getBytes());
        //given
        Category category = Category.builder()
                .largeClassId(1).middleClassId(1).smallClassId(1)
                .detailClassId(1).bottomClassId(18)
                .build();
        POIRequestDto dto =
                POIRequestDto.builder()
                        .name("qotndk")
                        .telNo("01000000000")
                        .description("반갑습니다")
                        .lon(1.1d)
                        .lat(0.1d)
                        .category(category)
                        .build();
        String requestToString = objectMapper.writeValueAsString(dto);
        mvc.perform(multipart("/pois")
                        .file(images)
                        .file(new MockMultipartFile("dto", "", "application/json", requestToString.getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("qotndk1"))
                .andDo(print());
    }
}