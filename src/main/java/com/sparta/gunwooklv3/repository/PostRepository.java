package com.sparta.gunwooklv3.repository;

import com.sparta.gunwooklv3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// JpaRepository를 받아 확장한다. Post 클래스의 인스턴스를 Long 형식의 기본키로 관리하여준다.
public interface PostRepository extends JpaRepository<Post, Long> {

    // Post 객체의 리스트를 반환하여준다.
    // 모든게시물을 찾아오고, 수정된 시간을 기준으로 내림차순으로 정렬해온다.
    List<Post> findAllByOrderByModifiedAtDesc();
}