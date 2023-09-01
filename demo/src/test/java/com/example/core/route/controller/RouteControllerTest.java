package com.example.core.route.controller;

import com.example.core.naver.service.DirectionService;
import com.example.core.poi.util.GeomMethodArgumentsResolver;
import com.example.core.route.dto.SaveRouteReqeust;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@ExtendWith({MockitoExtension.class})
//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RouteController.class)
class RouteControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private RouteController routeController;
    @MockBean
    private DirectionService directionService;
    @MockBean
    private  GeomMethodArgumentsResolver geomMethodArgumentsResolver;
    @MockBean
    private  GeometryFactory geometryFactory;
//    @BeforeEach
//    void setup() {
//        this.mvc = MockMvcBuilders
//                .standaloneSetup(new RouteController(null))
//                .build();
//        this.objectMapper = new ObjectMapper();
//    }

    @Test
    @DisplayName(value = "Route File Binding Test")
    @WithMockUser(username = "username",password = "password",roles = {"user","USER"})
    public void saveRequestBindingTest() throws Exception {

        SaveRouteReqeust request = new SaveRouteReqeust();
        request.setPath(null);
        request.setTitle("title");
        request.setDescription("description");
        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile multipartFile = new MockMultipartFile("files", "test.png", "multipart/form-data", "<<data>>".getBytes());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders.multipart("/route")
                .file(new MockMultipartFile("request","","application/json",requestJson.getBytes(StandardCharsets.UTF_8)))
                .file(multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .accept(MediaType.APPLICATION_JSON)
                .with(csrf());
        mvc.perform(param)
                .andExpect(content().string("title"));
    }

}