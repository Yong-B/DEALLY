package com.example.product.usecase;

import com.example.product.domain.Item;

public interface ItemBuyerUseCase {
    Item addBuyerFromChatRoom(String chatRoomId, String loginId);
}
