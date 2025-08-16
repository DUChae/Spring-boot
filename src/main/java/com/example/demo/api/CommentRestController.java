package com.example.demo.api;


import com.example.demo.domain.User;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentRestController {
    private final CommentService commentService;
    private final UserRepository userRepository;
    //댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    //댓글 등록
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long postId,
                                           @RequestBody CommentRequestDto dto,
                                           @AuthenticationPrincipal UserDetails userDetails){
        User user= userRepository.findByUsername("test")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        commentService.createComment(postId, dto, user);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal UserDetails userDetails){
        String username="test";
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok().build();
    }

}
