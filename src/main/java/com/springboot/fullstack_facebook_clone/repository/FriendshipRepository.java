package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Friendship;
import com.springboot.fullstack_facebook_clone.entity.constants.FriendshipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.user.userId = :userId AND f.friends.userId = :strangerId AND f.status = :status")
    Optional<Friendship> findByFriendship(@Param("userId") Long userId,
                                          @Param("strangerId") Long strangerId,
                                          @Param("status") FriendshipStatus status
    );

    void deleteByUser_UserIdAndFriends_UserId(Long userId, Long strangerId);
    Page<Friendship> findAllByStatusAndFriends_UserId(FriendshipStatus status, Long userId, Pageable pageable);
    Optional<Friendship> findByUser_UserIdAndFriends_UserId(Long userId, Long friendId);
    Optional<Friendship> findByStatusAndUser_UserIdAndFriends_UserId(FriendshipStatus status, Long userId, Long friendId);



}
