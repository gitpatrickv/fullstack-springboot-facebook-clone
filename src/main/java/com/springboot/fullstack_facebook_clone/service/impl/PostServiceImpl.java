package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.*;
import com.springboot.fullstack_facebook_clone.entity.Friendship;
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
import com.springboot.fullstack_facebook_clone.utils.mapper.SharedPostMapper;
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
    private final SharedPostMapper sharedPostMapper;
    private final PostImageRepository postImageRepository;
    @Transactional
    @Override
    public void createPost(String email, Long userId, String content, MultipartFile[] files) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        User guestPoster = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));
        Long currentUserId = user.getUserId();

        Post post = new Post();
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());

        if(currentUserId.equals(userId)) {
            post.setUser(user);
        } else {
            post.setUser(guestPoster);
            post.setGuestPoster(user);
        }

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
            PostModel postModel = this.getPost(post,postMapper);
            if(post.getSharedPost() != null) {
                Post sharedPost = post.getSharedPost();
                postModel.setSharedPost(this.getSharedPost(sharedPost));
            }
            postModelList.add(postModel);
        }
        return new PostListResponse(postModelList, pageResponse);
    }

    @Override
    public PostListResponse fetchAllPosts(String email, int pageNo, int pageSize) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));

        Long userId = currentUser.getUserId();
        List<Long> friendIds = new ArrayList<>();

        for(Friendship friendship : currentUser.getFriends()){
            Long friendId = friendship.getFriends().getUserId();
            friendIds.add(friendId);
        }

        Page<Post> posts = postRepository.findPostsByUserIdAndFriendId(userId, friendIds, pageable);
        PageResponse pageResponse = this.getPagination(posts);
        List<PostModel> postModelList = new ArrayList<>();

        for(Post post : posts){
            PostModel postModel = this.getPost(post,postMapper);
            if(post.getSharedPost() != null) {
                Post sharedPost = post.getSharedPost();
                postModel.setSharedPost(this.getSharedPost(sharedPost));
            }
            postModelList.add(postModel);
        }

        return new PostListResponse(postModelList,pageResponse);
    }

    @Override
    public void sharePost(String email, Long postId, SharePostRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Post> sharedPost = postRepository.findById(postId);

        if(sharedPost.isPresent()) {
            Post post = sharedPost.get();
            Post newPost = new Post();
            newPost.setContent(request.getContent());
            newPost.setTimestamp(LocalDateTime.now());
            newPost.setUser(user);
            if (post.getSharedPost() != null && post.getSharedImage() == null) {
                newPost.setSharedPost(post.getSharedPost());
            }
            else if(post.getSharedImage() != null) {
                newPost.setSharedPost(post.getSharedPost());
                newPost.setSharedImage(post.getSharedImage());
            }
            else {
                newPost.setSharedPost(post);
            }
            postRepository.save(newPost);
        }
    }

    @Override
    public void sharePostImage(String email, Long postImageId,  Long postId,  SharePostRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<PostImage> postImage = postImageRepository.findById(postImageId);
        Optional<Post> sharedPost = postRepository.findById(postId);

        if(postImage.isPresent() && sharedPost.isPresent()){
            Post post = sharedPost.get();
            Post newPost = new Post();
            newPost.setContent(request.getContent());
            newPost.setTimestamp(LocalDateTime.now());
            newPost.setUser(user);

            if(post.getSharedPost() != null && post.getSharedImage() != null){
                newPost.setSharedPost(post.getSharedPost());
                newPost.setSharedImage(post.getSharedImage());
            } else if (post.getSharedImage() == null && post.getSharedPost() != null){
                newPost.setSharedPost(post.getSharedPost());
                newPost.setSharedImage(postImage.get());
            }
            else {
                newPost.setSharedImage(postImage.get());
                newPost.setSharedPost(post);
            }

            postRepository.save(newPost);
        }
    }

    @Override
    public SharedPostCountResponse getSharedPostImageCount(Long postImageId) {
        Long count = postRepository.countSharedPostImage(postImageId);

        SharedPostCountResponse sharedPostCountResponse = new SharedPostCountResponse();
        sharedPostCountResponse.setSharedPostCount(count);
        return sharedPostCountResponse;
    }

    @Override
    public SharedPostCountResponse getSharedPostCount(Long postId) {

        Long count = postRepository.countSharedPost(postId);

        SharedPostCountResponse sharedPostCountResponse = new SharedPostCountResponse();
        sharedPostCountResponse.setSharedPostCount(count);
        return sharedPostCountResponse;
    }

    @Override
    public void deletePost(String email, Long postId) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            postRepository.deleteById(postId);
        } else {
            throw new NoSuchElementException(StringUtil.USER_NOT_FOUND + email);
        }
    }

    @Override
    public PostResponse findPostCreatorById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_NOT_FOUND + postId));

        PostResponse postResponse = new PostResponse();

        postResponse.setPostId(post.getPostId());
        postResponse.setPostTimestamp(post.getTimestamp());
        postResponse.setContent(post.getContent());

        User poster = post.getGuestPoster() != null ? post.getGuestPoster() : post.getUser();
        postResponse.setUserId(poster.getUserId());
        postResponse.setFirstName(poster.getFirstName());
        postResponse.setLastName(poster.getLastName());
        postResponse.setProfilePicture(poster.getProfilePicture());

        return postResponse;
    }

    @Override
    public PostModel getPostById(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_NOT_FOUND + postId));

        PostModel postModel = this.getPost(post, postMapper);
        if(post.getSharedPost()!=null){
            Post sharedPost = post.getSharedPost();
            postModel.setSharedPost(this.getSharedPost(sharedPost));
        }

        return postModel;
    }
    private PostModel getPost(Post post, PostMapper postMapper) {
        PostModel postModel = postMapper.mapEntityToModel(post);
        postModel.setUserId(post.getUser().getUserId());
        postModel.setFirstName(post.getUser().getFirstName());
        postModel.setLastName(post.getUser().getLastName());
        postModel.setProfilePicture(post.getUser().getProfilePicture());
        return postModel;
    }

    private SharedPostResponse getSharedPost(Post sharedPost){

        SharedPostResponse sharedPostResponse = sharedPostMapper.mapEntityToModel(sharedPost);
        sharedPostResponse.setUserId(sharedPost.getUser().getUserId());
        sharedPostResponse.setFirstName(sharedPost.getUser().getFirstName());
        sharedPostResponse.setLastName(sharedPost.getUser().getLastName());
        sharedPostResponse.setProfilePicture(sharedPost.getUser().getProfilePicture());

        return sharedPostResponse;
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
