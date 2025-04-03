
package com.example.chat.service;

import com.example.chat.controller.dto.ChatMessageDto;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.Message;
import com.example.product.domain.Item;
import com.example.product.usecase.ItemSelectOneUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.ChatRoomRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public List<Message> getMessages(String chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }

    @Autowired
    private ItemSelectOneUseCase itemSelectOneUseCase;  // 상품 정보 조회용

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getChatRoomsWithItemInfo(String userId) {
        // 사용자가 참여한 채팅방 목록 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdOrReceiverId(userId, userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Map<String, Object> data = new HashMap<>();
            data.put("itemId", room.getChatRoomId());

            // 상품 정보 조회
            try {
                Item item = itemSelectOneUseCase.findById(Long.parseLong(room.getChatRoomId()));
                data.put("itemName", item.getItemName());
            } catch (Exception e) {
                data.put("itemName", "알 수 없는 상품");
                log.error("상품 정보 조회 실패: " + room.getChatRoomId(), e);
            }

            // 마지막 메시지 조회
            List<Message> messages = messageRepository.findByChatRoomId(String.valueOf(room.getChatRoomId()));
            if (!messages.isEmpty()) {
                // 최신 메시지 찾기
                Message lastMessage = messages.stream()
                        .max(Comparator.comparing(Message::getTimestamp))
                        .orElse(null);

                if (lastMessage != null) {
                    data.put("lastMessage", lastMessage.getMessage());
                    data.put("lastTimestamp", formatTimestamp(lastMessage.getTimestamp()));
                } else {
                    data.put("lastMessage", "");
                    data.put("lastTimestamp", "");
                }
            } else {
                data.put("lastMessage", "");
                data.put("lastTimestamp", "");
            }
            result.add(data);
        }
        return result;
    }

    private String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            return "";
        }
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}