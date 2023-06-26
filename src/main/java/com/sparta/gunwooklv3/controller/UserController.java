package com.sparta.gunwooklv3.controller;


import com.sparta.gunwooklv3.dto.LoginRequestDto;
import com.sparta.gunwooklv3.dto.SignupRequestDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.entity.User;
import com.sparta.gunwooklv3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated // 제약조건을 위한 선언
@RestController // 컨트롤러 선언
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    // 회원가입 API
    @PostMapping("/signup")
    // 범위를 지정하기위해 @Valid를 붙여주었고, RequestBody에서 SignupRequestDto형식으로 정보를 받아온다.
    public StatusResult signup(@RequestBody SignupRequestDto signupRequestDto){
        return userService.signup(signupRequestDto);
    }

    // 로그인 API
    @ResponseBody
    @PostMapping("/login")
    // HTTP응답을 생성하기 위해서 HttpServletResponse를 사용.
    public StatusResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponseresponse){
        return userService.login(loginRequestDto, httpServletResponseresponse);
    }
}