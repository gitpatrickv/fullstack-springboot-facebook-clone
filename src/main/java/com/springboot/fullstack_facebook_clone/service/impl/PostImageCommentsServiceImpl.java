package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.PostImage;
import com.springboot.fullstack_facebook_clone.entity.PostImageComments;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostImageCommentsRepository;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageCommentsService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PostImageCommentsServiceImpl implements PostImageCommentsService {

    private final UserRepository userRepository;
    private final PostImageCommentsRepository postImageCommentsRepository;
    private final PostImageRepository postImageRepository;
    private final UserService userService;
    @Override
    public void writePostImageComment(String email, Long postImageId, String comment, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        PostImage postImage = postImageRepository.findById(postImageId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_IMAGE_NOT_FOUND + postImageId));

        PostImageComments postImageComments = new PostImageComments();
        postImageComments.setComment(comment);
        if(file != null) {
            postImageComments.setCommentImage(userService.processUserImage(user.getEmail(), file));
        }
        postImageComments.setUser(user);
        postImageComments.setPostImage(postImage);
        postImageComments.setTimestamp(LocalDateTime.now());
        postImageCommentsRepository.save(postImageComments);
    }
}
