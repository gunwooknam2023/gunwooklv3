package com.sparta.gunwooklv3.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String username; // 유저이름
    private String password; // 비밀번호
    private String token; // 토큰
}