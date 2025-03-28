package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.chatRoomId = :chatRoomId " +
            "AND ((cr.senderId = :senderId AND cr.receiverId = :receiverId) " +
            "OR (cr.senderId = :receiverId AND cr.receiverId = :senderId))")
    @Transactional(readOnly = true)
    Optional<ChatRoom> findByChatRoomIdAndSenderIdAndReceiverId(
            @Param("chatRoomId") Long chatRoomId,
            @Param("senderId") String senderId,
            @Param("receiverId") String receiverId
    );

    @Query("SELECT cr FROM ChatRoom cr WHERE " +
            "(cr.senderId = :userId OR cr.receiverId = :userId)")
    @Transactional(readOnly = true)
    List<ChatRoom> findBySenderIdOrReceiverId(@Param("userId") String userId);
}