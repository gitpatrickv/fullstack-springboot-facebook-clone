package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId);
    void deleteByPost_PostIdAndUser_UserId(Long postId, Long userId);
}
