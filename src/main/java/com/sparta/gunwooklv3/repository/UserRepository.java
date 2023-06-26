package com.sparta.gunwooklv3.repository;

import com.sparta.gunwooklv3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// JpaRepository를 받아 확장한다. User 클래스의 인스턴스를 Long 형식의 기본키로 관리하여준다.
public interface UserRepository extends JpaRepository<User, Long> {

    // User 객체를 optional 형식으로 반환.
    // 사용자 이름을 받아와서 사용자를 찾아준다.
    // 값이 존재할수도 있고 않을수도있다. 따라서 반환된 Optional 객체를 사용하여 확인할수있다.
    Optional<User> findByUsername(String username);
}