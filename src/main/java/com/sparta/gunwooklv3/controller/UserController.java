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
    public StatusResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto){
        // UserService클래스의 signup메서드에 RequestDto정보를 넘겨주고 user형식으로 정보를 저장한다.
        User user = userService.signup(signupRequestDto);

        // 회원가입이 된다면 메세지를 출력하고 코드200을 반환하여준다.
        return new StatusResult("회원가입 성공", HttpStatus.OK.value());
    }

    // 로그인 API
    @ResponseBody
    @PostMapping("/login")
    // HTTP응답을 생성하기 위해서 HttpServletResponse를 사용.
    public StatusResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){

        // token에 발급받은 토큰을 저장하여준다.
        String token = userService.login(loginRequestDto, response);

        return new StatusResult("로그인 성공", HttpStatus.OK.value());
    }
}