package com.example.demo.domain;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="users")
public class User{
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotBlank(message="이름을 입력해주세요.")
    private Long id;

    @NotBlank(message="아이디를 입력해주세요.")
    @Column(unique = true)
    private String username;

    @NotBlank(message="비밀번호를 입력해주세요.")
    private String password;
}
