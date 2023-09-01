package com.example.core.naver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeocodeService {

    private final String secretKey;
    private final String clientId;
    private final String baseUrl;
    private final String secretHeader;
    private final String clientHeader;
    private final WebClient geocodeClient;

    public GeocodeService(
            @Value("${naver.secret.key}") final String secretKey,
            @Value("${naver.client.id}") final String clientId,
            @Value("${naver.api.path.geocoding}") final String baseUrl,
            @Value("${naver.secret.header}") final String secretHeader,
            @Value("${naver.client.header}") final String clientHeader,
            WebClient webClient
    ) {
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.secretHeader = secretHeader;
        this.clientHeader = clientHeader;


        this.geocodeClient = webClient.mutate()
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

    public String addressToCoordinate(String query) {
        String block = geocodeClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return block;
    }


}
