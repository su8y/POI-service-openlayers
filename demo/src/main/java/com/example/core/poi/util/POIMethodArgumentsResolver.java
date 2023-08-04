package com.example.core.poi.util;

import com.example.core.poi.dto.POISearchDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class POIMethodArgumentsResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper objectMapper;

    public POIMethodArgumentsResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(POIArgsResolve.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();

        String paramName = parameter.getParameterName();
        String jsonObject = req.getQueryString();

        POISearchDto searchDto = objectMapper.readValue(jsonObject, POISearchDto.class);
        System.out.println("searchDto.getInputText() = " + searchDto.getInputText());
        return searchDto;
    }
}
