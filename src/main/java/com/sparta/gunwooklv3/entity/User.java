package com.sparta.gunwooklv3.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 번호

    @Column(nullable = false, unique = true)
    private String username; // 유저이름

    @Column(nullable = false)
    private String password; // 비밀번호

    public User(String username, String password){
        this.username = username; // 유저이름 대입
        this.password = password; // 비밀번호 대입
    }
}