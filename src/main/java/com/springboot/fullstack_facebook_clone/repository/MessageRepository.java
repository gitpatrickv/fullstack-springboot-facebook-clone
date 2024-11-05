package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.chatId =:chatId")
    Page<Message> findChatMessages (@Param("chatId") Long chatId,
                               Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chat.chatId = :chatId ORDER BY m.timestamp DESC")
    List<Message> findLastMessageByChatId(@Param("chatId") Long chatId, Pageable pageable);
}
