package com.example.product.service;

import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.Message;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.MessageRepository;
import com.example.product.domain.Item;
import com.example.product.repository.ItemRepository;
import com.example.product.usecase.ItemBuyerUseCase;
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
public class ItemBuyerService implements ItemBuyerUseCase {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ItemSelectOneUseCase itemSelectOneUseCase;
    private final ItemRepository itemRepository;
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getChatRoomsWithItemInfoByItemId(Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByItemId(itemId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Map<String, Object> data = new HashMap<>();
            data.put("chatRoomId", room.getChatRoomId());
            data.put("itemId", room.getItemId());
            
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

    private String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) return "";
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public Item addBuyerFromChatRoom(String chatRoomId, String loginId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        String buyerId;

        // user1과 user2 중 로그인한 사용자가 아닌 쪽을 buyer로 설정
        if (loginId.equals(chatRoom.getUser1())) {
            buyerId = chatRoom.getUser2();
        } else if (loginId.equals(chatRoom.getUser2())) {
            buyerId = chatRoom.getUser1();
        } else {
            throw new RuntimeException("로그인 사용자는 이 채팅방에 속해 있지 않습니다.");
        }

        // Item에 buyer 설정
        Item item = itemSelectOneUseCase.findById(chatRoom.getItemId());
        item.addBuyer(buyerId);
        return itemRepository.save(item);
    }
}
