package com.example.demo.service;


import com.example.demo.domain.Like;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void toggleLike(User user, Post post){
        if(likeRepository.existsByUserAndPost(user,post)){
            likeRepository.deleteByUserAndPost(user,post);
        }else{
            Like like=new Like();
            like.setUser(user);
            like.setPost(post);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
        }
    }

    @Override
    public boolean isLiked(User user, Post post){
        return likeRepository.existsByUserAndPost(user, post);
    }
    @Override
    public long countLikes(Post post){
        return likeRepository.countByPost(post);
    }

}
