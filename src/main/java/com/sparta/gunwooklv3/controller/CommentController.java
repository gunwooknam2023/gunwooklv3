package com.sparta.gunwooklv3.controller;


import com.sparta.gunwooklv3.dto.CommentRequestDto;
import com.sparta.gunwooklv3.dto.CommentResponseDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/comment/{postId}")
    public CommentResponseDto createComment(@PathVariable Long postId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            HttpServletRequest httpServletRequest){

        return commentService.createComment(postId, commentRequestDto, httpServletRequest);

    }


    // 댓글 수정 API
    @PutMapping("/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            HttpServletRequest httpServletRequest){

        return commentService.updateComment(commentId, commentRequestDto, httpServletRequest);

    }

    // 댓글 삭제 API
    @DeleteMapping("/comment/{commentId}")
    public StatusResult deleteComment(@PathVariable Long commentId,
                                      HttpServletRequest httpServletRequest){

        return commentService.deleteComment(commentId, httpServletRequest);

    }


}
