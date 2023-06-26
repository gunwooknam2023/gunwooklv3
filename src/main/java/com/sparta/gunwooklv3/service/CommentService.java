package com.sparta.gunwooklv3.service;

import com.sparta.gunwooklv3.dto.CommentRequestDto;
import com.sparta.gunwooklv3.dto.CommentResponseDto;
import com.sparta.gunwooklv3.dto.StatusResult;
import com.sparta.gunwooklv3.entity.Comment;
import com.sparta.gunwooklv3.entity.Post;
import com.sparta.gunwooklv3.entity.User;
import com.sparta.gunwooklv3.entity.UserRoleEnum;
import com.sparta.gunwooklv3.jwt.JwtUtil;
import com.sparta.gunwooklv3.repository.CommentRepository;
import com.sparta.gunwooklv3.repository.PostRepository;
import com.sparta.gunwooklv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    // 댓글 작성 API
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = new Comment(user, commentRequestDto, post);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }


    // 댓글 수정 API
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
    }


    // 댓글 삭제 API
    public StatusResult deleteComment(Long commentId, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if(comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment);
            return new StatusResult("삭제 성공", 200);
        } else {
            return new StatusResult("작성자만 삭제할 수 있습니다.", 400);
        }

    }

    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                    );
            return user;
        }
        return null;
    }


}
