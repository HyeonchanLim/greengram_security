package com.green.greengram.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.greengram.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@ToString
@Schema(title = "피드 댓글 리스트 요청")
public class FeedCommentGetReq {
    private final static int FIRST_COMMENT_SIZE = 3;
//    private final static int DEFAULT_PAGE_SIZE = 20;
//    yaml 20 작성했으니 제외

    @Positive
    @Schema(title = "피드 pk", name = "feed_id", description = "피드 PK", example = "1" , requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId; // n + 1 방식이라 feedId 가 필요함

    @PositiveOrZero
    @Schema(title="ㅅ", description = "피드 PK", name="start_idx", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int startIdx;

    @Min(value = 21 , message = "사이즈는 20 이상이어야 합니다.")
    @Schema(title = "페이지 당 아이템 수" , description = "default = 20")
    private int size;


    @ConstructorProperties({"feed_id", "start_idx", "size"})
    public FeedCommentGetReq(long feedId, int startIdx, Integer size) {
        // bindparam - 클라이언트인 url 주소랑 맞춤 , 즉 url 키값은 camel 케이스 기법이 아닌
        // feed_id 인 _ 를 사용하는 값을 쓰고 있음
        // 그리고 url 에서 넘어오는 값을 받음
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size = (size == null ? Constants.getDefault_page_size() : size) + 1;
    }
}
