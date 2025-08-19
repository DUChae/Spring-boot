package com.example.demo.api;


import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeRestController {

    private final UserRepository userRepository;
    private final LikeService likeService;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Void> addLike(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetails userDetails){
        User user=userRepository.findByUsername("test")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다: "));
        likeService.toggleLike(user,post);
        return ResponseEntity.ok().build();
    }
}
