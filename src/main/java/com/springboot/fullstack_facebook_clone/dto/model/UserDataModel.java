package com.springboot.fullstack_facebook_clone.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataModel {
    private Long uniqueId;
    private Long userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
}
