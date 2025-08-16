// src/main/java/com/example/demo/service/CommentServiceImpl.java
package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

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


    @Override
    public Comment createComment(Long postId, CommentRequestDto requestDto, User user){
        Post post=postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
        Comment comment=new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(requestDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long commentId,String newContent,User user){
        Comment comment=findById(commentId);
        if(!comment.getAuthor().getId().equals(user.getId())){
            throw new IllegalArgumentException("해당 댓글을 수정할 권한이 없습니다.");
        }
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return save(comment);
    }
    @Override
    public void deleteComment(Long commentId, String username){
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
        if(!comment.getAuthor().getUsername().equals(username)){
            throw new IllegalArgumentException("해당 댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }

}