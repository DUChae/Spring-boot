package com.example.demo.controller;


import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다: " + postId));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        CommentRequestDto requestDto=new CommentRequestDto();
        requestDto.setContent(content);
        commentService.createComment(postId,requestDto,user);

        return "redirect:/posts/" + postId;
    }


    //댓글 수정 폼 요청
    @GetMapping("/comments/{id}/edit")
    public String editCommentForm(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails){

        Comment comment=commentService.findById(id);
        if(!comment.getAuthor().getUsername().equals(userDetails.getUsername())){
            throw new IllegalArgumentException("해당 댓글을 수정할 권한이 없습니다.");
        }
        model.addAttribute("comment",comment);
        return "comment/edit";
    }


    //댓글 수정 처리
    @PostMapping("/comments/{id}/edit")
    public String editComment(@PathVariable Long id,
                              @RequestParam String content,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user=userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
      Comment udpateComment=commentService.updateComment(id,content,user);


      return "redirect:/posts/" + udpateComment.getPost().getId();
    }

    //댓글 삭제
    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails){
        Comment comment=commentService.findById(id);
        if(!comment.getAuthor().getUsername().equals(userDetails.getUsername())){
            throw new IllegalArgumentException("해당 댓글을 삭제할 권한이 없습니다.");
        }
        commentService.deleteById(id);
        return "redirect:/posts/" + comment.getPost().getId();
    }

}






