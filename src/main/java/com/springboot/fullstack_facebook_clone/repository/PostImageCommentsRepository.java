package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostImageComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageCommentsRepository extends JpaRepository<PostImageComments, Long> {
    Page<PostImageComments> findAllByPostImage_PostImageId(Long postImageId, Pageable pageable);
}
