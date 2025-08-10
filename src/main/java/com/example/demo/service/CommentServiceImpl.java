// src/main/java/com/example/demo/service/CommentServiceImpl.java
package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<Comment> findByPost(Post post) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId());
    }

    @Override
    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Comment not found"));
    }

    @Override
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentResponseDto> getComments(Long postId) {

        List<Comment> comments=commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(comment->{
                    CommentResponseDto dto=new CommentResponseDto();
                    dto.setId(comment.getId());
                    dto.setContent(comment.getContent());
                    dto.setAuthorName(comment.getAuthor().getUsername());
                    dto.setCreatedAt(comment.getCreatedAt().toString());
                    return dto;

                })
                .collect(Collectors.toList());
    }
}