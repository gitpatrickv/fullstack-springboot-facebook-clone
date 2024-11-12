package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findAllByPost_PostId(Long postId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM PostComment c WHERE c.post.postId = :postId")
    Long countPostComment(@Param("postId") Long PostId);

    @Query("SELECT p FROM PostComment p WHERE p.post.postId = :postId ORDER by p.timestamp DESC")
    List<PostComment> findLastComment(@Param("postId") Long postId, Pageable pageable);
}
