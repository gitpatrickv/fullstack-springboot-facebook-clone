package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;
import com.springboot.fullstack_facebook_clone.entity.PostImage;
import com.springboot.fullstack_facebook_clone.entity.PostImageLikes;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostImageLikesRepository;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageLikeService;
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
public class PostImageLikeServiceImpl implements PostImageLikeService {

    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final PostImageLikesRepository postImageLikesRepository;
    @Transactional
    @Override
    public void likePostImage(String email, Long postImageId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        PostImage postImage = postImageRepository.findById(postImageId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_IMAGE_NOT_FOUND + postImageId));
        Optional<PostImageLikes> optionalPostImageLikes = postImageLikesRepository.findByPostImage_PostImageIdAndUser_UserId(postImageId, user.getUserId());

        if(optionalPostImageLikes.isPresent()){
            postImageLikesRepository.deleteByPostImage_PostImageIdAndUser_UserId(postImageId, user.getUserId());
        } else {
            PostImageLikes postImageLikes = new PostImageLikes();
            postImageLikes.setTimestamp(LocalDateTime.now());
            postImageLikes.setPostImage(postImage);
            postImageLikes.setUser(user);
            postImageLikesRepository.save(postImageLikes);
        }
    }

    @Override
    public LikeResponse getPostImageLike(String email, Long postImageId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<PostImageLikes> postLike = postImageLikesRepository.findByPostImage_PostImageIdAndUser_UserId(postImageId, user.getUserId());

        LikeResponse likeResponse = new LikeResponse();

        likeResponse.setLiked(postLike.isPresent());

        return likeResponse;
    }

    @Override
    public PostLikeCountResponse getPostImageLikeCount(Long postImageId) {

        Long postImageLikes = postImageLikesRepository.countPostImageLike(postImageId);
        PostLikeCountResponse postLikeCountResponse = new PostLikeCountResponse();
        postLikeCountResponse.setPostLikeCount(postImageLikes);

        return postLikeCountResponse;
    }

    @Override
    public List<PostLikeUserListResponse> getPostImageLikeUserList(Long postImageId) {
        return  postImageLikesRepository.findAllByPostImage_PostImageId(postImageId)
                .stream()
                .map(users -> {
                    PostLikeUserListResponse userList = new PostLikeUserListResponse();
                    userList.setPostLikeId(users.getPostImageLikeId());
                    userList.setUserId(users.getUser().getUserId());
                    userList.setFirstName(users.getUser().getFirstName());
                    userList.setLastName(users.getUser().getLastName());
                    return userList;
                })
                .toList();
    }
}