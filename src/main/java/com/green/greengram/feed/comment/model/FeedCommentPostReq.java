package com.green.greengram.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "피드 댓글 등록 요청")
public class FeedCommentPostReq {
    @JsonIgnore
    private long feedCommentId; // pk값 받을려고 사용

    @Schema(title = "피드 PK" , example = "1" , requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @JsonIgnore
    private long userId;
    @Schema(title = "댓글" , example = "안녕하세요" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;

}
