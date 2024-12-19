package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Schema(title = "유저 정보 get 요청")
@ToString
@Setter
public class UserInfoGetReq {
    @JsonIgnore
    private long signedUserId;
    @Schema(name = "profile_user_id", description = "프로필 유저 pk" , example = "1" , requiredMode = Schema.RequiredMode.REQUIRED)
    private long profileUserId;

    @ConstructorProperties({"signed_user_id"})
    public UserInfoGetReq(long profileUserId){
    this.profileUserId = profileUserId;
    }
    public void setSignedUserId (long signedUserId){
        this.signedUserId = signedUserId;
    }
}
