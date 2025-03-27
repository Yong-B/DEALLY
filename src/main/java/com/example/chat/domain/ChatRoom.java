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
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long chatRoomId;       // 채팅방 ID (상품 ID)
    private String  senderId;         // 보낸 사람 ID
    private String  receiverId;       // 받는 사람 ID

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime createdAt;  // 메시지 보낸 시간

    @PrePersist
    public void setTimestamp() {
        this.createdAt = LocalDateTime.now();
    }
}
