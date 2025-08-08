package com.example.demo.api;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.PostCreateRequestDto;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostServiceImpl postService;
    private final UserRepository userRepository;

    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.findAll().stream()
                .map(PostResponseDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다: " + id));
        return new PostResponseDto(post);
    }

    @PostMapping
    public String createPost(@RequestBody PostCreateRequestDto dto) {
        System.out.println("✅ POST 요청 도착: " + dto.getTitle());

        // 예외 대신 null을 반환하여 프로그램이 중단되지 않도록 함
        User user = userRepository.findById(1L).orElse(null);

        // 유저가 없는 경우 예외를 발생시키지 않고 처리
        if (user == null) {
            return "오류: 게시글을 작성할 유저가 없습니다.";
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        postService.save(post);

        return "게시글이 성공적으로 생성되었습니다.";
    }
}