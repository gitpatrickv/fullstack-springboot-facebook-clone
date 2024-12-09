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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_poster_id")
    private User guestPoster;

    @ManyToOne
    @JoinColumn(name = "shared_post_id")
    private Post sharedPost;

    @ManyToOne
    @JoinColumn(name = "shared_image_id")
    private PostImage sharedImage;

    @OneToMany(mappedBy = "sharedPost", cascade = CascadeType.ALL)
    private List<Post> sharedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post",  cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

}
