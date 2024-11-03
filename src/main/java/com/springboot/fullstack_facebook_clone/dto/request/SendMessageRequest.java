package com.springboot.fullstack_facebook_clone.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendMessageRequest {
    private Long userId;
    private Long chatId;
    private String text;
}
