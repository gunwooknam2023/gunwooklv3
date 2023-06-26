package com.sparta.gunwooklv3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {


    private String username; // 유저이름
    private String password; // 비밀번호
    private boolean admin = false; // admin의 기본값 설정. 기본값을 false로 설정해두어 따로 요청이없으면 USER로 생성된다.
    private String adminToken = ""; // 빈문자열로 초기화해주었다. 

}