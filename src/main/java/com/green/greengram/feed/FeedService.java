package com.green.greengram.feed;

import com.green.greengram.common.MyFileUtils;
import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.feed.comment.FeedCommentMapper;
import com.green.greengram.feed.comment.model.FeedCommentDto;
import com.green.greengram.feed.comment.model.FeedCommentGetReq;
import com.green.greengram.feed.comment.model.FeedCommentGetRes;
import com.green.greengram.feed.comment.model.FeedPicSel;
import com.green.greengram.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedService {
    private final FeedMapper mapper;
    private final FeedpicMapper feedpicMapper;
    private final MyFileUtils myFileUtils;
    private final FeedCommentMapper feedCommentMapper;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    // 애노테이션 걸어두면 구현부 중간에 오류가 발생하면 아예 실행 X
    // 애노테이션 없으면 오류 직전까지의 실행부분이 실행 적용
    // 아래의 메소드에 구현부 내용이 많다 -> 트랜젝션
    public FeedPostRes postFeed(List<MultipartFile> pics , FeedPostReq p){
        p.setWriterUserId(authenticationFacade.getSignedUserId());
        int result = mapper.insFeed(p);
        // 파일 등록
        long feedId = p.getFeedId();

        //저장 폴더 만들기 , 저장위치/feed/${feedId}/파일들을 저장한다.
        String middlePath = String.format("feed/%d",feedId);
        myFileUtils.makeFolders(middlePath);

        // 랜덤 파일명 저장용 >> feed_pic 테이블에 저장할 때 사용
        // pic.size() 작성한 이유 -> 근사치 설정하기 위해서 why? 속도 향상
        List<String> picNameList = new ArrayList<>(pics.size());

        for (MultipartFile pic : pics){
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            picNameList.add(savedPicName);
            String filePath = String.format("%s/%s",middlePath,savedPicName);
            try {
                myFileUtils.transferTo(pic , filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 피드 하나당 사진이 여러개 -> 1 : n 관계  - for 문으로 반복
        }
        FeedPicDto feedPicDto = new FeedPicDto();
        feedPicDto.setPics(picNameList);
        feedPicDto.setFeedId(feedId);
        int resultpics = feedpicMapper.insFeedpic(feedPicDto);

        // postres SETTER 사용했을 경우
//        FeedPostRes res = new FeedPostRes();
//        res.setFeedId(feedId);
//        res.setpic(picNameList);

        return FeedPostRes.builder()
                .feedId(feedId)
                .pics(picNameList)
                .build();
    }

    public List<FeedGetRes> selFeedList (FeedGetReq p){
        p.setSighedUserId(authenticationFacade.getSignedUserId());
        // n + 1 이슈 발생 -> 리스트를 만들었기 때문에 리스트 가져오는데 +1회가 발생함
        // 피드 20 * 2번 + 리스트 1 -> 41번 실행
        List<FeedGetRes> list = mapper.selFeedList(p);
        //피드 당 사진 , item - feedid 각각의 튜플들 담아서 사용
        // 1번 feedid 작업 끝나면 다음 feedid 넘어가면서 계속 진행(for문 반복)
        for (FeedGetRes item : list){
            // 피드 당 사진 리스트
            item.setPics(feedpicMapper.selFeedPicList(item.getFeedId()));

            // 피드 당 댓글 4개
            FeedCommentGetReq commentGetReq = new FeedCommentGetReq(item.getFeedId(), 0, 3);

            List<FeedCommentDto> commentList = feedCommentMapper.selFeedCommentList(commentGetReq);
            // commentdto 타입 & xml에서 설정한 튜플들로 목록 설정 ->
            // 매개변수 자체가 튜플이라 생각
            // 튜플에 대응하는 컬럼을 작성 -> 2개 이상이라면 list
            FeedCommentGetRes commentGetRes = new FeedCommentGetRes();
            commentGetRes.setCommentList(commentList);
            commentGetRes.setMoreComment(commentList.size() == commentGetReq.getSize());

            if (commentGetRes.isMoreComment()){
                commentList.remove(commentList.size()-1);
            }
            item.setComment(commentGetRes);
            // 코멘트 값 설정 1 or 0 , 4개면 true , 아니면 false

            // 댓글 0 페이지에서 시작 부분은 0,4 -> 만약 댓글을 삭제하면 3으로 바뀌는데 더 보기 누를 경우 3, 20
            // 1개 더 삭제하면 2, 20 으로 시작
        }
        return list;
    }

    public List<FeedGetRes> getFeedList2(FeedGetReq p){
        return null;
    }
    public List<FeedGetRes> getFeedList3(FeedGetReq p){
        p.setSighedUserId(authenticationFacade.getSignedUserId());
        List<FeedGetRes> list = mapper.selFeedList(p);

        if (list.size()==0){
            return list;
        }
        List<Long> feedIds4 = list.stream().map(FeedGetRes::getFeedId).collect(Collectors.toList());
        List<Long> feedIds5 = list.stream().map(item -> ((FeedGetRes)item).getFeedId()).toList();
        List<Long> feedIds6 = list.stream().map(item -> { return ((FeedGetRes)item).getFeedId();}).toList();
        List<Long> feedIds = new ArrayList<>(list.size());
        for(FeedGetRes item : list) {
            feedIds.add(item.getFeedId());
        }
        // feedIds 배열 - getres 타입의 feedid 입력
        log.info("feedIds: {}", feedIds);
        //피드와 관련된 사진 리스트
        List<FeedPicSel> feedPicList = feedpicMapper.selFeedPicListByFeedIds(feedIds);
        // feedids 가 가지고 있는 feedid 를 이용해서 pic 입력하게 설정 / 현재 pic 없음
        log.info("feedPicList: {}", feedPicList);
        // Map - key & value 쌍으로 데이터를 저장하는 인터페이스
        // 키는 고유값 , 하나의 키에 하나의 값만 매핑 가능
        // 값은 중복 가능 , 키는 중복 불가능
        Map<Long, List<String>> picHashMap = new HashMap<>();
        // Long (feedId) , List<String> (pic) 을 HashMap 으로 찍어낼꺼임
        for(FeedPicSel item : feedPicList) {
            long feedId = item.getFeedId();
            if(!picHashMap.containsKey(feedId)) {
                // 피드id 가 현재 있는지 확인 절차
                picHashMap.put(feedId, new ArrayList<String>(2));
                // 피드id 가 없다면 해당 피드id에 배열(value - pic) 생성
            }
            List<String> pics = picHashMap.get(feedId);
            // feedId (key) 를 pics 에 입력하면서 value인 List<String> (pic) 을 가져옴
            pics.add(item.getPic());
            // value 인 pic 을 feedId 에 추가
        }

        //피드와 관련된 댓글 리스트
        List<FeedCommentDto> feedCommentList = feedCommentMapper.selFeedCommentListByFeedIdsLimit4(feedIds);

        Map<Long, FeedCommentGetRes> commentHashMap = new HashMap<>();
        for(FeedCommentDto item : feedCommentList) {
            long feedId = item.getFeedId();
            if(!commentHashMap.containsKey(feedId)) {
                FeedCommentGetRes feedCommentGetRes = new FeedCommentGetRes();
                feedCommentGetRes.setCommentList(new ArrayList<>(4));
                commentHashMap.put(feedId, feedCommentGetRes); // key - value 셋팅
            }
            FeedCommentGetRes feedCommentGetRes = commentHashMap.get(feedId);
            // key - feedid 통해서 value - commentgetres 호출
            feedCommentGetRes.getCommentList().add(item);
            // commentGetRes (4) . commentDto ( item(feedId) )
        }
        // 위에서 만든 list (feedId) 를 가져옴
        for(FeedGetRes res : list) {
            // hashmap 을 통해서 key 값 feedid 를 가져오면 value인 pic 을 가져옴
            // res.setpics 에 value - pic 을 입력
            res.setPics(picHashMap.get(res.getFeedId()));
            FeedCommentGetRes feedCommentGetRes = commentHashMap.get(res.getFeedId());
            // feedid , pic 은 가져왔지만 댓글은 안가져왔음 if문 통해서 comment 입력
            if(feedCommentGetRes == null) {
                // 댓글 목록 에서 없으면 새로 만들고 배열이 0 인걸 만들어줌
                feedCommentGetRes = new FeedCommentGetRes();
                feedCommentGetRes.setCommentList(new ArrayList<>());
            } else if (feedCommentGetRes.getCommentList().size() == 4) {
                // 댓글이 4개면 morecomment 에서 true 판정 -> 3개 출력 + 1개는 더보기로 바뀌니 -1 작업
                feedCommentGetRes.setMoreComment(true);
                feedCommentGetRes.getCommentList().remove(feedCommentGetRes.getCommentList().size() - 1);
            }
            res.setComment(feedCommentGetRes);
        }
        log.info("list: {}", list);
        return list;
    }
    @Transactional
    public int deleteFeed(FeedDeleteReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        //피드 댓글, 좋아요 , 사진 삭제
        int affectedRows = mapper.delFeedLikeAndFeedCommentAndFeedPic(p);
        log.info("affectedRows: {}", affectedRows);

        //피드 삭제
        int affectedRowsFeed = mapper.delFeed(p);
        log.info("FeedService > deleteFeed > affectedRowsFeed : {}" , affectedRowsFeed);

        //피드 사진 , 폴더 삭제
        String deletePath = String.format("%s/feed/%d", myFileUtils.getUploadPath(), p.getFeedId());
        myFileUtils.deleteFolder(deletePath, true);

        return 1;
    }
}
