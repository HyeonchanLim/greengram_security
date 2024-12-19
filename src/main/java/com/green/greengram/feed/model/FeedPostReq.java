package com.green.greengram.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "피드 등록 요청")
public class FeedPostReq {
    @JsonIgnore
    private long feedId;
    @JsonIgnore
    private long writerUserId;

    @Schema(title = "피드 내용" , example = "피드 내용 테스트")
    private String contents;
    @Schema(title = "피드 위치" , example = "그린 컴퓨터 학원")
    private String location;
}
