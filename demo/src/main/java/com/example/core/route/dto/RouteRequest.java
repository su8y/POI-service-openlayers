package com.example.core.route.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RouteRequest {
    public static final String 실시간_빠른_길 = "trafast";
    public static final String 실시간_편한_길 = "tracomfort";
    public static final String 실시간_최적 = "traoptimal";
    public static final String 무료_도로_우선 = "traavoidtoll";
    public static final String 자동자_전용도로_회피 = "traavoidcaronly";


    @NotBlank
    private String start;
    @NotBlank
    private String goal;
    // 경로는 최대 5개까지 연결할 수 있으며 : 으로 연결한다.
    // 옵션은 최대 3개까지 연결할수 있으며 . 으로 연결한다
    private String waypoints;
    private String option;
}
