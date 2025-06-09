package com.example.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType; // 메시지 타입
    private String chatRoomId;      // 방 번호 (상품ID_구매자ID 형식)
    private String senderId;        // 채팅을 보낸 사람
    private String receiverId;      // 받는 사람
    private String message;         // 메시지 내용
} 