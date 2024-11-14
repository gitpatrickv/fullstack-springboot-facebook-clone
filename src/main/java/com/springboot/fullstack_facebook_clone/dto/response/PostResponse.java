package com.springboot.fullstack_facebook_clone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postTimestamp;
    private Long userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
}
