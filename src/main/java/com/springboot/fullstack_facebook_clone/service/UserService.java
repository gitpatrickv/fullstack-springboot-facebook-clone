package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserModel;
import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(UserModel userModel);
    String getAuthenticatedUser();
    UserModel getCurrentUserInfo(String email);
    UserModel getUserProfileInfo(Long userId);
    void uploadUserImage(String email, MultipartFile file, ImageType imageType, String description);
    String processImage(MultipartFile image);
    UserListResponse searchUser(String search,  int pageNo, int pageSize);
    PageResponse getUserPagination(Page<User> users);
    List<UserDataModel> getUserDataModels(Page<User> users);

}
