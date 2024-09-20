package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostImage;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    @Override
    public void uploadPostImages(Long postId, MultipartFile[] files) {
        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()){
            for (MultipartFile file : files) {
                if(file.isEmpty()){
                    return;
                }
                PostImage postImage = new PostImage();
                postImage.setPost(post.get());
                postImage.setPostImageUrl(this.processPostImages(postId, file));
                postImageRepository.save(postImage);
            }
        }
    }

    @Override
    public byte[] getImages(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(StringUtil.PHOTO_DIRECTORY + filename));
    }

    @Override
    public String processPostImages(Long postId, MultipartFile image) {

        String filename = postId + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();

        try {
            Path fileStorageLocation = Paths.get(StringUtil.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename));

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/post/image/" + filename).toUriString();


        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }
}
