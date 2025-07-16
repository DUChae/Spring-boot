package com.example.demo.service;

import com.example.demo.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<Post> findAll(Pageable pageable);
    Optional<Post> findById(Long id);
    Post save(Post post);
    void deleteById(Long id);
    Page<Post> findByTitle(String keword, Pageable pageable);
}
