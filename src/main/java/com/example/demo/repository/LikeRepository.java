package com.example.demo.repository;

import com.example.demo.domain.Like;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndPost(User user , Post post);

    void deleteByUserAndPost(User user , Post post);
    long countByPost(Post post);
}
