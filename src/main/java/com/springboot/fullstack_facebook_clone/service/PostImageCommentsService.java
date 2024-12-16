package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.PostCommentCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentListResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostImageCommentsService {

    void writePostImageComment(Long postImageId, String comment, MultipartFile file);
    PostCommentListResponse fetchAllPostImageComments(Long postImageId, int pageNo, int pageSize);
    PostCommentCountResponse getPostImageCommentCount(Long postImageId);
}
