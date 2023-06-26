package com.sparta.gunwooklv3.repository;

import com.sparta.gunwooklv3.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 받아 확장한다. Comment 클래스의 인스턴스를 Long 형식의 기본키로 관리하여준다.
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
