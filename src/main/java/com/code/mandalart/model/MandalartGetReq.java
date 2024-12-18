package com.code.mandalart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MandalartGetReq {
    @Schema(name = "mandalart_id")
    private long mandalartId;
    @Schema(name = "project_id")
    private long projectId;

}
