package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.model.UserModel;
import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ErrorResponse;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.entity.constants.ImageType;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostImageService postImageService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse loginResponse = userService.login(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(StringUtil.INVALID_CREDENTIALS);
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@RequestBody @Valid UserModel userModel) {
        return userService.register(userModel);
    }

    @GetMapping
    public UserModel getCurrentUserInfo() {
        String currentUser = userService.getAuthenticatedUser();
        return userService.getCurrentUserInfo(currentUser);
    }
    @GetMapping("/profile/{userId}")
    public UserModel getUserProfileInfo(@PathVariable Long userId) {
        return userService.getUserProfileInfo(userId);
    }

    @PostMapping("/profile/picture/upload/{imageType}")
    public void uploadUserImage(@PathVariable(value = "imageType") ImageType imageType,
                                @RequestPart(value = "file") MultipartFile file,
                                @RequestPart(value="description", required = false) String description){
        String currentUser = userService.getAuthenticatedUser();
        userService.uploadUserImage(currentUser,file,imageType, description);
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getUserPhoto(@PathVariable("filename") String filename) throws IOException {
        return postImageService.getImages(filename);
    }
}
