package com.example.product.usecase;

import com.example.product.domain.Item;

public interface ItemUpdateUseCase {
    Item update(Long itemId, Item updateParam);
}
