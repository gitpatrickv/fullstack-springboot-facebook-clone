package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.FriendshipStatusResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.entity.Friendship;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.FriendshipStatus;
import com.springboot.fullstack_facebook_clone.repository.FriendshipRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public void addToFriend(String currentUser, Long strangerUserId) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        Optional<Friendship> friendRequest = friendshipRepository.findByFriendship(user.getUserId(), strangerUserId, FriendshipStatus.PENDING);

        if(user.getUserId().equals(strangerUserId)){
            throw new IllegalArgumentException(StringUtil.FRIEND_REQUEST_NOT_ALLOWED);
        }

        if(friendRequest.isPresent()){
            friendshipRepository.deleteByUser_UserIdAndFriends_UserId(user.getUserId(), strangerUserId);
        }
        else {
            User stranger = userRepository.findById(strangerUserId)
                    .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + strangerUserId));

            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriends(stranger);
            friendship.setStatus(FriendshipStatus.PENDING);
            friendship.setTimestamp(LocalDateTime.now());
            friendshipRepository.save(friendship);
        }
    }

    @Override
    public void acceptFriendRequest(String currentUser, Long strangerUserId) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        User stranger = userRepository.findById(strangerUserId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + strangerUserId));

        Optional<Friendship> friendRequest = friendshipRepository.findByFriendship(strangerUserId, user.getUserId(), FriendshipStatus.PENDING);

        if(friendRequest.isPresent()) {

            friendRequest.get().setStatus(FriendshipStatus.FRIENDS);

            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriends(stranger);
            friendship.setStatus(FriendshipStatus.FRIENDS);
            friendship.setTimestamp(LocalDateTime.now());
            friendshipRepository.save(friendship);
        }
    }

    @Override
    public UserListResponse fetchAllFriendRequest(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Friendship> friendships = friendshipRepository.findAllByStatusAndFriends_UserId(FriendshipStatus.PENDING, userId, pageable);
        PageResponse pageResponse = this.getPagination(friendships);

        List<UserDataModel> userDataModels = this.getUserDataModels(friendships);
        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public UserListResponse fetchAllUserFriends(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Friendship> friendships = friendshipRepository.findAllByStatusAndUser_UserId(FriendshipStatus.FRIENDS, userId, pageable);
        PageResponse pageResponse = this.getPagination(friendships);

        List<UserDataModel> userDataModels = this.getFriendsDataModels(friendships);
        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public UserListResponse fetchAllFriendSuggestions(Long userid, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> userList = friendshipRepository.findFriendSuggestions(userid,pageable);
        PageResponse pageResponse = this.getUserPagination(userList);

       List<UserDataModel> userDataModels = new ArrayList<>();

       for(User user : userList){
           UserDataModel userDataModel = new UserDataModel();
           userDataModel.setUniqueId(user.getUserId() + 1000L);
           userDataModel.setUserId(user.getUserId());
           userDataModel.setFirstName(user.getFirstName());
           userDataModel.setLastName(user.getLastName());
           userDataModel.setProfilePicture(user.getProfilePicture());
           userDataModels.add(userDataModel);
       }

        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public FriendshipStatusResponse getFriendshipStatus(String currentUser, Long friendId, boolean isRequestStatus) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        Optional<Friendship> friendship;

        if(isRequestStatus){
            friendship = friendshipRepository.findByUser_UserIdAndFriends_UserId(user.getUserId(), friendId);
        } else {
            friendship = friendshipRepository.findByFriendship(friendId, user.getUserId(), FriendshipStatus.PENDING);
        }

        if(friendship.isEmpty()){
            return null;
        }

        FriendshipStatusResponse friendshipStatusResponse = new FriendshipStatusResponse();
        friendshipStatusResponse.setStatus(friendship.get().getStatus().toString());

        return friendshipStatusResponse;
    }

    @Override
    public void unfriend(Long userId, Long friendId) {

        Optional<Friendship> friendship1 = friendshipRepository.findByFriendship(userId, friendId, FriendshipStatus.FRIENDS);
        friendship1.ifPresent(friendshipRepository::delete);

        Optional<Friendship> friendship2 = friendshipRepository.findByFriendship(friendId, userId, FriendshipStatus.FRIENDS);
        friendship2.ifPresent(friendshipRepository::delete);
    }

    @Override
    public void deleteFriendRequest(Long userId, Long strangerId) {
        Optional<Friendship> friendship = friendshipRepository.findByFriendship(strangerId, userId, FriendshipStatus.PENDING);
        friendship.ifPresent(friendshipRepository::delete);
    }

    @Override
    public CountResponse getFriendListCount(Long userId) {
        Long count = friendshipRepository.getFriendListCount(userId);

        CountResponse countResponse = new CountResponse();
        countResponse.setCount(count);
        return countResponse;
    }

    private List<UserDataModel> getFriendsDataModels(Page<Friendship> friendships) {
        List<UserDataModel> userDataModels = new ArrayList<>();

        for(Friendship friendship : friendships){
            UserDataModel userDataModel = new UserDataModel();
            userDataModel.setUniqueId(friendship.getFriendshipId());
            userDataModel.setUserId(friendship.getFriends().getUserId());
            userDataModel.setFirstName(friendship.getFriends().getFirstName());
            userDataModel.setLastName(friendship.getFriends().getLastName());
            userDataModel.setProfilePicture(friendship.getFriends().getProfilePicture());
            userDataModels.add(userDataModel);
        }

        return userDataModels;
    }

    private List<UserDataModel> getUserDataModels(Page<Friendship> friendships) {
        List<UserDataModel> userDataModels = new ArrayList<>();

        for(Friendship friendship : friendships){
            UserDataModel userDataModel = new UserDataModel();
            userDataModel.setUniqueId(friendship.getFriendshipId());
            userDataModel.setUserId(friendship.getUser().getUserId());
            userDataModel.setFirstName(friendship.getUser().getFirstName());
            userDataModel.setLastName(friendship.getUser().getLastName());
            userDataModel.setProfilePicture(friendship.getUser().getProfilePicture());
            userDataModels.add(userDataModel);
        }
        return userDataModels;
    }

    private PageResponse getPagination(Page<Friendship> friendships){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(friendships.getNumber());
        pageResponse.setPageSize(friendships.getSize());
        pageResponse.setTotalElements(friendships.getTotalElements());
        pageResponse.setTotalPages(friendships.getTotalPages());
        pageResponse.setLast(friendships.isLast());
        return pageResponse;
    }

    private PageResponse getUserPagination(Page<User> users){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(users.getNumber());
        pageResponse.setPageSize(users.getSize());
        pageResponse.setTotalElements(users.getTotalElements());
        pageResponse.setTotalPages(users.getTotalPages());
        pageResponse.setLast(users.isLast());
        return pageResponse;
    }
}
