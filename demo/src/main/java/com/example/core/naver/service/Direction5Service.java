package com.example.core.naver.service;

import com.example.core.route.dto.RouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class Direction5Service implements DirectionService {
    private final String secretKey;
    private final String clientId;
    private final String baseUrl;
    private final String secretHeader;
    private final String clientHeader;
    private final WebClient direction5WebClient;

    public Direction5Service(
            @Value("${naver.secret.key}") final String secretKey,
            @Value("${naver.client.id}") final String clientId,
            @Value("${naver.api.path.direction5}") final String baseUrl,
            @Value("${naver.secret.header}") final String secretHeader,
            @Value("${naver.client.header}") final String clientHeader,
            WebClient webClient
    ) {
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.secretHeader = secretHeader;
        this.clientHeader = clientHeader;


        this.direction5WebClient = webClient.mutate()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(secretHeader, secretKey);
                    httpHeaders.add(clientHeader, clientId);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "charset=UTF-8");

                })
                .baseUrl(baseUrl)
                .build();
    }


    @Override
    public String requestRouteByRequestRoute(RouteRequest routeRequest) {
        String block = direction5WebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("start", routeRequest.getStart())
                        .queryParam("goal", routeRequest.getGoal())
                        .queryParam("option", routeRequest.getOption())
                        .queryParam("waypoints", routeRequest.getWaypoints())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return block;
    }
}
