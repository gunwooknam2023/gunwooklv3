package com.sparta.gunwooklv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.gunwooklv3.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글번호

    @Column(nullable = false)
    private String comment; // 댓글내용

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    public Comment(User user, CommentRequestDto commentRequestDto, Post post){
        this.post = post; // 가져온 정보를 주입
        this.user = user; // 가져온 정보를 주입
        this.comment = commentRequestDto.getComment(); // 가져온 정보를 주입
    }

    public void update(CommentRequestDto commentRequestDto){
        this.comment = commentRequestDto.getComment(); // 가져온 정보를 주입
    }

}
