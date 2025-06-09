package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chatRoomId", columnDefinition = "varchar(255)")
    @JsonIgnore
    private ChatRoom chatRoom;   // 어떤 채팅방인지

    private String senderId;      // 보낸 사람
    private String message;       // 메시지 내용
    private LocalDateTime timestamp;  // 보낸 시간

    @PrePersist
    public void setTimestamp() {
        this.timestamp = LocalDateTime.now();
    }
} 