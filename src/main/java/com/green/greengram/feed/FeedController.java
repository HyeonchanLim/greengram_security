package com.green.greengram.feed;

import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.feed.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("feed")
@Tag(name = "2. 피드" , description = "피드 관리")
public class FeedController {
    private final FeedService service;

    @PostMapping
    @Operation
    public ResultResponse<FeedPostRes> postFeed (@Valid @RequestPart List<MultipartFile> pics
                                                , @RequestPart FeedPostReq p) {
        FeedPostRes res = service.postFeed(pics , p);
        return ResultResponse.<FeedPostRes>builder()
                .resultMessage("피드 등록 완료")
                .resultData(res)
                .build();
    }
    @GetMapping("ver2")
    @Operation(summary = "Feed 리스트 - No N+1", description = "signed_user_id는 로그인한 사용자의 pk")
    public ResultResponse<List<FeedGetRes>> getFeedListVer2(@Valid @ParameterObject @ModelAttribute FeedGetReq p) {
        log.info("FeedController > getFeedList > p: {}", p);
        List<FeedGetRes> list = service.getFeedList2(p);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

    @GetMapping("ver3")
    @Operation(summary = "Feed 리스트", description = "loginUserId는 로그인한 사용자의 pk")
    public ResultResponse<List<FeedGetRes>> getFeedList(@Valid @ParameterObject @ModelAttribute FeedGetReq p) {
        log.info("FeedController > getFeedList > p: {}", p);
        //List<FeedGetRes> list = service.getFeedList(p);
        List<FeedGetRes> list = service.getFeedList3(p);
        log.info("FeedController > getFeedList > aaaaaaaaa: {}", list);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

    @GetMapping("ver4")
    @Operation(summary = "Feed 리스트 - No N+1 - using Mybatis", description = "signed_user_id는 로그인한 사용자의 pk")
    public ResultResponse<List<FeedGetRes>> getFeedListVer4(@Valid @ParameterObject @ModelAttribute FeedGetReq p) {
        log.info("FeedController > getFeedList > p: {}", p);
        List<FeedGetRes> list = service.getFeedList4(p);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }
    @DeleteMapping
    @Operation(summary = "feed 삭제" , description = "피드의 댓글 , 좋아요 전부 삭제 처리")
    public ResultResponse<Integer> deleteFeed(@ParameterObject @ModelAttribute FeedDeleteReq p){
        log.info("FeedController > deleteFeed > p: {}" , p);
        int result = service.deleteFeed(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("삭제 완료")
                .resultData(result)
                .build();
    }

}
