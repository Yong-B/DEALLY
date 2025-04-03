package com.example.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomListDto {
    private String chatRoomId;      // 채팅방 ID (= 상품 ID)
    private String itemName;      // 상품 이름
    private String lastMessage;   // 마지막 메시지
    private LocalDateTime lastTimestamp; // 마지막 메시지 시간
    private String formattedTimestamp; // 포맷팅된 시간
}