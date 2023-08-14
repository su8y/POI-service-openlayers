package com.example.core.poi;

import com.example.core.category.Category;
import com.example.core.poi.dto.POIRequestDto;
import com.example.core.poi.dto.POIResponseDto;
import com.example.core.poi.dto.Poi;
import com.example.core.poi.util.GeomArgsResolve;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/pois")
@RequiredArgsConstructor
public class POIController {
    private final POIService poiService;
    private final PagedResourcesAssembler<POIResponseDto> assembler;

    // METHOD :: GET
    @GetMapping
    public EntityModel<ResponseEntity> getList(
            @GeomArgsResolve(value = "polygon") Polygon polygon,
            @RequestParam(name = "inputText") String inputText,
            @ModelAttribute Category category,
            @PageableDefault(size = 100) Pageable pageable
    ) {
        POISearchParam searchparam = new POISearchParam();
        searchparam.setPolygon(polygon);
        searchparam.setSelectedCategory(category);
        searchparam.setInputText(inputText);
        searchparam.setPageable(pageable);

        Page<Poi> byCurrentPosition = poiService.findByCurrentPosition(searchparam);

        return EntityModel.of(ResponseEntity.ok(byCurrentPosition));
    }

    @GetMapping("/manage")
    public ResponseEntity<PagedModel<?>> getMemberList(
            Authentication authentication,
            @ModelAttribute Category category,
            @PageableDefault Pageable pageable
    ) {

        String memberId = authentication.getName();

        POISearchParam build = POISearchParam.builder()
                .memberId(memberId)
                .selectedCategory(category)
                .pageable(pageable)
                .build();

        Page<POIResponseDto> poiList = poiService.getManagePoiList(build);

        PagedModel<EntityModel<POIResponseDto>> entityModels = assembler.toModel(poiList);

        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/manage/{poiId}")
    public ResponseEntity<Poi> detailPoi(@PathVariable(name = "poiId") Long id) {
        return ResponseEntity.ok(poiService.getDetailPoi(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel createPoi(
            @ModelAttribute @Validated POIRequestDto dto,
            @RequestPart("images") List<MultipartFile> multipartFile,
            Authentication authentication,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return EntityModel.of(ResponseEntity.badRequest().build());
        }

        String memberId = authentication.getName();
        Poi madePoi = Poi.factory(dto);
        poiService.createPOI(madePoi, dto.getCategoryCode(), multipartFile, memberId);


        return EntityModel.of(ResponseEntity.ok(dto.getName() + "save is Successed"),
                linkTo(methodOn(POIController.class).editPoi(null)).withRel("update"),
                linkTo(methodOn(POIController.class).createPoi(null, null, null, null)).withSelfRel()
        );
    }

    @PutMapping
    public ResponseEntity<?> editPoi(Long poiId) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{poiIdList}")
    public ResponseEntity<?> removePoi(@PathVariable(name = "poiIdList") List<Long> poiIdList) {
        for (Long aLong : poiIdList) {
            System.out.println(aLong);
        }
        poiService.removePOI(poiIdList);
        return ResponseEntity.ok("Delete Success");
    }
}
