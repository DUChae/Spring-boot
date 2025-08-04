package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.LikeService;
import com.example.demo.service.PostServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostServiceImpl postService;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeService likeService;
    private User user;

    @Autowired
    public PostController(PostServiceImpl postService,
                          UserRepository userRepository,
                          PostRepository postRepository,
                          LikeService likeService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeService = likeService;
    }

    //게시글 전체 조회
    @GetMapping
    public String listPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value= "keyword", required = false) String keyword,
            Model model){
        Pageable pageable= PageRequest.of(page,size);
        Page<Post> postPage=postService.findAll(pageable);

        if(keyword!=null&&!keyword.isBlank()){
            postPage=postService.findByTitle(keyword,pageable);
        }else{
            postPage=postService.findAll(pageable);
        }

        model.addAttribute("postPage",postPage);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", postPage.getTotalPages());
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
    public String createPost(@Valid @ModelAttribute("post") Post postForm,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (bindingResult.hasErrors()) {
            return "post/new";
        }

        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());

        // ✅ 로그인 유저 바로 설정
        post.setAuthor(customUserDetails.getUser());

        postService.save(post);
        return "redirect:/posts";
    }
    //특정 글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model){
        Post post=postService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));
        model.addAttribute("post",post);


        //좋아요 수
        model.addAttribute("likeCount",likeService.countLikes(post));
        model.addAttribute("isLiked",likeService.isLiked(user,post));
        return "post/view";
    }

    //글 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Post post=postService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));

        if(!post.getAuthor().getId().equals(customUserDetails.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"수정 권한이 없습니다.");
        }
        model.addAttribute("post",post);
        return "post/edit";
    }


    //글 수정
    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id,@Valid @ModelAttribute Post post, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("post", post);
            return "post/edit"; //유효성 검사 실패 시 수정 폼으로 돌아감
        }
        Post existingPost=postService.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물을 찾을 수 없습니다."));
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.save(existingPost);
        return "redirect:/posts/" + id;}

    //글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id){
        postService.deleteById(id);
        return "redirect:/posts";
    }


    @PostMapping("/{postId}/like")
    public String toggleLike(@PathVariable Long postId,
                             @AuthenticationPrincipal UserDetails userDetails){
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("게시글 없음"));
        User user=userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()->new IllegalArgumentException("유저 없음"));

        likeService.toggleLike(user,post);
        return "redirect:/posts/" + postId;
    }



}
