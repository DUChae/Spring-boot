package com.example.demo.api;


import com.example.demo.domain.Post;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostServiceImpl postService;

    // PostRestController.java
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.findAll().stream()
                .map(PostResponseDto::new)
                .toList();
    }

}
