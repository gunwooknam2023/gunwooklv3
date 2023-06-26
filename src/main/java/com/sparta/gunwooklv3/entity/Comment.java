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
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    public Comment(User user, CommentRequestDto commentRequestDto, Post post){
        this.post = post;
        this.user = user;
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto){
        this.comment = commentRequestDto.getComment();
    }

}
