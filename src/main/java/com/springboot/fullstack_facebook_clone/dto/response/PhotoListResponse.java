package com.springboot.fullstack_facebook_clone.dto.response;

import com.springboot.fullstack_facebook_clone.dto.model.PostImageModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoListResponse {

    List<PostImageResponse> postImageModels = new ArrayList<>();
    private PageResponse pageResponse;
}
