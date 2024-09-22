package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.PostImageModel;
import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostImage;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;
    private final PostMapper postMapper;
    private final PostImageRepository postImageRepository;

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

    @Override
    public List<PostModel> fetchAllUserPosts(String email) {

        List<Post> posts = postRepository.findAllByUser_Email(email);
        List<PostModel> postModelList = new ArrayList<>();

        for(Post post : posts){
            PostModel postModel = postMapper.mapEntityToModel(post);
            postModelList.add(postModel);

            List<PostImageModel> postImageList = new ArrayList<>();

            if(!post.getPostImages().isEmpty()){
                List<PostImage> postImages = postImageRepository.findAllByPost_PostId(post.getPostId());

                for(PostImage postImage : postImages){
                    PostImageModel postImageModel = new PostImageModel();
                    postImageModel.setPostImageId(postImage.getPostImageId());
                    postImageModel.setPostImageUrl(postImage.getPostImageUrl());

                    postImageList.add(postImageModel);
                }
            }

            postModel.setPostImages(postImageList);
        }

        return postModelList;
    }




}
