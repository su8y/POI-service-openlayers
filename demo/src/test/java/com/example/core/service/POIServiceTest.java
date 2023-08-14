package com.example.core.service;

import com.example.core.poi.repository.POIRepository;
import com.example.core.poi.POIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PoiServiceTest {
    @InjectMocks
    private POIService poiService;
    @Mock
    private POIRepository poiRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createPOI() {
    }
}