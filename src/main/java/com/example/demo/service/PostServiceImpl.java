package com.example.demo.service;


import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;


    //생성자 주입 방식으로 PostRepository를 주입
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //게시글 전체 조회
@Override
public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
}

    //게시글 단건 조회
    @Override
    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    //게시글 저장 (새 글 작성)
    @Override
    public Post save(Post post){
        return postRepository.save(post);
    }

    //게시글 수정

    public Post updatePost(Long id,Post updatedPost){
        return postRepository.findById(id)
                .map(post->{
                    post.setTitle(updatedPost.getTitle());
                    post.setContent(updatedPost.getContent());
                    post.setAuthor(updatedPost.getAuthor());
                    return postRepository.save(post);
                })
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    //게시글 삭제
    @Override
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

}
