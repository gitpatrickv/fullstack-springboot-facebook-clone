package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.PostMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;
    private final PostMapper postMapper;
    private final PostImageRepository postImageRepository;
    @Transactional
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
    public PostListResponse fetchAllUserPosts(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Post> posts = postRepository.findAllByUser_UserId(userId, pageable);
        PageResponse pageResponse = this.getPagination(posts);

        List<PostModel> postModelList = new ArrayList<>();

        for(Post post : posts){
            PostModel postModel = this.getPostById(post,postMapper);
            postModelList.add(postModel);

            if(post.getSharedPost() != null){
                Post sharedPost = post.getSharedPost();
                PostModel sharedPostModel = this.getPostById(sharedPost, postMapper);
                postModel.setSharedPost(sharedPostModel);
            }
        }
        return new PostListResponse(postModelList, pageResponse);
    }

    @Override
    public void sharePost(String email, Long postId, SharePostRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Post> sharedPost = postRepository.findById(postId);

        if(sharedPost.isPresent()) {
            Post post = new Post();
            post.setContent(request.getContent());
            post.setTimestamp(LocalDateTime.now());
            post.setUser(user);
            post.setSharedPost(sharedPost.get());
            postRepository.save(post);
        }
    }

    private PostModel getPostById(Post post, PostMapper postMapper) {
        PostModel postModel = postMapper.mapEntityToModel(post);
        postModel.setFirstName(post.getUser().getFirstName());
        postModel.setLastName(post.getUser().getLastName());
        postModel.setProfilePicture(post.getUser().getProfilePicture());
        return postModel;
    }

    private PageResponse getPagination(Page<Post> posts){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(posts.getNumber());
        pageResponse.setPageSize(posts.getSize());
        pageResponse.setTotalElements(posts.getTotalElements());
        pageResponse.setTotalPages(posts.getTotalPages());
        pageResponse.setLast(posts.isLast());
        return pageResponse;
    }
}
