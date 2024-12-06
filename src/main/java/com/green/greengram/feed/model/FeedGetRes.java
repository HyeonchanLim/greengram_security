package com.green.greengram.feed.model;

import com.green.greengram.feed.comment.model.FeedCommentGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(title = "피드 정보")
public class FeedGetRes {
    @Schema(title = "피드 PK")
    private long feedId;
    @Schema(title = "피드 내용")
    private String contents;
    @Schema(title = "피드 위치")
    private String location;
    @Schema(title = "피드 생성일시")
    private String createdAt;
    @Schema(title = "작성자 유저 PK")
    private long writerUserId;
    @Schema(title = "작성자 유저 이름")
    private String writerNm;
    @Schema(title = "작성자 유저 프로필 사진파일명")
    private String writerPic;
    @Schema(title = "좋아요", description = "1: 좋아요, 0: 좋아요 아님")
    private int isLike;

    @Schema(title = "피드 사진 리스트")
    private List<String> pics;
    @Schema(title = "피드 댓글")
    private FeedCommentGetRes comment;
    // getres 에 작성한 이유는 프론트에서 더보기 여부 + 댓글 더 있는지 체크 하는걸 중복 데이터
    // 보낼 이유가 없어서 따로 분리해서 작성함
    // 댓글이 더 있냐 없냐 확인 + list(commentdto) 목록

}
