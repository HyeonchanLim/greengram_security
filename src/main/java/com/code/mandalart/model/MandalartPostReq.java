package com.code.mandalart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "")
public class MandalartPostReq {
    @Schema(title = "mandalart_id")
    private long mandalartId;
    @Schema(title = "project_id")
    private long projectId;
    @Schema(description = "실천 목표")
    private String title;
    private String contents;
    @Schema(description = "계획 시작일")
    private String startDate;
    @Schema(description = "계획 종료일")
    private String finishDate;
    @Schema(name = "completed_fd", description = "완료 여부 0:미완료 , 1:완료")
    private int completedFg;
    @Schema(description = "0:최상위 부모 1:1단계 2:2단계")
    private int depth;
    @Schema(name = "order_id", description = "각 단계별 0~7칸 , 선택 칸 -> 데이터 입력 ")
    private int orderId;

    /*
    ex) lv2의 7번 칸 - lv2 7칸 - lv 8칸
    -> depth , order_id 로 따로 작성 xxx
    프론트에서 데이터를 해당 칸에 직접 입력함
    만다라트 시작 날짜 > 프로젝트 시작일 - 상위 부모의 시작일보다 더 빨리 시작 x
    만다라트 종료 날짜 > 프로젝트 종료일 - 상위 부모의 종료일보다 더 늦게 종료 x

     */
}
