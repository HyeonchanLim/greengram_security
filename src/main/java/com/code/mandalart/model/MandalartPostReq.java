package com.code.mandalart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "")
public class MandalartPostReq {
    private long mandalartId;
    private String title;
    private String contents;
    private String
}
