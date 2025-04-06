package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 채팅방 ID로 조회
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);

    // 로그인한 유저가 user1 또는 user2인 모든 채팅방 조회
    List<ChatRoom> findByUser1OrUser2(String user1, String user2);
}
