package com.example.chat.service;

import com.example.chat.controller.dto.ChatMessageDto;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.Message;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.MessageRepository;
import com.example.product.domain.Item;
import com.example.product.usecase.ItemSelectOneUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ItemSelectOneUseCase itemSelectOneUseCase;

    /**
     * 채팅방 생성 또는 조회
     * - user1, user2를 정렬해서 고정된 순서로 저장하여 중복 방지
     */
    @Transactional
    public ChatRoom createOrGetChatRoom(Long itemId, String userId1, String userId2) {
        String[] users = {userId1, userId2};
        Arrays.sort(users);
        String chatRoomId = itemId + "_" + users[0] + "_" + users[1];

        return chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseGet(() -> {
                    ChatRoom chatRoom = ChatRoom.builder()
                            .chatRoomId(chatRoomId)
                            .itemId(itemId)
                            .user1(users[0])
                            .user2(users[1])
                            .build();
                    return chatRoomRepository.save(chatRoom);
                });
    }

    /**
     * 채팅 메시지 저장
     */
    @Transactional
    public void saveMessage(ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage())
                .build();

        messageRepository.save(message);
    }

    /**
     * 채팅방의 메시지 전체 조회
     */
    @Transactional(readOnly = true)
    public List<Message> getMessages(String chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }

    /**
     * 유저가 참여한 채팅방 목록 + 상품 정보 + 최근 메시지 반환
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getChatRoomsWithItemInfo(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUser1OrUser2(userId, userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Map<String, Object> data = new HashMap<>();
            data.put("chatRoomId", room.getChatRoomId());

            // itemId 추출
            String[] parts = room.getChatRoomId().split("_");
            Long itemId = Long.parseLong(parts[0]);
            data.put("itemId", itemId);

            // 상품 정보 조회
            try {
                Item item = itemSelectOneUseCase.findById(itemId);
                data.put("itemName", item.getItemName());
            } catch (Exception e) {
                data.put("itemName", "알 수 없는 상품");
                log.error("❌ 상품 정보 조회 실패 - chatRoomId: {}", room.getChatRoomId(), e);
            }

            // 마지막 메시지 조회
            List<Message> messages = messageRepository.findByChatRoomId(room.getChatRoomId());
            if (!messages.isEmpty()) {
                Message lastMessage = messages.stream()
                        .max(Comparator.comparing(Message::getTimestamp))
                        .orElse(null);

                if (lastMessage != null) {
                    data.put("lastMessage", lastMessage.getMessage());
                    data.put("lastTimestamp", formatTimestamp(lastMessage.getTimestamp()));
                }
            } else {
                data.put("lastMessage", "");
                data.put("lastTimestamp", "");
            }

            result.add(data);
        }

        return result;
    }
    /**
     * 시간 포맷 변환기
     */
    private String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) return "";
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * chatRoomId로 채팅방 조회
     */
    @Transactional(readOnly = true)
    public ChatRoom getChatRoom(String chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
    }
}
