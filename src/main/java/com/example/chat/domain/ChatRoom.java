package com.example.chat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String chatRoomId; // 예: "1_a_b" (itemId_user1_user2)

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private String user1; // 채팅 참여자 1 (ID 기준 정렬된 작은 값)

    @Column(nullable = false)
    private String user2; // 채팅 참여자 2

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void setTimestamp() {
        this.createdAt = LocalDateTime.now();
    }
}
