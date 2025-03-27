package com.example.chat.controller.dto;

import io.jsonwebtoken.security.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType{
        ENTER, TALK
    }
    
    private MessageType messageType; // 메시지 타입
    
    private Long chatRoomId; // 방 번호
    
    private String senderId; // 채팅을 보낸 사람

    private String receiverId;

    private String message;

}
