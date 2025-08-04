package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;

public interface LikeService {
    void toggleLike(User user, Post post);
    boolean isLiked(User user, Post post);
    long countLikes(Post post);
}
