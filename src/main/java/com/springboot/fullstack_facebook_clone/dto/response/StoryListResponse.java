package com.springboot.fullstack_facebook_clone.dto.response;

import com.springboot.fullstack_facebook_clone.dto.model.StoryModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryListResponse {

    private Long userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private List<StoryModel> storyModels;

}
