package com.sparta.gunwooklv3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {

    @NotBlank // NotBlank는 null과 "" " " 모두 허용하지 않는다.
    @Size(min =4, max = 10, message = "4~10자 사이") // 글자수를 지정하여준다.
    @Pattern(regexp = "^[a-z0-9]*$",message = "알파벳 소문자(a~z), 숫자(0~9)") // 정규식을 지정해줄수 있다.
    private String username;

    @NotBlank // NotBlank는 null과 "" " " 모두 허용하지 않는다.
    @Size(min = 8, max = 15, message = "8~15자 사이") // 글자수를 지정하여준다.
    @Pattern(regexp = "^[a-zA-Z0-9]*$",message = "알파벳 소문자(a~z),알파벳 대문자(A~Z), 숫자(0~9)") // 정규식을 지정해줄수 있다.
    private String password;

}