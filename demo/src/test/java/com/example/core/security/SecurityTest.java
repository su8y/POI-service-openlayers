package com.example.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
@SpringBootTest
//@ContextConfiguration(classes = {AuthConfig.class})
@ExtendWith(value = RestDocumentationExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SecurityTest {

    private MockMvc mvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
//                .defaultRequest(get("/").with(user("user").roles("ADMIN")))
                .build();
    }

//    public static RequestPostProcessor rob() {
//        return user("rob").roles("admin");
//    }

    @Test
    @WithAnonymousUser
    void signup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> input = new HashMap<>();
        input.put("memberId", "qotndk123");
        input.put("password", "qotndk123");
        //with security by RequestPostProcessor
//        mvc.perform(get("/hello").with(rob()));
        mvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("member-login", // 4
                                requestFields(
                                        fieldWithPath("memberId").type(JsonFieldType.STRING).description("회원ID"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원PW")
                                )
                        )
                );

        /**
         * formLogin test
         * */
//      mvc.perform(formLogin("/auth").user("u","admin").password("p","pass"))
    }
}
