package com.sparta.gunwooklv3.entity;


import com.sparta.gunwooklv3.dto.PostRequestDto;
import com.sparta.gunwooklv3.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped { // 타임스태프를 상속하여 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 번호

    @Column(nullable = false) // null 불가
    private String title; // 게시글 제목

    @Column(nullable = false) // null 불가
    private String contents; // 게시글 내용

    @Column(nullable = false) // null 불가
    private String username; // 유저이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = true, updatable = true)

    private User user;

    public Post(PostRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
        this.username = user.getUsername();
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }



}