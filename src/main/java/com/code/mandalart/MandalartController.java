package com.code.mandalart;


import com.code.mandalart.model.MandalartGetReq;
import com.code.mandalart.model.MandalartGetRes;
import com.code.mandalart.model.MandalartPostReq;
import com.code.mandalart.model.MandalartPostRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("mand")
@Tag(name = "만다라트", description = "만다라트")
public class MandalartController {
        /*
        만다라트 조회 -> 프로젝트id "?" , mandalart : [
        {
        만다라트 id : ?
        , title : 만다라트 명칭
        , contents : 만다라트 내용
        , completed_fd : 완료 여부 0 or 1
        , depth : 단계(레벨) 0,1,2
        , order_id : 순서 0~7칸
        , start_date : 계획 시작일
        , finish_date : 계획 종료일
        }
        ]
        만다라트 마다 출력
         */

    @GetMapping
    public MandalartGetRes getMandalart (MandalartGetReq p){
        p.getProjectId();
        return null;
    }

    @PostMapping
    public MandalartPostRes postMandalart (MandalartPostReq p){

        return null;
    }

}










