package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.PostCommentCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentListResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostCommentService {

    void writePostComment(String email, Long postId, String comment, MultipartFile file);
    PostCommentListResponse fetchAllPostComments(Long postId, int pageNo, int pageSize);
    PostCommentCountResponse getCommentCount(Long postId);
}
