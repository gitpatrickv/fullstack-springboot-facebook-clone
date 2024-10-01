package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.PostCommentModel;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentListResponse;
import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostComment;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.PostCommentRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.PostCommentService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PostCommentServiceImpl implements PostCommentService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    @Override
    public void writePostComment(String email, Long postId, String comment, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException(StringUtil.POST_NOT_FOUND + postId));

        PostComment postComment = new PostComment();
        postComment.setComment(comment);
        if(file != null) {
            postComment.setCommentImage(userService.processUserImage(user.getEmail(), file));
        }
        postComment.setUser(user);
        postComment.setPost(post);
        postComment.setTimestamp(LocalDateTime.now());
        postCommentRepository.save(postComment);
    }

    @Override
    public PostCommentListResponse fetchAllPostComments(Long postId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostComment> postComments = postCommentRepository.findAllByPost_PostId(postId, pageable);
        PageResponse pageResponse = this.getPagination(postComments);

        List<PostCommentModel> postCommentModelList = new ArrayList<>();

        for(PostComment postComment : postComments){
            PostCommentModel postCommentModel = new PostCommentModel();
            postCommentModel.setPostCommentId(postComment.getPostCommentId());
            postCommentModel.setComment(postComment.getComment());
            postCommentModel.setCommentImage(postComment.getCommentImage());
            postCommentModel.setFirstName(postComment.getUser().getFirstName());
            postCommentModel.setLastName(postComment.getUser().getLastName());
            postCommentModel.setProfilePicture(postComment.getUser().getProfilePicture());
            postCommentModel.setTimestamp(postComment.getTimestamp());
            postCommentModelList.add(postCommentModel);
        }

        return new PostCommentListResponse(postCommentModelList, pageResponse);
    }

    @Override
    public PostCommentCountResponse getCommentCount(Long postId) {
        Long commentCount = postCommentRepository.findAllByPost_PostId(postId).stream().count();

        PostCommentCountResponse countResponse = new PostCommentCountResponse();
        countResponse.setPostCommentCount(commentCount);

        return countResponse;
    }

    private PageResponse getPagination(Page<PostComment> postComments){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(postComments.getNumber());
        pageResponse.setPageSize(postComments.getSize());
        pageResponse.setTotalElements(postComments.getTotalElements());
        pageResponse.setTotalPages(postComments.getTotalPages());
        pageResponse.setLast(postComments.isLast());
        return pageResponse;
    }
}
