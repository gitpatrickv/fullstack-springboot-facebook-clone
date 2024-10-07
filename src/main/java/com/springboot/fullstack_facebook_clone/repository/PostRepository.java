package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUser_UserId(Long userId, Pageable pageable);
//    @Query("SELECT p FROM Post p WHERE p.user.email = :email")
//    List<Post> findAllByUserEmail(@Param("email") String email);

//    List<Post> findAllBySharedPost_PostId(Long postId);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.sharedPost.postId = :postId AND p.sharedImage IS NULL")
    Long countSharedPost(@Param("postId") Long postId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.sharedImage.postImageId = :postImageId")
    Long countSharedPostImage(@Param("postImageId") Long postImageId);
}
