package com.springboot.fullstack_facebook_clone.dto.response;

import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

   List<UserDataModel> userList = new ArrayList<>();
    private PageResponse pageResponse;
}
