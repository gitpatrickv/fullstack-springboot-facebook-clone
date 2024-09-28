package com.springboot.fullstack_facebook_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_images")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;
    private String postImageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

//    @OneToMany(mappedBy = "postImage", cascade = CascadeType.ALL)
//    private List<PostComment> postComments = new ArrayList<>();
}
