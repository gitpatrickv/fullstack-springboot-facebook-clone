package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Long postLikes = postLikeRepository.countPostLike(postId);
        PostLikeCountResponse postLikeCountResponse = new PostLikeCountResponse();
        postLikeCountResponse.setPostLikeCount(postLikes);

        return postLikeCountResponse;
    }

    @Override
    public UserListResponse getPostLikeUserList(Long postId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostLike> postLikes = postLikeRepository.findAllByPost_PostId(postId, pageable);
        PageResponse pageResponse = this.getPagination(postLikes);

        List<UserDataModel> userDataModels = new ArrayList<>();

        for(PostLike postLike : postLikes){
            UserDataModel userDataModel = new UserDataModel();
            userDataModel.setUniqueId(postLike.getPostLikeId());
            userDataModel.setUserId(postLike.getUser().getUserId());
            userDataModel.setFirstName(postLike.getUser().getFirstName());
            userDataModel.setLastName(postLike.getUser().getLastName());
            userDataModel.setProfilePicture(postLike.getUser().getProfilePicture());
            userDataModels.add(userDataModel);
        }

        return new UserListResponse(userDataModels,pageResponse);
    }

    private PageResponse getPagination(Page<PostLike> postLikes){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(postLikes.getNumber());
        pageResponse.setPageSize(postLikes.getSize());
        pageResponse.setTotalElements(postLikes.getTotalElements());
        pageResponse.setTotalPages(postLikes.getTotalPages());
        pageResponse.setLast(postLikes.isLast());
        return pageResponse;
    }

}

