package com.example.demo.domain;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    @Size(max=100,message="작성자 이름은 100자 이내로 입력해주세요.")
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
