package com.springboot.fullstack_facebook_clone.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Timestamp {
    private LocalDateTime timestamp;
}
