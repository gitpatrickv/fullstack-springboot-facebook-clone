package com.springboot.fullstack_facebook_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "story")
public class Story extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyId;
    private String text;
    private String storyImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
