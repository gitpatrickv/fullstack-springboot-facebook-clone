package com.springboot.fullstack_facebook_clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeUserListResponse {
    private Long postLikeId;
    private Long userId;

    private String firstName;
    private String lastName;
}
