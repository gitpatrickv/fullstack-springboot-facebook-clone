package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.constants.ChatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN c.users u1 JOIN c.users u2 WHERE u1.id = :userId AND u2.id = :friendId AND c.chatType = :chatType")
    Optional<Chat> findExistingChat(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("chatType") ChatType chatType);
    @Query("SELECT DISTINCT c FROM Chat c JOIN c.users u WHERE u.id = :userId AND SIZE(c.messages) > 0")
    Page<Chat> findAllUserChats(@Param("userId") Long userId, Pageable pageable);

}
