package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId);
    void deleteByPost_PostIdAndUser_UserId(Long postId, Long userId);
    Page<PostLike> findAllByPost_PostId(Long postId, Pageable pageable);
    @Query("SELECT COUNT(c) FROM PostLike c WHERE c.post.postId = :postId")
    Long countPostLike(@Param("postId") Long postId);
}
