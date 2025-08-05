package com.example.demo.dto;


import com.example.demo.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getAuthor().getUsername();
        this.createdAt = post.getCreatedAt();
    }
}
