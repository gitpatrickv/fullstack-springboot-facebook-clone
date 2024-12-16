package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.NotificationModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.entity.Notification;
import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostLike;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.NotificationType;
import com.springboot.fullstack_facebook_clone.repository.NotificationRepository;
import com.springboot.fullstack_facebook_clone.repository.PostLikeRepository;
import com.springboot.fullstack_facebook_clone.service.NotificationService;
import com.springboot.fullstack_facebook_clone.service.PostLikeService;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.Pagination;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.NotificationMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final Pagination pagination;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    @Override
    public void likePost(Long postId) {
        User user = userService.getCurrentAuthenticatedUser();
        Post post = postService.getPost(postId);
        Optional<PostLike> optionalPostLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, user.getUserId());

        if (optionalPostLike.isPresent()) {
            postLikeRepository.deleteByPost_PostIdAndUser_UserId(postId, user.getUserId());
            notificationRepository.deleteByPost_PostIdAndSender_UserId(postId, user.getUserId());
        } else {
            PostLike postLike = new PostLike();
            postLike.setTimestamp(LocalDateTime.now());
            postLike.setPost(post);
            postLike.setUser(user);
            postLikeRepository.save(postLike);

            if(!post.getUser().getUserId().equals(user.getUserId())) {
                Notification notification = new Notification();
                notification.setMessage(StringUtil.LIKES_YOUR_POST);
                notification.setRead(false);
                notification.setNotificationType(NotificationType.POST_LIKED);
                notification.setTimestamp(LocalDateTime.now());
                notification.setPost(post);
                notification.setReceiver(post.getUser());
                notification.setSender(user);

                Notification savedNotification = notificationRepository.save(notification);

                NotificationModel notificationModel = notificationMapper.mapEntityToModel(savedNotification);
                notificationModel.setContent(post.getContent());
                notificationModel.getSender().setUniqueId(user.getUserId() + 1000);

                notificationService.sendNotification(post.getUser().getEmail(), notificationModel);
            }
        }
    }

    @Override
    public LikeResponse getPostLike(Long postId) {
        User user = userService.getCurrentAuthenticatedUser();
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
        PageResponse pageResponse = pagination.getPagination(postLikes);

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

}

