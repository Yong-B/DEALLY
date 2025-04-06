package com.example.chat.controller;

import com.example.chat.domain.ChatRoom;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ItemSelectOneUseCase itemSelectOneUseCase;

    /**
     * 채팅방 입장 페이지
     */
    @GetMapping("/basic/items/{itemId}/chat")
    public String chatPage(@PathVariable Long itemId,
                           @RequestParam String chatRoomId, // ★ 이거 추가
                           @AuthenticationPrincipal CustomMemberDetails memberDetails,
                           Model model) {

        String currentUserId = memberDetails.getUsername();

        ChatRoom chatRoom = chatService.getChatRoom(chatRoomId); // 생성 X, 그냥 가져오기

        model.addAttribute("chatRoomId", chatRoom.getChatRoomId());
        model.addAttribute("userId", currentUserId);

        return "basic/chat";
    }

    @GetMapping("/basic/items/{itemId}/start-chat")
    public String startChat(@PathVariable Long itemId,
                            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        String loginUserId = memberDetails.getUsername();

        Item item = itemSelectOneUseCase.findById(itemId);
        String opponentId = String.valueOf(item.getUserId());

        // 본인이 자기 물건에 채팅하면 안 되니까 체크
        if (loginUserId.equals(opponentId)) {
            throw new IllegalArgumentException("자신의 상품과는 채팅할 수 없습니다.");
        }

        // ChatRoom 조회 or 생성
        ChatRoom chatRoom = chatService.createOrGetChatRoom(itemId, loginUserId, opponentId);

        // chatRoomId 붙여서 리다이렉트
        return "redirect:/basic/items/" + itemId + "/chat?chatRoomId=" + chatRoom.getChatRoomId();
    }
    
    /**
     * 채팅방 내 메시지 조회 (AJAX 등)
     */
    @GetMapping("/chatroom/{chatRoomId}")
    @ResponseBody
    public List<Message> getMessages(@PathVariable String chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }

    /**
     * 채팅방 목록 페이지
     */
    @GetMapping("/basic/chatroom")
    public String chatRoomList(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                               Model model) {
        String userId = memberDetails.getUsername();

        List<Map<String, Object>> chatRoomsData = chatService.getChatRoomsWithItemInfo(userId);
        model.addAttribute("chatRooms", chatRoomsData);

        return "basic/chatroom"; // 채팅방 목록 화면
    }

    @GetMapping("/chat/enter/{chatRoomId}")
    public String enterChatRoom(@PathVariable String chatRoomId,
                                @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        String currentUserId = memberDetails.getUsername();
        ChatRoom chatRoom = chatService.getChatRoom(chatRoomId);

        if (!chatRoom.getUser1().equals(currentUserId) && !chatRoom.getUser2().equals(currentUserId)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }

        Long itemId = chatRoom.getItemId();

        // 여기서 기존 chatRoomId를 넘긴다! (중요)
        return "redirect:/basic/items/" + itemId + "/chat?chatRoomId=" + chatRoomId;
    }
}
