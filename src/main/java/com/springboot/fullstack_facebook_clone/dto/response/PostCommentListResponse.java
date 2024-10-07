package com.springboot.fullstack_facebook_clone.dto.response;

import com.springboot.fullstack_facebook_clone.dto.model.PostCommentModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentListResponse {

    private List<PostCommentModel> postCommentList;
    private PageResponse pageResponse;
}
