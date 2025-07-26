// src/main/java/com/example/demo/service/CommentService.java
package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;

import java.util.List;

public interface CommentService {
    void save(Comment comment);
    List<Comment> findByPost(Post post);

    Comment findById(Long id);

}