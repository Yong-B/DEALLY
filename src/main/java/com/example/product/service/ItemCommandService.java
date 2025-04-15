package com.example.product.service;

import com.example.product.domain.Item;
import com.example.product.domain.ItemStatus;
import com.example.product.repository.ItemRepository;
import com.example.product.usecase.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCommandService implements ItemSaveUseCase, ItemSelectAllUseCase, ItemSelectOneUseCase, ItemUpdateUseCase, ItemDeleteUseCase {
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Page<Item> searchByItemName(String searchKeyword, Pageable pageable) {
        return itemRepository.findByItemNameContaining(searchKeyword, pageable);
    }

    @Override
    public Page<Item> findByUserIdItem(String userId, Pageable pageable) {
        return itemRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Item> findByBuyerIdItem(String buyerId, Pageable pageable) {
        return itemRepository.findByBuyerId(buyerId, pageable);
    }
    

    @Override
    public Item findById(Long id) {
        return itemRepository.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

    @Override
    public Item findByUserId(String userId) {
        return itemRepository.findProductByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + userId));
    }

    @Transactional
    @Override
    public Item update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.updateFields(
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getDescription()
        );

        return itemRepository.save(findItem);
    }

    @Override
    public void updateStatus(Long itemId, ItemStatus status) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));
        item.setStatus(status);
        itemRepository.save(item);
    }

    @Override
    public void delete(Long itemId) {
        Item findItem = findById(itemId);
        itemRepository.delete(findItem);

    }

}