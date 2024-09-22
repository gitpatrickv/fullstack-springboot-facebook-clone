package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUser_Email(String email);
//    @Query("SELECT p FROM Post p WHERE p.user.email = :email")
//    List<Post> findAllByUserEmail(@Param("email") String email);
}
