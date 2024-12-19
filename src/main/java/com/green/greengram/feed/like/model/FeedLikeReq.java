package com.green.greengram.feed.like.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "피드 좋아요 Toggle")
// GET 방식에서는 description x
// 쿼리스트링에서는 title 작동 x -> description 사용
public class FeedLikeReq {
    @Schema(title = "피드 PK" , example = "2" , requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @JsonIgnore
    private long userId;

}
