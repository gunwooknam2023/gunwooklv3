package com.sparta.gunwooklv3.controller;


import com.sparta.gunwooklv3.dto.PostRequestDto;
import com.sparta.gunwooklv3.dto.PostResponseDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 컨트롤러 정의
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api") // 공통된 경로 묶어주기
public class PostController {

    private final PostService postService;

    @PostMapping("/posts") // 게시글 작성 API
    // 게시글 작성에 필요한 정보를 전달받기 위해 @RequestBody PostRequestDto requestDto 사용
    // 요청에 대한 정보(헤더정보나 세션)을 활용하기 위해 HttpServletRequest request 사용
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/posts") // 전체 게시글 목록 조회 API
    public List<PostResponseDto> getPost(){
        return postService.getPost();
    }

    @GetMapping("/posts/{id}") // 선택한 게시글 조회 API
    // {id}부분의 해당하는 값을 추출하여 Long id에 저장한다.
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PutMapping("/posts/{id}") //  선택한 게시글 수정 API
    // id값을 가져오기위해 @PathVariable을 사용하였고, 게시글정보를 가져오기위해 @RequestBody를 사용,
    // 사용자 토큰을 확인하기 위해 HttpServletRequest를 사용하였다.
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.updatePost(id, requestDto, request);
    }


    @DeleteMapping("/posts/{id}") // 선택한 게시글 삭제 API
    // 선택한 id값을 가져오기위해 @PathVariable을 사용하였고, 유저정보를 확인하기위해 HttpServletRequest를 사용
    public StatusResult deletePost(@PathVariable Long id, HttpServletRequest request){
        postService.deletePost(id, request);
        // HttpStatus은 열거형 상수이고 HTTP응답코드를 나타낸다. HttpStatus.OK는 상태코드 200을 나타낸다.
        return new StatusResult("게시글 삭제 성공", HttpStatus.OK.value());
    }










}