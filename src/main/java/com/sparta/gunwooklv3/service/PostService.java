package com.sparta.gunwooklv3.service;

import com.sparta.gunwooklv3.dto.PostRequestDto;
import com.sparta.gunwooklv3.dto.PostResponseDto;
import com.sparta.gunwooklv3.entity.Post;
import com.sparta.gunwooklv3.entity.User;
import com.sparta.gunwooklv3.jwt.JwtUtil;
import com.sparta.gunwooklv3.repository.PostRepository;
import com.sparta.gunwooklv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service // 해당클래스를 빈 컨테이너로 등록해주는 어노테이션
@RequiredArgsConstructor // 롬복의 이 기능을 사용하면 final이 붙은 필드로 생성자를 한개 만들어준다. @Autowired가 자동으로 붙는다.
public class PostService {

    private final PostRepository postRepository; // 필드정의
    private final UserRepository userRepository; // 필드정의
    private final JwtUtil jwtUtil; // 필드정의

    // 게시글 작성 API
    @Transactional // 현재 메서드를 트랜잭션으로 관리
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        User user = checkToken(request); // 받아온 request안의 토큰정보를 추출하여 user에 넣어준다.

        if(user == null){ // 사용자의 user정보가 존재하지않으면 예외를 throw한다.
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }
        Post post = new Post(requestDto, user); // post에 받아온 requestDto와 유저정보를 입력한다.
        postRepository.save(post); // requestDTo와 user가 담긴 post를 Repository를 통해 저장한다.
        return new PostResponseDto(post); // 저장된 게시물을 PostResponseDto 안에 넣어서 반환한다.
    }

    // 전체 게시글 목록 조회 API
    @Transactional
    public List<PostResponseDto> getPost() {
        // findAllByOrderByModifiedAtDesc를 통해 최신 수정일자를 기준으로 모든 게시물을 가져온다.
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        // PostResponseDto 형식으로 리스트를 생성
        List<PostResponseDto> postResponseDto = new ArrayList<>();

        for(Post post : posts){ // 받아온 모든 게시물만큼 반복한다.
            postResponseDto.add(new PostResponseDto(post)); // 각 게시물을 postResponseDto 형식으로 변환하여 넣어준다.
        }

        return postResponseDto; // 변환된 postResponseDto를 반환한다.
    }

    @Transactional
    // 선택한 게시글 조회 API
    public PostResponseDto getPost(Long id) {
        // findById를 사용하여 id에 해당하는 게시물을 찾아서 Post형식의 post에 저장한다.
        Post post = postRepository.findById(id).orElseThrow(
                // 일치하는 아이디가 존재하지않으면 예외처리를 throw 한다.
                () -> new IllegalArgumentException("아이디가 일치하지 않습니다")
        );

        return new PostResponseDto(post); // post를 PostResponseDto안에 담아 반환한다.
    }

    @Transactional
    //  선택한 게시글 수정 API
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크
        User user = checkToken(request); // request안에 담긴 JWT토큰을 가져온다.

        if(user == null){ // 인증되지 않는 사용자일때
            throw new IllegalArgumentException("인증되지 않은 사용자입니다."); // 예외처리를 throw한다.
        }

        Post post = postRepository.findById(id).orElseThrow( // id에맞는 게시물을 post에 입력받는다.
                () -> new NullPointerException("해당 글이 존재하지 않습니다.") // 존재하지않으면 예외처리를 throw한다.
        );

        if(!post.getUser().equals(user)){ // post에 담긴 user와 받아온 user정보가 일치하는지 확인한다.
            throw new IllegalArgumentException("글 작성자가 아닙니다."); // 일치하지않으면 예외처리를 throw 한다.
        }

        post.update(requestDto); // 게시글접오를 받아온 requestDto로 업데이트 한다.
        return new PostResponseDto(post); // 업데이트된 게시물을 PostResponseDto형태로 변환하여 반환한다.
    }

    @Transactional
    // 선택한 게시글 삭제 API
    public void deletePost(Long id, HttpServletRequest request) {

        // 토큰 체크
        User user = checkToken(request); // 받아온 request에서 jwt정보를 추출한다.

        if(user == null){ // 인증값이 없을시.
            throw new IllegalArgumentException("인증되지 않은 사용자입니다."); // 예외처리 throw
        }

        Post post = postRepository.findById(id).orElseThrow( // id에 대한 글이 없을시.
                () -> new NullPointerException("해당 글이 존재하지 않습니다.") // 예외처리 throw
        );

        if(post.getUser().equals(user)){ // 게시글의 user정보와 요청한 user정보를 비교한다.
            postRepository.delete(post); // 일치시 postRepository를 이용하여 post정보를 삭제한다.
        }
    }


    // JWT토큰을 확인하고 사용자를 인증하는 메서드.
    public User checkToken(HttpServletRequest request){

        // 클라이언트의 요청에서 JWT토큰 가지고 오기
        String token = jwtUtil.resolveToken(request); // token에 JWT토큰 대입
        Claims claims;

        // 추출된 토큰이 null이 아닐경우 토큰 유효성 검사 실행
        if(token != null){
            // JWT 토큰 유효성 검사
            if(jwtUtil.validateToken(token)){
                // 토큰이 유효한 경우에 getUserInfoFromToken을 사용하여 토큰에서 사용자 정보 추출
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                // 토큰이 존재하지 않으면 Token Error라는 메세지를 출력하며 예외를 발생시킨다.
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );
            return user;

        }
        // 토큰이 없을경우 null을 반환해준다.
        return null;
    }
}