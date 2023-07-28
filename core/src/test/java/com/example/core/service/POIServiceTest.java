package com.example.core.service;

import com.example.core.model.POI;
import com.example.core.model.Point;
import com.example.core.repository.CategoryRepository;
import com.example.core.repository.POIRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
@DataJpaTest
class POIServiceTest {
    @Autowired
    private POIRepository poiRepository;
    //    @Mock
    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @DisplayName("POI 저장 테스트")
    public void createPOITest() {
        POI poi = POI.builder()
                .telNo("000-0000-0000")
                .name("test1234")
                .description("test용 poi")
                .lon(1.0)
                .lat(1.0)
                .build();

        poiRepository.save(poi);
    }

    @Test
    @DisplayName("POI 삭제 테스트")
    public void deletePOITest() {
        Long id = 10005L;
        poiService.removePOI(id);
    }
//    @Test
//    @DisplayName("POI 조회 테스트")
//    public void listPOITest(){
//        //given
//        ArrayList<POI> pois = new ArrayList<>();
//        pois.add(new POI());
//        when(poiRepository.selectAll()).thenReturn(pois);
//        //when
//        List<POI> findPOI = poiService.findAllPOI();
//        //then
//        assertThat(findPOI.size()).isEqualTo(1);
//
//    }

}