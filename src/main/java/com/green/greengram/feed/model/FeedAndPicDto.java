package com.green.greengram.feed.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedAndPicDto {
    private long feedId;
    private String contents;
    private String location;
    private String createdAt;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private int isLike;
    // pic 칼럼 추가하고 schema 는 내부에서 처리할 데이터라 필요없어서 지움
    private String pic;
}
