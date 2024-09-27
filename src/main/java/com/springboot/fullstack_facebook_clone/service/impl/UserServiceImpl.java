package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.UserModel;
import com.springboot.fullstack_facebook_clone.dto.request.LoginRequest;
import com.springboot.fullstack_facebook_clone.dto.response.LoginResponse;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ImageType;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.security.JwtService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.UserMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper mapper;
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

    @Override
    public LoginResponse register(UserModel userModel) {

        boolean isEmailExists = userRepository.existsByEmailIgnoreCase(userModel.getEmail());

        if (isEmailExists) {
            throw new EntityExistsException(StringUtil.USER_EXISTS + userModel.getEmail());
        }

        User user = mapper.mapUserModelToUserEntity(userModel);
        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            return LoginResponse.builder()
                    .jwtToken(jwtService.generateToken(authentication))
                    .role(authentication.getAuthorities().iterator().next().getAuthority())
                    .currentUser(authentication.getName())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(StringUtil.INVALID_CREDENTIALS);
        }
    }

    @Override
    public String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public UserModel getCurrentUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        return mapper.mapUserEntityToUserModel(user);
    }

    @Override
    public UserModel getUserProfileInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));

        return mapper.mapUserEntityToUserModel(user);
    }

    @Override
    public void uploadUserImage(String email, MultipartFile file, ImageType imageType) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            if(file != null) {
                if (imageType.equals(ImageType.PROFILE_PICTURE)) {
                    user.setProfilePicture(processUserImage(email, file));
                } else if (imageType.equals(ImageType.COVER_PHOTO)) {
                    user.setCoverPhoto(processUserImage(email, file));
                }
                userRepository.save(user);
            }
        }
    }

    @Override
    public String processUserImage(String email, MultipartFile image) {
        String filename = email + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();

        try {
            Path fileStorageLocation = Paths.get(StringUtil.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/user/image/" + filename).toUriString();


        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }
}
