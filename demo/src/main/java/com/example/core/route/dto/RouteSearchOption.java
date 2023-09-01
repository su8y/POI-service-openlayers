package com.example.core.route.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class RouteSearchOption {
    private final Pageable pageable;
    private final String memberId;
    private final String inputText;

    public static RouteSearchOption toRouteSearchOption(final Pageable pageable, final String memberId, final String inputText) {
        return RouteSearchOption.builder()
                .pageable(pageable)
                .memberId(memberId)
                .inputText(Optional.ofNullable(inputText).orElse(""))
                .build();
    }

}
