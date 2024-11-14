package com.springboot.fullstack_facebook_clone.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupChatNameRequest {
    @NotNull
    private Long chatId;
    @NotNull
    private String name;
}
