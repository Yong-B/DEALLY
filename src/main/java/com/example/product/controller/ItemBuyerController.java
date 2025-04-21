package com.example.product.controller;

import com.example.product.domain.Item;
import com.example.product.service.ItemBuyerService;
import com.example.product.usecase.ItemSelectOneUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemBuyerController {

    private final ItemSelectOneUseCase itemSelectOneUseCase;
    private final ItemBuyerService itemBuyerService;

    @GetMapping("/basic/{itemId}/chatroom-selection")
    public String selection(@PathVariable Long itemId, @AuthenticationPrincipal CustomMemberDetails memberDetails,
                            Model model) {
        String userId = memberDetails.getUsername();
        Item item = itemSelectOneUseCase.findById(itemId);
        List<Map<String, Object>> chatRoomsData = itemBuyerService.getChatRoomsWithItemInfoByItemId(itemId, userId);
        model.addAttribute("chatRooms", chatRoomsData);

        return "basic/chatroom-selection"; // 채팅방 목록 화면
    }

    @PostMapping("/basic/{itemId}/chatroom-selection")
    public String buyerIdSelect(@PathVariable Long itemId,
                                @RequestParam String chatRoomId,
                                @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        itemBuyerService.addBuyerFromChatRoom(chatRoomId, memberDetails.getUsername());
        return "basic/items";
    }
}
