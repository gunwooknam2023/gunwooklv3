package com.sparta.gunwooklv3.dto;

import com.sparta.gunwooklv3.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id; // 댓글번호
    private String comment; // 댓글내용
    private String username; // 유저이름
    private LocalDateTime createAt; //생성시간
    private LocalDateTime modifiedAt; // 수정시간

    public CommentResponseDto(Comment comment){
        this.id = comment.getId(); // 받아온값을 주입
        this.comment = comment.getComment(); // 받아온값을 주입
        this.username = comment.getComment(); // 받아온값을 주입
        this.createAt = comment.getCreatedAt(); // 받아온값을 주입
        this.modifiedAt = comment.getModifiedAt(); // 받아온값을 주입
    }

}
