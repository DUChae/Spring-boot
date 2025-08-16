// src/main/java/com/example/demo/service/CommentService.java
package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);
    List<Comment> findByPost(Post post);
    Comment findById(Long id);
    void deleteById(Long id);
    List<CommentResponseDto> getComments(Long postId);
    Comment createComment(Long postId, CommentRequestDto requestDto, User user);
    Comment updateComment(Long commentId, String newContent,User user);
    void deleteComment(Long commentId, String username);
}