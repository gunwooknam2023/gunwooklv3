package com.sparta.gunwooklv3.service;


import com.sparta.gunwooklv3.dto.LoginRequestDto;
import com.sparta.gunwooklv3.dto.SignupRequestDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.entity.User;
import com.sparta.gunwooklv3.entity.UserRoleEnum;
import com.sparta.gunwooklv3.jwt.JwtUtil;
import com.sparta.gunwooklv3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"; // 관리자 상수를 정의한다.

    @Transactional
    // 회원가입 API
    public StatusResult signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername(); // requestdto에서 가져온 유저이름을 대입
        String password = signupRequestDto.getPassword(); // requestdto에서 가져온 비밀번호를 대입

        // 아이디 형식 확인
        // 아이디를 조건에 맞춰서 검사한다. 조건에 맞지않으면 예외처리를 throw 한다.
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username)) {
            throw new IllegalArgumentException ("형식에 맞지 않는 아이디 입니다.");
        }

        // 비밀번호 형식 확인
        // 비밀번호를 조건에 맞춰 검사한다. 조건에 맞지 않으면 예외처리를 throw 한다.
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", password)) {
            throw new IllegalArgumentException ("형식에 맞지 않는 비밀번호 입니다.");
        }

        // 회원 중복 확인
        // findByUsername을 사용하여 유저네임이 중복인지 체크한다.
        Optional<User> found = userRepository.findByUsername(username); // 유저네임을 넘겨주어 중복인지 확인.
        if(found.isPresent()){ // isPresent()메서드를 사용하여 객체가 해당값을 가지고 있는지 확인.
            throw new IllegalArgumentException("중복된 사용자가 존재합니다."); // 가지고있으면 예외처리 throw
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER; // 처음 상태를 USER로 지정
        if (signupRequestDto.isAdmin()) { // ADMIN권한으로 등록하려할시에 실행.
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) { // 받아온 admintoken과 지정된 admintoken이 일치시 실행
                return new StatusResult("관리자 암호가 틀려 등록이 불가능합니다.", 400); // 예외처리 throw
            }
            role = UserRoleEnum.ADMIN; // 조건이 성립하면 ADMIN권한을 부여한다.
        }

        User user = new User(username, password, role); // 이름, 비밀번호, 권한을 넣어 새로운 객체를 생성해준다.
        userRepository.save(user); // 생성된 user정보를 Repository에 저장해준다.
        return new StatusResult("회원가입 성공", 200); // 성공이되면 메세지와 코드200을 반환하여준다.
    }


    @Transactional(readOnly = true)
    // 로그인 API
    public StatusResult login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getUsername(); // Dto에 담겨있는 유저네임을 저장
        String password = loginRequestDto.getPassword(); // Dto에 담겨있는 비밀번호를 저장

        // 사용자 확인
        // findByUsername을 사용하여 저장된 user정보를 가져온다.
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.") // 예외처리를 throw
        );

        // 비밀번호 확인
        if(!user.getPassword().equals(password)){ // user에서 가져온 password와 입력받은 password가 일치하는지 확인한다.
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 일치하지않으면 예외처리 throw
        }

        // JWT Token 생성 및 반환
        // 응답헤더에 JWT토큰을 추가하여준다.
        // user의 getUsername을 해주어 사용자의 이름을 기반으로 JWT 토큰 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

        return new StatusResult("로그인 성공", 200);
    }
}