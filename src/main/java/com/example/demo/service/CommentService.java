package com.example.demo.service;


import com.example.demo.domain.Comment;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public List<Comment> getCommentsByPostId(Long postId){
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }
}
