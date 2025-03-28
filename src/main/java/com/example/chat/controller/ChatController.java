package com.example.chat.controller;

import com.example.chat.domain.Message;
import com.example.chat.service.ChatService;
import com.example.product.domain.Item;
import com.example.product.usecase.ItemSelectOneUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ItemSelectOneUseCase itemSelectOneUseCase;
    private final ChatService chatService;
    
    @GetMapping("/basic/items/{itemId}/chat")
    public String chatPage(@PathVariable Long itemId, @AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        Item item = itemSelectOneUseCase.findById(itemId);

        // 상품 등록자의 userId를 receiverId로 설정
        Long chatRoomId = itemId;
        String receiverId = String.valueOf(item.getUserId());
        String senderId = memberDetails.getUsername(); // JWT 쿠키에서 가져온 로그인한 유저 ID

        model.addAttribute("chatRoomId", itemId);           // chatRoomId로 사용
        model.addAttribute("senderId", senderId);       // 현재 로그인한 유저
        model.addAttribute("receiverId", receiverId);   // 상품 등록자

        return "basic/chat";
    }

    @GetMapping("/chatroom/{chatRoomId}")
    @ResponseBody
    public List<Message> getMessages(@PathVariable Long chatRoomId,
                                     @RequestParam String senderId,
                                     @RequestParam String receiverId) {
        return chatService.getMessages(chatRoomId, senderId, receiverId);
    }


    @GetMapping("/basic/chatroom")
    public String chatRoomList(@AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        String userId = memberDetails.getUsername();

        // 사용자의 채팅방 목록 조회 로직
        List<Map<String, Object>> chatRoomsData = chatService.getChatRoomsWithItemInfo(userId);
        model.addAttribute("chatRooms", chatRoomsData);

        return "basic/chatroom";  // HTML 템플릿 경로
    }
}
