package com.example.core.naver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReverseGeocodeService {

    private final String secretKey;
    private final String clientId;
    private final String baseUrl;
    private final String secretHeader;
    private final String clientHeader;
    private final WebClient reverseGeocodeClient;

    public ReverseGeocodeService(
            @Value("${naver.secret.key}") final String secretKey,
            @Value("${naver.client.id}") final String clientId,
            @Value("${naver.api.path.reverse-geocoding}") final String baseUrl,
            @Value("${naver.secret.header}") final String secretHeader,
            @Value("${naver.client.header}") final String clientHeader,
            WebClient webClient
    ) {
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.secretHeader = secretHeader;
        this.clientHeader = clientHeader;


        this.reverseGeocodeClient = webClient.mutate()
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

    public String coordinateToAddress(double[] coordinate) {
        StringBuilder builder = new StringBuilder();
        builder.append(coordinate[0]);
        builder.append(",");
        builder.append(coordinate[1]);

        String block = reverseGeocodeClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("")
                                .queryParam("coords", builder.toString())
                                .queryParam("orders", "legalcode,admcode,addr,roadaddr")
                                .queryParam("output", "json")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return block;
    }


}
