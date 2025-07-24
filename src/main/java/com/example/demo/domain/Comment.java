package com.example.demo.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private LocalDateTime createdAt= LocalDateTime.now();





}
