package com.example.product.usecase;

import com.example.product.domain.Item;

public interface ItemSelectOneUseCase {
    Item findById(Long id);

    Item findByUserId(String userId);
}
