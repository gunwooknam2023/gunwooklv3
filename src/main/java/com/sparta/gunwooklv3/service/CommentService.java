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
    @Transactional // 트랜잭션 처리
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest); // 토큰을 검증한다.

        Post post = postRepository.findById(postId).orElseThrow( // 선택한 게시글의 유무를 판단한다.
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.") // 없을시 예외처리 throw
        );

        Comment comment = new Comment(user, commentRequestDto, post); // 코멘트에 유저정보, 댓글내용, 게시글번호를 담아준다.
        commentRepository.save(comment); // 코멘트를 저장한다.

        return new CommentResponseDto(comment); // CommentResponseDto을 생성하여 반환하여준다.
    }


    // 댓글 수정 API
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest); // 토큰 검증

        Comment comment = commentRepository.findById(commentId).orElseThrow( // 댓글번호를 넘겨주어 댓글의 유무를 파악한다.
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.") // 없을시 예외처리 throw
        );

        // 댓글의 작성자와 유저의 작성자가 일치하는지, 권한이 admin인지 확인후 둘중 하나라도 만족시 실행한다.
        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto); // 댓글내용을 업데이트한다.
            return new CommentResponseDto(comment); // CommentResponseDto형식으로 리턴한다.
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다."); // 위 조건에 맞지않으면 예외처리를 throw한다.
        }
    }


    // 댓글 삭제 API
    public StatusResult deleteComment(Long commentId, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest); // 토큰을 검증한다.

        Comment comment = commentRepository.findById(commentId).orElseThrow( // 댓글이 존재하는지 체크한다.
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.") // 존재하지않으면 예외처리 throw
        );

        // 댓글의 작성자와 유저의 작성자가 일치하는지, 권한이 admin인지 확인후 둘중 하나라도 만족시 실행한다.
        if(comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment); // 조건이 성립시 Repository를 사용하여 댓글을 삭제한다.
            return new StatusResult("삭제 성공", 200); // 삭제되었다는 메세지를 출력하고 코드를 반환한다.
        } else {
            return new StatusResult("작성자만 삭제할 수 있습니다.", 400); // 조건과 부합하지않으면 출력한다.
        }

    }


    // 토큰 검증
    // HttpServletRequest를 사용하여 토큰을 가져온다.
    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.resolveToken(request); // token에 토큰을 대입하여준다.
        Claims claims;

        if(token != null){ // 토큰이 없을시 실행
            if(jwtUtil.validateToken(token)){ // 토큰이 유효한지 확인.
                claims = jwtUtil.getUserInfoFromToken(token); // 유효하다면 claims에 넣어준다.
            } else {
                throw new IllegalArgumentException("Token Error"); // 유효하지않다면 예외처리를 throw 한다.
            }

            // 사용자 정보를 조회한다.
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.") // 존재하지않으면 예외처리 throw
                    );
            return user;
        }
        return null;
    }


}
