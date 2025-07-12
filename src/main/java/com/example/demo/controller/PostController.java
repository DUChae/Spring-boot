package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    //게시글 전체 조회
    @GetMapping
    public String listPosts(Model model){
        model.addAttribute("posts",postService.findAll());
        return "post/list";
    }

    //새 글 작성 폼
    @GetMapping("/new")
    public String newPostForm(Model model){
        model.addAttribute("post",new Post());
        return "post/new";
    }

    //새 글 저장
    @PostMapping
    public String createPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "post/new"; //유효성 검사 실패 시 새 글 작성 폼으로 돌아감
        }
        postService.save(post);
        return "redirect:/posts";
    }

    //특정 글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model){
        Post post=postService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));
        model.addAttribute("post",post);
        return "post/view";
    }

    //글 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model){
        Post post=postService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));

        model.addAttribute("post",post);
        return "post/edit";
    }


    //글 수정
    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post){
        Post existingPost=postService.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setAuthor(post.getAuthor());
        postService.save(existingPost);
        return "redirect:/posts/" + id;}

    //글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id){
        postService.deleteById(id);
        return "redirect:/posts";
    }




}
