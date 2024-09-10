package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.security.JwtService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            return LoginResponse.builder()
                    .jwtToken(jwtService.generateToken(authentication))
                    .role(authentication.getAuthorities().iterator().next().getAuthority())
                    .currentUser(authentication.getName())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(StringUtil.INVALID_CREDENTIALS);
        }
    }
}
