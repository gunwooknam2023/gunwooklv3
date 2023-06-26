package com.sparta.gunwooklv3.service;


import com.sparta.gunwooklv3.dto.LoginRequestDto;
import com.sparta.gunwooklv3.dto.SignupRequestDto;
import com.sparta.gunwooklv3.entity.User;
import com.sparta.gunwooklv3.jwt.JwtUtil;
import com.sparta.gunwooklv3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    // 회원가입 API
    public User signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername(); // requestdto에서 가져온 유저이름을 대입
        String password = signupRequestDto.getPassword(); // requestdto에서 가져온 비밀번호를 대입

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username); // 유저네임을 넘겨주어 중복인지 확인.
        if(found.isPresent()){ // isPresent()메서드를 사용하여 객체가 해당값을 가지고 있는지 확인.
            throw new IllegalArgumentException("중복된 사용자가 존재합니다."); // 가지고있으면 예외처리 throw
        }

        User user = new User(username, password); // 가지고있지않다면 정보를 대입해주어 새로운 user를 생성한다.
        return userRepository.save(user); // 회원가입이 성공하면 userRepository에 저장하고 클라이언트에게 확인시켜준다.
    }


    @Transactional(readOnly = true)
    // 로그인 API
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getUsername(); // Dto에 담겨있는 유저네임을 저장
        String password = loginRequestDto.getPassword(); // Dto에 담겨있는 비밀번호를 저장

        // 사용자 확인
        // findByUsername을 사용하여 저장된 user정보를 가져온다. 없으면 예외처리를 throw 해준다.
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!user.getPassword().equals(password)){ // user에서 가져온 password와 입력받은 password가 일치하는지 확인한다.
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 일치하지않으면 예외처리 throw
        }

        // JWT Token 생성 및 반환
        // 응답헤더에 JWT토큰을 추가하여준다.
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

        return username; // 저장이 완료되면 반환해준다.
    }
}