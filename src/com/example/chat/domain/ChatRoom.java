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

    @Column(nullable = false, columnDefinition = "varchar(255)", unique = true)
    private String chatRoomId;       // 채팅방 ID (상품ID_구매자ID 형식)
    private String senderId;         // 보낸 사람 ID
    private String receiverId;       // 받는 사람 ID

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime createdAt;  // 메시지 보낸 시간

    @PrePersist
    public void setTimestamp() {
        this.createdAt = LocalDateTime.now();
    }

    // 채팅방 ID 생성 메서드
    public static String createChatRoomId(Long itemId, String buyerId) {
        return itemId + "_" + buyerId;
    }

    // 상품 ID 추출 메서드
    public Long getItemId() {
        return Long.parseLong(this.chatRoomId.split("_")[0]);
    }

    // 구매자 ID 추출 메서드
    public String getBuyerId() {
        return this.chatRoomId.split("_")[1];
    }
} 