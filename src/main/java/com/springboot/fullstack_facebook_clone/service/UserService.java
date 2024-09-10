package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
}
