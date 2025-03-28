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
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 채팅방에 메시지 저장
    @Transactional
    public void saveMessage(ChatMessageDto chatMessageDto) {
        // 채팅방 조회 또는 생성
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndSenderIdAndReceiverId(
                chatMessageDto.getChatRoomId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getReceiverId()
        ).orElseGet(() -> createNewChatRoom(
                chatMessageDto.getChatRoomId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getReceiverId()
        ));

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage())
                .build();

        messageRepository.save(message);
    }
    @Transactional
    private ChatRoom createNewChatRoom(Long chatRoomId, String senderId, String receiverId) {
        ChatRoom newRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)  // 기존 필드명 유지
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        return chatRoomRepository.save(newRoom);
    }

    // 메시지 조회 수정
    @Transactional
    public List<Message> getMessages(Long chatRoomId, String senderId, String receiverId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndSenderIdAndReceiverId(
                chatRoomId, senderId, receiverId
        ).orElse(null);

        if (chatRoom == null) {
            return new ArrayList<>();
        }

        return messageRepository.findByChatRoomId(chatRoom.getChatRoomId());
    }

    
    @Autowired
    private ItemSelectOneUseCase itemSelectOneUseCase;  // 상품 정보 조회용

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getChatRoomsWithItemInfo(String userId) {
        // userId를 한 번만 전달 (동일한 값을 두 번 전달하지 않음)
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdOrReceiverId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Map<String, Object> data = new HashMap<>();
            data.put("itemId", room.getChatRoomId());

            // 채팅 상대방 정보 추가
            String otherUserId = userId.equals(room.getSenderId()) ?
                    room.getReceiverId() : room.getSenderId();
            data.put("otherUserId", otherUserId);

            // 나머지 코드는 동일
            try {
                Item item = itemSelectOneUseCase.findById(room.getChatRoomId());
                data.put("itemName", item.getItemName());
            } catch (Exception e) {
                data.put("itemName", "알 수 없는 상품");
                log.error("상품 정보 조회 실패: " + room.getChatRoomId(), e);
            }

            // 마지막 메시지 조회
            List<Message> messages = messageRepository.findByChatRoomId(room.getChatRoomId());
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
