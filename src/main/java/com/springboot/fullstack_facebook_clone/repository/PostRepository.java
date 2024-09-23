package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUser_Email(String email, Pageable pageable);
//    @Query("SELECT p FROM Post p WHERE p.user.email = :email")
//    List<Post> findAllByUserEmail(@Param("email") String email);
}
