package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
//아무 메서드도 없는데 동작하는 이유: extends JpaRepository<Post, Long>는 기본적인 CRUD 메서드를 제공하기 때문
   Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
