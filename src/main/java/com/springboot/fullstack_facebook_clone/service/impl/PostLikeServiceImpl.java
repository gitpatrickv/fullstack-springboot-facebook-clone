package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostLike;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostLikeRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostLikeService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void likePost(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_NOT_FOUND + postId));
        Optional<PostLike> optionalPostLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, user.getUserId());

        if (optionalPostLike.isPresent()) {
            postLikeRepository.deleteByPost_PostIdAndUser_UserId(postId, user.getUserId());
        } else {
            PostLike postLike = new PostLike();
            postLike.setTimestamp(LocalDateTime.now());
            postLike.setPost(post);
            postLike.setUser(user);
            postLikeRepository.save(postLike);
        }
    }

}

