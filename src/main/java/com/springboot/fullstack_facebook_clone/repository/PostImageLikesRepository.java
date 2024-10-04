package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostImageLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageLikesRepository extends JpaRepository<PostImageLikes, Long> {

    Optional<PostImageLikes> findByPostImage_PostImageIdAndUser_UserId(Long postImageId, Long userId);
    void deleteByPostImage_PostImageIdAndUser_UserId(Long postImageId, Long userId);
    List<PostImageLikes> findAllByPostImage_PostImageId(Long postImageId);
}
