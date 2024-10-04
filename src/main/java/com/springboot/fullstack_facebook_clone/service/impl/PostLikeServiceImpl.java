package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;
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
import java.util.List;
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

    @Override
    public LikeResponse getPostLike(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<PostLike> postLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, user.getUserId());

        LikeResponse likeResponse = new LikeResponse();

        likeResponse.setLiked(postLike.isPresent());

        return likeResponse;
    }

    @Override
    public PostLikeCountResponse getPostLikeCount(Long postId) {

        Long postLikes = postLikeRepository.findAllByPost_PostId(postId)
                .stream()
                .count();

        PostLikeCountResponse postLikeCountResponse = new PostLikeCountResponse();
        postLikeCountResponse.setPostLikeCount(postLikes);

        return postLikeCountResponse;
    }

    @Override
    public List<PostLikeUserListResponse> getPostLikeUserList(Long postId) {
        return postLikeRepository.findAllByPost_PostId(postId)
                .stream()
                .map(users -> {
                    PostLikeUserListResponse userList = new PostLikeUserListResponse();
                    userList.setPostLikeId(users.getPostLikeId());
                    userList.setUserId(users.getUser().getUserId());
                    userList.setFirstName(users.getUser().getFirstName());
                    userList.setLastName(users.getUser().getLastName());
                    return userList;
                })
                .toList();
    }

}

