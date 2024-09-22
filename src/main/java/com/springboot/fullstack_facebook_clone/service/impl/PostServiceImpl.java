package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;

    @Override
    public void createPost(String email, String content, MultipartFile[] files) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        Post post = new Post();
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        if (files != null && files.length > 0) {
            postImageService.uploadPostImages(savedPost.getPostId(), files);
        }
    }
}
