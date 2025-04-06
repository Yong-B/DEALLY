package com.example.chat.repository;

import com.example.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.timestamp ASC")
    List<Message> findByChatRoomId(@Param("chatRoomId") String chatRoomId);
}
