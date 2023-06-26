package com.sparta.gunwooklv3.controller;


import com.sparta.gunwooklv3.dto.CommentRequestDto;
import com.sparta.gunwooklv3.dto.CommentResponseDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // 컨트롤러 선언
@RequiredArgsConstructor // 생성자 생성
@RequestMapping("/api") // 겹치는 경로 묶어주기
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/comment/{postId}")
    // postID에 {postId}로 받아온 값을 넣어준다.
    // 또한 작성내용을 CommentRequestDto에서 가져와서 저장한다.
    public CommentResponseDto createComment(@PathVariable Long postId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            HttpServletRequest httpServletRequest){

        // 받아온 값을 넘겨주고 CommentResponseDto형식으로 값을 반환받아온다.
        return commentService.createComment(postId, commentRequestDto, httpServletRequest);

    }


    // 댓글 수정 API
    @PutMapping("/comment/{commentId}")
    // CommentId안에 받아온 값을 넣어준다.
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            HttpServletRequest httpServletRequest){

        // commentId와 받아온값들을 넣어준다.
        return commentService.updateComment(commentId, commentRequestDto, httpServletRequest);

    }

    // 댓글 삭제 API
    @DeleteMapping("/comment/{commentId}")
    // commentId에 받아온 값을 넣어준다.
    public StatusResult deleteComment(@PathVariable Long commentId,
                                      HttpServletRequest httpServletRequest){

        // 삭제할 commentID와 유저정보를 넘겨준다.
        return commentService.deleteComment(commentId, httpServletRequest);

    }


}
