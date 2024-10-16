package com.springboot.fullstack_facebook_clone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostImageResponse {

    private Long postImageId;
    private String postImageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timestamp;
    private Long postId;
}
