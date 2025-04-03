package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);

    List<ChatRoom> findBySenderIdOrReceiverId(String senderId, String receiverId);
}