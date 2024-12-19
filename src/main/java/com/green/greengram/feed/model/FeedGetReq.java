package com.green.greengram.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.greengram.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Slf4j
@ToString(callSuper = true)
// 부모한테 있는 생성자 값도 찍음 , 없으면 본인꺼만 찍음
// tostring 오버라이딩 -> 값 출력이 더 생기는 이유 ( 생성자 2개 작성 )
public class FeedGetReq extends Paging {
    @JsonIgnore
    private long signedUserId;
    // pk 값 가져옴
    @Schema(name = "profile_user_id")
    private Long profileUserId;
//    @ConstructorProperties({"page", "size", "signed_user_id"})
    // 쿼리스트링에서만 bindparam , constructorproperties 사용
    // camel 케이스 기법 사용x -> snake 케이스 기법 사용
    // setter 에서는 bindparam 사용 불가능함 -> 생성자로 값을 주입해줘야 함
    // swagger 에서 try it out 할 때 변경되는 key 값 -> key 값에 맞춰서 매개변수도 작성해줘야함
    public FeedGetReq (Integer page , Integer size
            , @BindParam("profile_user_id") Long profileUserId){
        // profileUserId -> 없을 때 없는대로 , 있으면 순서로
        super(size, page);
        this.profileUserId=profileUserId;
    }

    public void setSighedUserId(long signedUserId) {
        this.signedUserId = signedUserId;
    }
}
// 데이터를 클라이언트에게 돌려줄 때 json 형태로 반환하는데 여기서 body 에 담아서 전송
// 클라이언트에게 받을 때 head 에 담아서 받음
// 쿼리스트링 사용하는 조건은 get / delete 방식에서 사용
// 대부분 json 형태로 주고 받지만 특별한 경우에는 form 사용