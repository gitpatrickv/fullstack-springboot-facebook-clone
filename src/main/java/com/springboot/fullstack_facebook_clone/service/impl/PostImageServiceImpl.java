package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PhotoListResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostImageResponse;
import com.springboot.fullstack_facebook_clone.entity.Post;
import com.springboot.fullstack_facebook_clone.entity.PostImage;
import com.springboot.fullstack_facebook_clone.repository.PostImageRepository;
import com.springboot.fullstack_facebook_clone.repository.PostRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                PostImage postImage = new PostImage();
                postImage.setPost(post.get());
                postImage.setPostImageUrl(this.processPostImages(postId, file));
                postImage.setTimestamp(LocalDateTime.now());
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

    @Override
    public PhotoListResponse fetchAllPhotos(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<PostImage> postImages = postImageRepository.findAllPostImagesByUserId(userId, pageable);
        PageResponse pageResponse = this.getPagination(postImages);

        List<PostImageResponse> postImageResponses = new ArrayList<>();

        for(PostImage postImage : postImages){
            PostImageResponse postImageResponse = new PostImageResponse();
            postImageResponse.setPostImageId(postImage.getPostImageId());
            postImageResponse.setPostImageUrl(postImage.getPostImageUrl());
            postImageResponse.setTimestamp(postImage.getTimestamp());
            postImageResponse.setPostId(postImage.getPost().getPostId());

            postImageResponses.add(postImageResponse);
        }

        return new PhotoListResponse(postImageResponses,pageResponse);
    }

    private PageResponse getPagination(Page<PostImage> images){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(images.getNumber());
        pageResponse.setPageSize(images.getSize());
        pageResponse.setTotalElements(images.getTotalElements());
        pageResponse.setTotalPages(images.getTotalPages());
        pageResponse.setLast(images.isLast());
        return pageResponse;
    }
}
