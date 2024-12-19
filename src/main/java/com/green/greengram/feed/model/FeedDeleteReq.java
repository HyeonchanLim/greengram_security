package com.green.greengram.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class FeedDeleteReq {
    @Schema(description = "피드 PK", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    //signeduserid 는 jwt 넘어가면 jsonignore 처리 할꺼임
    @JsonIgnore
    private long signedUserId;

    @ConstructorProperties({"feed_id"})
    public FeedDeleteReq(long feedId) {
        this.feedId = feedId;
    }
    public void setSignedUserId(long signedUserId){
        this.signedUserId = signedUserId;
    }
}
