package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.StoryModel;
import com.springboot.fullstack_facebook_clone.dto.response.StoryListResponse;
import com.springboot.fullstack_facebook_clone.entity.Friendship;
import com.springboot.fullstack_facebook_clone.entity.Story;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.FriendshipStatus;
import com.springboot.fullstack_facebook_clone.repository.StoryRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.StoryService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.StoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final StoryMapper storyMapper;

    @Override
    public void createStory(Long userId, String text, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));

        Story story = new Story();
        story.setText(text);
        if(file != null) {
            story.setStoryImage(userService.processImage(file));
        }
        story.setTimestamp(LocalDateTime.now());
        story.setUser(user);
        storyRepository.save(story);
    }

    @Override
    public List<StoryListResponse> fetchAllStories(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));

        Long currentUserId = currentUser.getUserId();
        List<Long> friendIds = new ArrayList<>();
        friendIds.add(currentUserId);

        for(Friendship friendship : currentUser.getFriends()){
            if(friendship.getStatus().equals(FriendshipStatus.FRIENDS)) {
                Long friendId = friendship.getFriends().getUserId();
                friendIds.add(friendId);
            }
        }

        List<User> users = userRepository.findUsersById(friendIds);
        List<StoryListResponse> storyListResponses = new ArrayList<>();

        for(User user : users) {
            List<Story> stories = user.getStories();
            if(stories != null && !stories.isEmpty()) {
            StoryListResponse storyListResponse = new StoryListResponse();
            storyListResponse.setUserId(user.getUserId());
            storyListResponse.setProfilePicture(user.getProfilePicture());
            storyListResponse.setFirstName(user.getFirstName());
            storyListResponse.setLastName(user.getLastName());

            List<StoryModel> storyModels = new ArrayList<>();
            for(Story story : stories) {
                StoryModel storyModel = storyMapper.mapEntityToModel(story);
                storyModels.add(storyModel);
            }

            storyModels.sort(Comparator.comparing(StoryModel::getTimestamp).reversed());

            storyListResponse.setStoryModels(storyModels);
            storyListResponses.add(storyListResponse);
            }
        }

        return storyListResponses;
    }

    @Override
    public void deleteStory(Long storyId) {
        storyRepository.deleteById(storyId);
    }
}
