package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.UserModel;
import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.entity.constants.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(UserModel userModel);
    String getAuthenticatedUser();
    UserModel getCurrentUserInfo(String email);
    void uploadUserImage(String email, MultipartFile file, ImageType imageType);
    String processUserImage(String email, MultipartFile image);
}
