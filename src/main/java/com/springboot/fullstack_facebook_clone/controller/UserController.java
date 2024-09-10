package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse loginResponse = userService.login(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
