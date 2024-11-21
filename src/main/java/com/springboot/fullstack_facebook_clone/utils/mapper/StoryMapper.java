package com.springboot.fullstack_facebook_clone.utils.mapper;

import com.springboot.fullstack_facebook_clone.dto.model.StoryModel;
import com.springboot.fullstack_facebook_clone.entity.Story;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoryMapper {

    private final ModelMapper mapper = new ModelMapper();

    public StoryModel mapEntityToModel(Story story) {
        return mapper.map(story, StoryModel.class);
    }
}
