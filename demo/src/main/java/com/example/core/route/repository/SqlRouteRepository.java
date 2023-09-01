package com.example.core.route.repository;

import com.example.core.route.dto.ResponseRoute;
import com.example.core.route.dto.RouteSearchOption;
import com.example.core.route.dto.SaveRouteReqeust;
import org.springframework.data.domain.Page;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface SqlRouteRepository {
    Page<ResponseRoute> findRouteBySearchOption(RouteSearchOption routeSearchOption) throws UserPrincipalNotFoundException;

    void update(SaveRouteReqeust request);

}
