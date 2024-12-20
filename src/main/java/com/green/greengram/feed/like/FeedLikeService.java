package com.green.greengram.feed.like;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    // 토글 방식 진행 과정 ->
    // 값이 0 이다 -> 값을 입력 -> 1로 변경
    // 값이 0 이 아니다 -> 0 리턴

    public Integer feedLikeToggle (FeedLikeReq p){
        p.setUserId(authenticationFacade.getSignedUserId());
        int result = mapper.delFeedLike(p);
        if (result == 0){
            return mapper.insFeedLike(p); // 좋아요 등록이 되었을 때
        }
        return 0; // 좋아요 취소가 되었을 때
    }
}
