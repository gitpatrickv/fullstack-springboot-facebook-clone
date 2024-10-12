package com.springboot.fullstack_facebook_clone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.fullstack_facebook_clone.dto.model.PostImageModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SharedPostResponse {
    private Long postId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timestamp;
    private Long userId;
    private String firstName;
    private String lastName;
    private String profilePicture;
    List<PostImageModel> postImages = new ArrayList<>();
    private UserDataModel guestPoster;
}
