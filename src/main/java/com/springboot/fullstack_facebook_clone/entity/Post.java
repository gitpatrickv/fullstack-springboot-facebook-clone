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
@Table(name = "posts")
public class Post extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String content;

    @OneToMany(mappedBy = "post",  cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

}
