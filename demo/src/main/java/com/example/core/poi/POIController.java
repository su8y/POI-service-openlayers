package com.example.core.poi;

import com.example.core.poi.dto.POIRequestDto;
import com.example.core.poi.dto.POISearchDto;
import com.example.core.poi.util.POIArgsResolve;
import com.example.core.service.POIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/pois")
@RequiredArgsConstructor
public class POIController {
    private final POIService poiService;

    @GetMapping
    public EntityModel<ResponseEntity> getList(
            @POIArgsResolve POISearchDto searchDto
    ) {


        return EntityModel.of(ResponseEntity.ok(searchDto));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel createPoi(
            @RequestPart("dto") @Validated POIRequestDto dto,
            @RequestPart("images") MultipartFile multipartFile,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors() || multipartFile.isEmpty()) {
            return EntityModel.of(ResponseEntity.badRequest().build());
        }


        return EntityModel.of(ResponseEntity.ok(dto.getName() + "save is Successed"),
                linkTo(methodOn(POIController.class).editPoi()).withRel("update"),
                linkTo(methodOn(POIController.class).createPoi(null, null, null)).withSelfRel()
        );
    }

    @PutMapping
    public ResponseEntity<?> editPoi() {
        return ResponseEntity.ok("");
    }

    @DeleteMapping
    public ResponseEntity<?> removePoi() {
        return ResponseEntity.ok("");
    }
}
