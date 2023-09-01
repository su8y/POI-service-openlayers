package com.example.core.naver.controller;

import com.example.core.naver.service.GeocodeService;
import com.example.core.naver.service.ReverseGeocodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NaverController {
    private final ReverseGeocodeService reverseGeocodeService;
    private final GeocodeService geocodeService;

    @GetMapping(value = "/to-coords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> toCoordinates(@RequestParam("address") String address) {
        String s = geocodeService.addressToCoordinate(address);
        return ResponseEntity.ok(s);
    }

    @GetMapping(value = "/to-addr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> toAddress(@RequestParam("coordinates") double[] coordinates) {
        String s = reverseGeocodeService.coordinateToAddress(coordinates);
        return ResponseEntity.ok(s);
    }
}
