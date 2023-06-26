package com.sparta.gunwooklv3.repository;

import com.sparta.gunwooklv3.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
