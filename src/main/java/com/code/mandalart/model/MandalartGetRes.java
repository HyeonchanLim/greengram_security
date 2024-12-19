package com.code.mandalart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "만다라트 조회 정보")
public class MandalartGetRes {
    @Schema(name = "mandalart_id")
    private long mandalartId;
    @Schema(name = "project_id")
    private long projectId;
    @Schema(name = "completed_fg" , description = "0: 미완료 , 1: 완료")
    private int completedFg;
    @Schema(description = "0: 최상위 부모 0단계, 1: 1단계 레벨 , 2: 2단계 레벨")
    private int depth;
    @Schema(name = "order_id" , description = "각 단계별0~7칸 선택 칸")
    private int orderId;
    @Schema(description = "실천 목표")
    private String title;
    @Schema(description = "세부 내용")
    private String contents;
    @Schema(name = "created_at" , description = "생성일")
    private String createdAt;
    @Schema(name = "updated_at" , description = "업데이트일")
    private String updatedAt;
    @Schema(name = "start_date" , description = "계획 시작일")
    private String startDate;
    @Schema(name = "finish_date" , description = "계획 종료일")
    private String finishDate;
}
