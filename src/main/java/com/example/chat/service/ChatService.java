package com.example.chat.service;

import com.example.chat.controller.dto.ChatMessageDto;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.ChatRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 채팅방에 메시지 저장
    @Transactional
    public void saveMessage(ChatMessageDto chatMessageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다"));

        // 메시지 생성
        Message message = Message.builder()
                .chatRoom(chatRoom)
                .senderId(chatMessageDto.getSenderId().toString())
                .message(chatMessageDto.getMessage())
                .build();

        // 메시지 저장
        messageRepository.save(message);
    }

    // 채팅방의 메시지 목록 조회
    @Transactional
    public List<Message> getMessages(Long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }
}
