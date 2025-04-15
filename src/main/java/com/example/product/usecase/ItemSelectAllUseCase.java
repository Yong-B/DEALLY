package com.example.product.usecase;

import com.example.product.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface ItemSelectAllUseCase {
    Page<Item> findAll(Pageable pageable);

    Page<Item> searchByItemName(String searchKeyword, Pageable pageable);

    Page<Item> findByUserIdItem(String userId, Pageable pageable);

    Page<Item> findByBuyerIdItem(String buyerId, Pageable pageable);
    
}
