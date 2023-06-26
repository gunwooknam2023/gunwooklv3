package com.sparta.gunwooklv3.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class StatusResult {

    private String msg; // 메세지 저장할 변수
    private int statusCode; // 상태코드 저장할 변수

    @Builder // 해당클래스에 빌더를 자동으로 추가해준다
    public StatusResult(String msg, int statusCode){
        this.msg = msg; // 메세지를 받아와서 저장
        this.statusCode = statusCode; // 상태코드를 받아와서 저장
    }
}